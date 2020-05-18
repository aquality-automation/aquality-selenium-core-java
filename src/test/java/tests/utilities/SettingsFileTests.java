package tests.utilities;

import aquality.selenium.core.applications.AqualityModule;
import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.applications.CustomAqualityServices;
import tests.applications.TestModule;
import tests.configurations.BaseProfileTest;

import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

import static org.testng.Assert.*;

public class SettingsFileTests extends BaseProfileTest {

    private static final String TIMEOUT_POLLING_INTERVAL_PATH = "/timeouts/timeoutPollingInterval";
    private static final String NULLVALUE_PATH = "/nullValue";
    private static final String ABSENTVALUE_PATH = "/absentvalue";
    private static final String TIMEOUT_POLLING_INTERVAL_KEY = "timeouts.timeoutPollingInterval";
    private static final String LANGUAGE_ENV_KEY = "logger.language";
    private static final String ARGUMENTS_ENV_KEY = "arguments.start";
    private static final String BOOLEANVALUE_ENV_KEY = "booleanValue";
    private static final String PROFILE = "jsontest";
    private static final String FILE_NAME = String.format("settings.%s.json", PROFILE);
    private static final Map<String, String> EXPECTED_LANGUAGES = new HashMap<String, String>() {{
        put("language", "ru");
    }};

    private ISettingsFile jsonSettingsFile;

    @BeforeMethod
    public void before() {
        System.setProperty(PROFILE_KEY, PROFILE);
        CustomAqualityServices.initInjector(getTestModule());
        jsonSettingsFile = CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class);
    }

    @Test
    public void testShouldBePossibleToGetMapWithJsonObject() {
        Map<String, Object> capabilities = jsonSettingsFile.getMap("/".concat("capabilities"));
        assertFalse(capabilities.get("loggingPrefs").toString().isEmpty());
    }

    @Test
    public void testShouldBePossibleToOverrideBooleanValueViaEnvironmentVariable() {
        BooleanSupplier getCurrentValue = () -> (Boolean) jsonSettingsFile.getValue("/".concat(BOOLEANVALUE_ENV_KEY));
        boolean oldValue = getCurrentValue.getAsBoolean();
        boolean targetValue = !oldValue;
        System.setProperty(BOOLEANVALUE_ENV_KEY, String.valueOf(targetValue));
        assertEquals(getCurrentValue.getAsBoolean(), targetValue, "value passed via env var is not used by SettingsFile");
    }

    @Test
    public void testShouldBePossibleToSetValueWhichIsAbsentInJsonFile() {
        Assert.assertFalse(jsonSettingsFile.isValuePresent(ABSENTVALUE_PATH), "value should be absent by default");
        String targetValue = String.valueOf(true);
        System.setProperty(ABSENTVALUE_PATH.substring(1), targetValue);
        Assert.assertTrue(jsonSettingsFile.isValuePresent(ABSENTVALUE_PATH), "value should be present after set");
        assertEquals(jsonSettingsFile.getValue(ABSENTVALUE_PATH), targetValue, "value passed via env var is not used by SettingsFile");
    }

    @Test
    public void testShouldBePossibleToGetDefaultContent() {
        System.clearProperty(PROFILE_KEY);
        CustomAqualityServices.initInjector(getTestModule());
        jsonSettingsFile = CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class);
        String language = jsonSettingsFile.getValue("/logger/language").toString();
        assertEquals(language, "en", "Logger language in default settings file should be read correctly");
    }

    @Test
    public void testShouldBePossibleToGetValue() {
        String languagePath = "/logger/language";
        String languageKey = "language";

        String language = jsonSettingsFile.getValue(languagePath).toString();
        assertEquals(language, EXPECTED_LANGUAGES.get(languageKey), String.format("Logger language in settings file '%s' should be read correctly", FILE_NAME));

        String newLang = "newLang";
        System.setProperty(LANGUAGE_ENV_KEY, newLang);
        language = jsonSettingsFile.getValue(languagePath).toString();
        assertEquals(language, newLang, String.format("Logger language in settings file '%s' should be overridden with environment variable", FILE_NAME));
    }

    @Test
    public void testShouldBePossibleToGetListOfValues() {
        String argumentsPath = "/arguments/start";
        List<String> expectedArguments = Arrays.asList("first", "second");

        List<String> arguments = jsonSettingsFile.getList(argumentsPath);
        assertNotNull(arguments);
        assertEquals(arguments, expectedArguments, String.format("List of values in settings file '%s' should be read correctly", FILE_NAME));

        expectedArguments = Arrays.asList("firstNew", "secondNew");
        String newArgs = "firstNew,secondNew";
        System.setProperty(ARGUMENTS_ENV_KEY, newArgs);
        arguments = jsonSettingsFile.getList(argumentsPath);
        assertNotNull(arguments);
        assertEquals(arguments, expectedArguments, String.format("Value in list in settings file '%s' be overridden with environment variable", FILE_NAME));
    }

    @Test
    public void testShouldBePossibleToGetMap() {
        String loggerPath = "/logger";

        Map<String, Object> languages = jsonSettingsFile.getMap(loggerPath);
        assertNotNull(languages);
        assertEquals(languages, EXPECTED_LANGUAGES, String.format("Map of values in settings file '%s' should be read correctly", FILE_NAME));

        String newLanguageValue = "newLangMap";
        Map<String, String> expectedLanguages = new HashMap<String, String>() {{
            put("language", newLanguageValue);
        }};
        System.setProperty(LANGUAGE_ENV_KEY, newLanguageValue);
        languages = jsonSettingsFile.getMap(loggerPath);
        assertNotNull(languages);
        assertEquals(languages, expectedLanguages, String.format("Map of values in settings file '%s' should be overridden with environment variable", FILE_NAME));
    }

    @Test
    public void testShouldBePossibleToCheckIsValuePresent() {
        boolean isTimeoutsPresent = jsonSettingsFile.isValuePresent(TIMEOUT_POLLING_INTERVAL_PATH);
        assertTrue(isTimeoutsPresent, String.format("%s value should be present in settings file '%s'", TIMEOUT_POLLING_INTERVAL_PATH, FILE_NAME));

        String wrongPath = "/blabla";
        boolean isWrongPathPresent = jsonSettingsFile.isValuePresent(wrongPath);
        assertFalse(isWrongPathPresent, String.format("%s value should not be present in settings file '%s'", wrongPath, FILE_NAME));
    }

    @Test
    public void testShouldBePossibleToCheckThatNullValueIsPresent() {
        boolean isNullValuePresent = jsonSettingsFile.isValuePresent(NULLVALUE_PATH);
        assertTrue(isNullValuePresent, String.format("%s value should be present in settings file '%s'", NULLVALUE_PATH, FILE_NAME));
    }

    @DataProvider
    public Object[] actionsToGetValue() {
        List<Consumer<String>> actionsList = new ArrayList<>();
        actionsList.add(path -> jsonSettingsFile.getValue(path));
        actionsList.add(path -> jsonSettingsFile.getMap(path));
        actionsList.add(path -> jsonSettingsFile.getList(path));
        return actionsList.toArray();
    }

    @Test(dataProvider = "actionsToGetValue")
    public void testShouldThrowExceptionWhenValueNotFound(Consumer<String> action) {
        String wrongPath = "/blabla";
        Assert.assertFalse(jsonSettingsFile.isValuePresent(wrongPath), String.format("%s value should not be present in settings file '%s'", wrongPath, FILE_NAME));
        Assert.assertThrows(IllegalArgumentException.class, () -> action.accept(wrongPath));
    }

    @AfterMethod
    public void after() {
        System.clearProperty(LANGUAGE_ENV_KEY);
        System.clearProperty(TIMEOUT_POLLING_INTERVAL_KEY);
        System.clearProperty(ARGUMENTS_ENV_KEY);
        CustomAqualityServices.initInjector(new AqualityModule<>(CustomAqualityServices::getApplication));
    }

    private TestModule getTestModule() {
        return new TestModule(CustomAqualityServices::getApplication);
    }
}

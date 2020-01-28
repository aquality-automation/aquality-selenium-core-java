package tests.utilities;

import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.TestModule;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class SettingsFileTests {
    private static final String TIMEOUT_POLLING_INTERVAL_PATH = "/timeouts/timeoutPollingInterval";
    private static final String TIMEOUT_POLLING_INTERVAL_KEY = "timeouts.timeoutPollingInterval";
    private static final String LANGUAGE_ENV_KEY = "logger.language";
    private static final String ARGUMENTS_ENV_KEY = "arguments.start";
    private static final String PROFILE = "jsontest";
    private static final String PROFILE_KEY = "profile";
    private static final String FILE_NAME = String.format("settings.%s.json", PROFILE);
    private ISettingsFile jsonSettingsFile;
    private static final Map EXPECTED_LANGUAGES = new HashMap<String, String>() {{
        put("language", "ru");
    }};

    @BeforeMethod
    public void before() {
        System.setProperty(PROFILE_KEY, PROFILE);
        CustomAqualityServices.initInjector(new TestModule());
        jsonSettingsFile = CustomAqualityServices.getInjector().getInstance(ISettingsFile.class);
    }

    @Test
    public void testShouldBePossibleToGetDefaultContent(){
        System.clearProperty(PROFILE_KEY);
        CustomAqualityServices.initInjector(new TestModule());
        jsonSettingsFile = CustomAqualityServices.getInjector().getInstance(ISettingsFile.class);
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
        List expectedArguments = Arrays.asList("first", "second");

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

        Map languages = jsonSettingsFile.getMap(loggerPath);
        assertNotNull(languages);
        assertEquals(languages, EXPECTED_LANGUAGES, String.format("Map of values in settings file '%s' should be read correctly", FILE_NAME));

        String newLanguageValue = "newLangMap";
        Map expectedLanguages = new HashMap<String, String>() {{
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

    @AfterMethod
    public void after() {
        System.clearProperty(PROFILE_KEY);
        System.clearProperty(LANGUAGE_ENV_KEY);
        System.clearProperty(TIMEOUT_POLLING_INTERVAL_KEY);
        System.clearProperty(ARGUMENTS_ENV_KEY);
    }
}

package tests.utilities;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.TestModule;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.testng.Assert.*;

public class SettingsFileTests {
    private static final String timeoutPollingIntervalPath = "/timeouts/timeoutPollingInterval";
    private static final String timeoutPollingIntervalKey = "timeouts.timeoutPollingInterval";
    private static final String languageEnvKey = "logger.language";
    private static final String argumentsEnvKey = "arguments.start";
    private static final String profile = "jsontest";
    private static final String profileKey = "profile";
    private static final String fileName = String.format("settings.%s.json", profile);
    private static final String defaultFileName = "settings.json";
    private ISettingsFile jsonSettingsFile;
    private static final Map expectedLanguages = new HashMap<String, String>() {{
        put("language", "ru");
    }};

    @BeforeMethod
    public void before() {
        System.setProperty(profileKey, profile);
        jsonSettingsFile = CustomAqualityServices.getInjector().getInstance(ISettingsFile.class);
    }

    @Test
    public void testShouldBePossibleToGetDefaultContent() throws IOException {
        System.clearProperty(profileKey);
        CustomAqualityServices.initInjector(new TestModule());
        jsonSettingsFile = CustomAqualityServices.getInjector().getInstance(ISettingsFile.class);

        String expectedContent = Files.asCharSource(new File(getClass().getClassLoader().getResource(defaultFileName).getFile()), Charsets.UTF_8).read();
        String actualContent = jsonSettingsFile.getContent();
        assertEquals(actualContent, expectedContent, String.format("Settings file %s should be read correctly", defaultFileName));
    }

    @Test
    public void testShouldBePossibleToGetContent() throws IOException {
        String expectedContent = Files.asCharSource(new File(getClass().getClassLoader().getResource(fileName).getFile()), Charsets.UTF_8).read();
        String actualContent = jsonSettingsFile.getContent();
        assertEquals(actualContent, expectedContent, String.format("Settings file %s should be read correctly", fileName));
    }

    @Test
    public void testShouldBePossibleToGetValue() {
        String languagePath = "/logger/language";
        String languageKey = "language";

        String language = jsonSettingsFile.getValue(languagePath).toString();
        assertEquals(language, expectedLanguages.get(languageKey), String.format("Logger language in settings file '%s' should be read correctly", fileName));

        String newLang = "newLang";
        System.setProperty(languageEnvKey, newLang);
        language = jsonSettingsFile.getValue(languagePath).toString();
        assertEquals(language, newLang, String.format("Logger language in settings file '%s' should be overridden with environment variable", fileName));
    }

    @Test
    public void testShouldBePossibleToGetListOfValues() {
        String argumentsPath = "/arguments/start";
        List expectedArguments = Arrays.asList("first", "second");

        List<String> arguments = jsonSettingsFile.getList(argumentsPath);
        assertNotNull(arguments);
        assertEquals(arguments, expectedArguments, String.format("List of values in settings file '%s' should be read correctly", fileName));

        expectedArguments = Arrays.asList("firstNew", "secondNew");
        String newArgs = "firstNew,secondNew";
        System.setProperty(argumentsEnvKey, newArgs);
        arguments = jsonSettingsFile.getList(argumentsPath);
        assertNotNull(arguments);
        assertEquals(arguments, expectedArguments, String.format("Value in list in settings file '%s' be overridden with environment variable", fileName));
    }

    @Test
    public void testShouldBePossibleToGetMap() {
        String loggerPath = "/logger";

        Map languages = jsonSettingsFile.getMap(loggerPath);
        assertNotNull(languages);
        assertEquals(languages, expectedLanguages, String.format("Map of values in settings file '%s' should be read correctly", fileName));

        String newLanguageValue = "newLangMap";
        Map expectedLanguages = new HashMap<String, String>() {{
            put("language", newLanguageValue);
        }};
        System.setProperty(languageEnvKey, newLanguageValue);
        languages = jsonSettingsFile.getMap(loggerPath);
        assertNotNull(languages);
        assertEquals(languages, expectedLanguages, String.format("Map of values in settings file '%s' should be overridden with environment variable", fileName));
    }

    @Test
    public void testShouldBePossibleToCheckIsValuePresent() {
        boolean isTimeoutsPresent = jsonSettingsFile.isValuePresent(timeoutPollingIntervalPath);
        assertTrue(isTimeoutsPresent, String.format("%s value should be present in settings file '%s'", timeoutPollingIntervalPath, fileName));

        String wrongPath = "/blabla";
        boolean isWrongPathPresent = jsonSettingsFile.isValuePresent(wrongPath);
        assertFalse(isWrongPathPresent, String.format("%s value should not be present in settings file '%s'", wrongPath, fileName));
    }

    @AfterMethod
    public void after() {
        System.clearProperty(profileKey);
        System.clearProperty(languageEnvKey);
        System.clearProperty(timeoutPollingIntervalKey);
        System.clearProperty(argumentsEnvKey);
    }
}
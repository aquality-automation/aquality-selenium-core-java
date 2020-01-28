package tests.configurations;

import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.localization.SupportedLanguage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.TestModule;

import static org.testng.Assert.assertEquals;

public class ProfileConfigurationTests {

    private static final String profileKey = "profile";
    private static final String profile = "custom";

    @BeforeMethod
    public void before(){
        System.setProperty(profileKey, profile);
    }

    @Test
    public void testShouldBePossibleToGetLanguageFromNewSettingsFile() {
        SupportedLanguage language = CustomAqualityServices.getInjector().getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, SupportedLanguage.RU, String.format("Current language should be got from %s profile", profile));
    }

    @AfterMethod
    public void after(){
        System.clearProperty(profileKey);
    }
}
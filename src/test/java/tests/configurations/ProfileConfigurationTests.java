package tests.configurations;

import aquality.selenium.core.applications.AqualityModule;
import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.localization.SupportedLanguage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.assertEquals;

public class ProfileConfigurationTests extends BaseProfileTest {

    private static final String PROFILE = "custom";

    @BeforeMethod
    public void before() {
        System.setProperty(PROFILE_KEY, PROFILE);
        CustomAqualityServices.initInjector(new AqualityModule<>(CustomAqualityServices::getApplication));
    }

    @Test
    public void testShouldBePossibleToGetLanguageFromNewSettingsFile() {

        SupportedLanguage language = CustomAqualityServices.getServiceProvider().getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, SupportedLanguage.RU, String.format("Current language should be got from %s profile", PROFILE));
    }
}

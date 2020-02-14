package tests.configurations;

import aquality.selenium.core.applications.AqualityModule;
import aquality.selenium.core.configurations.LoggerConfiguration;
import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.applications.CustomAqualityServices;

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
        String language = new LoggerConfiguration(CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class)).getLanguage();
        assertEquals(language, "ru", String.format("Current language should be got from %s profile", PROFILE));
    }
}

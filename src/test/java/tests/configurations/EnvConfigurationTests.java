package tests.configurations;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.localization.SupportedLanguage;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.TestModule;

import static org.testng.Assert.assertEquals;

public class EnvConfigurationTests {

    private static final String LANGUAGE_KEY = "logger.language";

    @BeforeMethod
    public void before(){
        System.setProperty(LANGUAGE_KEY, "ru");
        initInjector();
    }

    @Test
    public void testShouldBePossibleToOverrideLanguageWithEnvVariable() {
        SupportedLanguage language = CustomAqualityServices.getServiceProvider().getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, SupportedLanguage.RU, "Current language should be overridden with env variable");
    }

    @AfterMethod
    public void after(){
        System.clearProperty(LANGUAGE_KEY);
        initInjector();
    }

    private void initInjector(){
        CustomAqualityServices.initInjector(new AqualityModule<>(CustomAqualityServices::getApplication));
    }
}

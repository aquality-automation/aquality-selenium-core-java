package tests.configurations;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.localization.SupportedLanguage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.TestModule;

import static org.testng.Assert.assertEquals;

public class EnvConfigurationTests {

    private static final String LANGUAGE_KEY = "logger.language";
    private static final String LANGUAGE_VALUE = "ru";
    private static final String CONDITION_TIMEOUT_KEY = "timeouts.timeoutCondition";
    private static final String NEW_TIMEOUT_VALUE = "10000";

    @BeforeMethod
    public void before(){
        System.setProperty(LANGUAGE_KEY, LANGUAGE_VALUE);
        System.setProperty(CONDITION_TIMEOUT_KEY, NEW_TIMEOUT_VALUE);
        CustomAqualityServices.initInjector(new TestModule(CustomAqualityServices::getApplication));
    }

    @Test
    public void testShouldBePossibleToOverrideLanguageWithEnvVariable() {
        SupportedLanguage language = CustomAqualityServices.getServiceProvider().getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, SupportedLanguage.RU, "Current language should be overridden with env variable");
    }

    @Test
    public void testShouldBePossibleToOverrideTimeoutWithEnvVariable() {
        long conditionTimeout = CustomAqualityServices.getServiceProvider().getInstance(ITimeoutConfiguration.class).getCondition();
        assertEquals(conditionTimeout, Long.parseLong(NEW_TIMEOUT_VALUE), "Condition timeout should be overridden with env variable");
    }

    @Test
    public void testNumberFormatExceptionShouldBeThrownIfTimeoutIsNotANumber() {
        System.setProperty(CONDITION_TIMEOUT_KEY, LANGUAGE_VALUE);
        try {
            CustomAqualityServices.initInjector(new TestModule(CustomAqualityServices::getApplication));
            CustomAqualityServices.getServiceProvider().getInstance(ITimeoutConfiguration.class).getCommand();
        } catch (Exception e) {
            Assert.assertSame(e.getCause().getClass(), NumberFormatException.class);
            Assert.assertTrue(e.getMessage().contains("NumberFormatException"));
        }
    }

    @AfterMethod
    public void after(){
        System.clearProperty(LANGUAGE_KEY);
        System.clearProperty(CONDITION_TIMEOUT_KEY);
        CustomAqualityServices.initInjector(new AqualityModule<>(CustomAqualityServices::getApplication));
    }
}

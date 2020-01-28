package tests.configurations;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.localization.SupportedLanguage;
import com.google.inject.Injector;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.TestModule;

import static org.testng.Assert.assertEquals;

public class EnvConfigurationTests {

    private static final String languageKey = "logger.language";
    private static final String languageValue = "ru";
    private static final String conditionTimeoutKey = "timeouts.timeoutCondition";
    private static final String newTimeoutValue = "10000";
    private Injector injector;

    @BeforeMethod
    public void before() {
        System.setProperty(languageKey, languageValue);
        System.setProperty(conditionTimeoutKey, newTimeoutValue);
        CustomAqualityServices.initInjector(new TestModule());
        injector = CustomAqualityServices.getInjector();
    }

    @Test
    public void testShouldBePossibleToOverrideLanguageWithEnvVariable() {
        SupportedLanguage language = injector.getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, SupportedLanguage.RU, "Current language should be overridden with env variable");
    }

    @Test
    public void testShouldBePossibleToOverrideTimeoutWithEnvVariable() {
        long conditionTimeout = injector.getInstance(ITimeoutConfiguration.class).getCondition();
        assertEquals(conditionTimeout, Long.parseLong(newTimeoutValue), "Condition timeout should be overridden with env variable");
    }

    @Test
    public void testNumberFormatExceptionShouldBeThrownIfTimeoutIsNotANumber() {
        System.setProperty(conditionTimeoutKey, languageValue);
        try {
            CustomAqualityServices.initInjector(new TestModule());
            CustomAqualityServices.getInjector().getInstance(ITimeoutConfiguration.class).getCommand();
        } catch (Exception e) {
            Assert.assertSame(e.getCause().getClass(), NumberFormatException.class);
            Assert.assertTrue(e.getMessage().contains("NumberFormatException"));
        }
    }

    @AfterMethod
    public void after() {
        System.clearProperty(languageKey);
        System.clearProperty(conditionTimeoutKey);
        CustomAqualityServices.initInjector(new AqualityModule());
    }
}
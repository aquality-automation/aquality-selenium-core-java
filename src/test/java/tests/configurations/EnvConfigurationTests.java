package tests.configurations;

import aquality.selenium.core.applications.AqualityModule;
import aquality.selenium.core.configurations.*;
import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.applications.CustomAqualityServices;
import tests.applications.TestModule;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class EnvConfigurationTests extends BaseProfileTest {

    private static final String LANGUAGE_KEY = "logger.language";
    private static final String ELEMENT_CACHE = "elementCache.isEnabled";
    private static final String NEW_BOOL_VALUE = "true";
    private static final String NEW_STRING_VALUE = "ru";
    private static final String DEFAULT_LANGUAGE = "en";
    private static final String CONDITION_TIMEOUT_KEY = "timeouts.timeoutCondition";
    private static final String NEW_INT_VALUE = "10000";
    private static final String RETRY_NUMBER_KEY = "retry.number";
    private ISettingsFile settingsFile;

    @BeforeMethod
    public void before() {
        System.setProperty(LANGUAGE_KEY, NEW_STRING_VALUE);
        System.setProperty(CONDITION_TIMEOUT_KEY, NEW_INT_VALUE);
        System.setProperty(RETRY_NUMBER_KEY, NEW_INT_VALUE);
        System.setProperty(ELEMENT_CACHE, NEW_BOOL_VALUE);
        CustomAqualityServices.initInjector(new TestModule(CustomAqualityServices::getApplication));
        settingsFile = CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class);
    }

    @Test
    public void testShouldBePossibleToOverrideLanguageWithEnvVariable() {
        String language = new LoggerConfiguration(settingsFile).getLanguage();
        assertEquals(language, "ru", "Current language should be overridden with env variable");
    }

    @Test
    public void testShouldBePossibleToOverrideTimeoutWithEnvVariable() {
        long conditionTimeout = new TimeoutConfiguration(settingsFile).getCondition().getSeconds();
        assertEquals(conditionTimeout, Long.parseLong(NEW_INT_VALUE), "Condition timeout should be overridden with env variable");
    }

    @Test
    public void testShouldBePossibleToOverrideRetryConfigurationWithEnvVariable() {
        int retryNumber = new RetryConfiguration(settingsFile).getNumber();
        assertEquals(retryNumber, Long.parseLong(NEW_INT_VALUE), "Number of retry attempts should be overridden with env variable");
    }

    @Test
    public void testShouldBePossibleToOverrideElementCacheStateWithEnvVariable() {
        boolean isEnabled = new ElementCacheConfiguration(settingsFile).isEnabled();
        assertTrue(isEnabled, "Element cache state should be overridden with env variable");
    }

    @Test
    public void testNumberFormatExceptionShouldBeThrownIfTimeoutIsNotANumber() {
        System.setProperty(CONDITION_TIMEOUT_KEY, NEW_STRING_VALUE);
        checkNumberFormatException(() -> CustomAqualityServices.getServiceProvider().getInstance(ITimeoutConfiguration.class).getCommand());
    }

    @Test
    public void testNumberFormatExceptionShouldBeThrownIfRetryConfigurationIsNotANumber() {
        System.setProperty(RETRY_NUMBER_KEY, NEW_STRING_VALUE);
        checkNumberFormatException(() -> CustomAqualityServices.getServiceProvider().getInstance(IRetryConfiguration.class).getNumber());
    }

    @Test
    public void testShouldGetDefaultLanguageIfConfigurationIsAbsent() {
        System.setProperty(PROFILE_KEY, "empty");
        System.clearProperty(LANGUAGE_KEY);
        CustomAqualityServices.initInjector(new TestModule(CustomAqualityServices::getApplication));
        String language = new LoggerConfiguration(settingsFile).getLanguage();
        assertEquals(language, DEFAULT_LANGUAGE, "Current language should be got from logger configuration");
    }

    @AfterMethod
    public void after() {
        System.clearProperty(LANGUAGE_KEY);
        System.clearProperty(CONDITION_TIMEOUT_KEY);
        System.clearProperty(RETRY_NUMBER_KEY);
        System.clearProperty(ELEMENT_CACHE);
        CustomAqualityServices.initInjector(new AqualityModule<>(CustomAqualityServices::getApplication));
    }

    private void checkNumberFormatException(Runnable getNumberAction) {
        try {
            CustomAqualityServices.initInjector(new TestModule(CustomAqualityServices::getApplication));
            getNumberAction.run();
        } catch (Exception e) {
            Assert.assertSame(e.getCause().getClass(), NumberFormatException.class);
        }
    }
}

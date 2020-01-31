package tests.configurations;

import aquality.selenium.core.configurations.*;
import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class ConfigurationTests {
    private static final ISettingsFile settingsFile = CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class);

    @Test
    public void testShouldBePossibleCheckIsEnableElementCache() {
        boolean isEnable = new ElementCacheConfiguration(settingsFile).isEnabled();
        assertFalse(isEnable, "Element cache is disabled by default");
    }

    @Test
    public void testShouldBePossibleToGetLanguage() {
        String language = new LoggerConfiguration(settingsFile).getLanguage();
        assertEquals(language, "en", "Current language should be got from logger configuration");
    }

    @Test
    public void testShouldBePossibleToGetRetryConfiguration() {
        RetryConfiguration retryConfiguration = new RetryConfiguration(settingsFile);
        assertEquals(retryConfiguration.getNumber(), 2, "Number of retry attempts timeout should be got");
        assertEquals(retryConfiguration.getPollingInterval(), 300, "Polling interval of retrier should be got");
    }

    @Test
    public void testShouldBePossibleToGetTimeoutConfiguration() {
        TimeoutConfiguration timeoutConfiguration = new TimeoutConfiguration(settingsFile);
        assertEquals(timeoutConfiguration.getCommand(), 60, "Command timeout should be got");
        assertEquals(timeoutConfiguration.getCondition(), 30, "Condition timeout should be got");
        assertEquals(timeoutConfiguration.getImplicit(), 0, "Implicit timeout should be got");
        assertEquals(timeoutConfiguration.getPollingInterval(), 300, "Polling interval should be got");
    }
}

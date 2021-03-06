package tests.configurations;

import aquality.selenium.core.configurations.*;
import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.annotations.Test;
import tests.applications.CustomAqualityServices;

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
        assertEquals(retryConfiguration.getPollingInterval().toMillis(), 300, "Polling interval of retrier should be got");
    }

    @Test
    public void testShouldBePossibleToGetTimeoutConfiguration() {
        TimeoutConfiguration timeoutConfiguration = new TimeoutConfiguration(settingsFile);
        assertEquals(timeoutConfiguration.getCommand().getSeconds(), 60, "Command timeout should be got");
        assertEquals(timeoutConfiguration.getCondition().getSeconds(), 30, "Condition timeout should be got");
        assertEquals(timeoutConfiguration.getImplicit().getSeconds(), 0, "Implicit timeout should be got");
        assertEquals(timeoutConfiguration.getPollingInterval().toMillis(), 300, "Polling interval should be got");
    }
}

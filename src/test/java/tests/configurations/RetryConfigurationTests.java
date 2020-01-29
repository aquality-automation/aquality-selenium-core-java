package tests.configurations;

import aquality.selenium.core.configurations.IRetryConfiguration;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.assertEquals;

public class RetryConfigurationTests {

    @Test
    public void testShouldBePossibleToGetRetryConfiguration() {
        IRetryConfiguration retryConfiguration = CustomAqualityServices.getServiceProvider().getInstance(IRetryConfiguration.class);
        assertEquals(retryConfiguration.getNumber(), 2, "Number of retry attempts timeout should be got");
        assertEquals(retryConfiguration.getPollingInterval(), 300, "Polling interval of retrier should be got");
    }
}

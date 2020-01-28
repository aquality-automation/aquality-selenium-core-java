package tests.configurations;

import aquality.selenium.core.configurations.ITimeoutConfiguration;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.assertEquals;

public class TimeoutConfigurationTests {

    @Test
    public void testShouldBePossibleToGetTimeoutConfiguration() {
        ITimeoutConfiguration timeoutConfiguration = CustomAqualityServices.getInjector().getInstance(ITimeoutConfiguration.class);
        assertEquals(timeoutConfiguration.getCommand(), 60, "Command timeout should be got");
        assertEquals(timeoutConfiguration.getCondition(), 30, "Condition timeout should be got");
        assertEquals(timeoutConfiguration.getImplicit(), 0, "Implicit timeout should be got");
        assertEquals(timeoutConfiguration.getPollingInterval(), 300, "Polling interval should be got");
    }
}

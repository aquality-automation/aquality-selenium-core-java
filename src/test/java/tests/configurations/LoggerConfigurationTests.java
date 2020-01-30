package tests.configurations;

import aquality.selenium.core.configurations.ILoggerConfiguration;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.assertEquals;

public class LoggerConfigurationTests {

    @Test
    public void testShouldBePossibleToGetLanguage() {
        String language = CustomAqualityServices.getServiceProvider().getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, "en", "Current language should be got from logger configuration");
    }
}

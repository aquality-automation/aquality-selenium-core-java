package tests.configurations;

import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.localization.SupportedLanguage;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import static org.testng.Assert.assertEquals;

public class LoggerConfigurationTests {

    @Test
    public void testShouldBePossibleToGetLanguage() {
        SupportedLanguage language = CustomAqualityServices.getServiceProvider().getInstance(ILoggerConfiguration.class).getLanguage();
        assertEquals(language, SupportedLanguage.EN, "Current language should be got from logger configuration");
    }
}

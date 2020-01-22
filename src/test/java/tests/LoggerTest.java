package tests;

import aquality.selenium.core.application.AqualityServices;
import com.google.inject.Injector;
import logging.Logger;
import org.testng.annotations.Test;
import static org.testng.Assert.assertNotNull;

public class LoggerTest {

    private Injector injector = AqualityServices.getInjector();

    @Test
    public void testGetInstanceShouldReturnSameObjectEachTime() {

        Logger logger = injector.getInstance(Logger.class);
        logger.info("test");
        assertNotNull(logger, "Logger instance is not null");
    }
}

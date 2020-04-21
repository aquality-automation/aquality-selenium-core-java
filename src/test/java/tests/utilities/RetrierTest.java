package tests.utilities;

import aquality.selenium.core.configurations.IRetryConfiguration;
import aquality.selenium.core.logging.Logger;
import org.openqa.selenium.InvalidArgumentException;
import tests.applications.browser.AqualityServices;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public abstract class RetrierTest {

    protected static final IRetryConfiguration RETRY_CONFIGURATION = AqualityServices.getServiceProvider().getInstance(IRetryConfiguration.class);
    protected static final Logger LOGGER = AqualityServices.getServiceProvider().getInstance(Logger.class);
    protected static final int RETRIES_COUNT = RETRY_CONFIGURATION.getNumber();
    protected static final long POLLING_INTERVAL = RETRY_CONFIGURATION.getPollingInterval().toMillis();
    protected static final long ACCURACY = 100;

    protected void checkRetrierShouldWorkOnceIfMethodSucceeded(Runnable retryFunction) {
        Date startTime = new Date();
        retryFunction.run();
        long duration = new Date().getTime() - startTime.getTime();
        assertTrue(duration < POLLING_INTERVAL,
                String.format("Duration '%s' should be less that pollingInterval '%s'", duration, POLLING_INTERVAL));
    }

    protected void checkRetrierShouldWaitPollingTimeBetweenMethodsCall(Runnable retryFunction) {
        Date startTime = new Date();
        retryFunction.run();
        long duration = new Date().getTime() - startTime.getTime();
        long doubledAccuracyPollingInterval = 2 * POLLING_INTERVAL + ACCURACY;
        assertTrue(duration >= POLLING_INTERVAL, String.format("duration '%s' should be more than polling interval '%s'", duration, POLLING_INTERVAL));
        assertTrue(duration <= doubledAccuracyPollingInterval,
                String.format("duration '%s' should be less than doubled polling interval '%s'", duration, doubledAccuracyPollingInterval));
    }

    protected void throwInvalidArgumentException() {
        throw new InvalidArgumentException("");
    }

    protected void checkRetrierShouldWorkCorrectTimes(RuntimeException handledException, AtomicInteger actualAttempts, Runnable retryFunction) {
        try {
            retryFunction.run();
        } catch (RuntimeException e) {
            assertTrue(handledException.getClass().isInstance(e));
        }
        assertEquals(actualAttempts.get(), RETRIES_COUNT + 1, "actual attempts count is not match to expected");
    }
}

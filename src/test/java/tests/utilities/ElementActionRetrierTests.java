package tests.utilities;

import aquality.selenium.core.configurations.IRetryConfiguration;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.ElementActionRetrier;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.applications.browser.AqualityServices;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ElementActionRetrierTests {

    private static final IRetryConfiguration RETRY_CONFIGURATION = AqualityServices.getServiceProvider().getInstance(IRetryConfiguration.class);
    private static final Logger LOGGER = AqualityServices.getServiceProvider().getInstance(Logger.class);
    private static final int RETRIES_COUNT = RETRY_CONFIGURATION.getNumber();
    private static final long POLLING_INTERVAL = RETRY_CONFIGURATION.getPollingInterval().toMillis();
    private static final ElementActionRetrier ELEMENT_ACTION_RETRIER = new ElementActionRetrier(RETRY_CONFIGURATION);
    private static final long ACCURACY = 100;

    @DataProvider
    private Object[][] handledExceptions() {
        return new Object[][]{
                {new StaleElementReferenceException("")},
                {new InvalidElementStateException("")}
        };
    }

    @Test
    public void testRetrierShouldWorkOnceIfMethodSucceeded() {
        checkRetrierShouldWorkOnceIfMethodSucceeded(() -> ELEMENT_ACTION_RETRIER.doWithRetry(() -> ""));
    }

    @Test
    public void testRetrierShouldWorkOnceIfMethodSucceededWithReturnValue() {
        checkRetrierShouldWorkOnceIfMethodSucceeded(() -> ELEMENT_ACTION_RETRIER.doWithRetry(() -> true));
    }

    private void checkRetrierShouldWorkOnceIfMethodSucceeded(Runnable retryFunction) {
        Date startTime = new Date();
        retryFunction.run();
        long duration = new Date().getTime() - startTime.getTime();
        assertTrue(duration < POLLING_INTERVAL,
                String.format("Duration '%s' should be less that pollingInterval '%s'", duration, POLLING_INTERVAL));
    }

    @Test(dataProvider = "handledExceptions")
    public void testRetrierShouldWaitPollingTimeBetweenMethodsCall(RuntimeException handledException) {
        AtomicBoolean isThrowException = new AtomicBoolean(true);
        checkRetrierShouldWaitPollingTimeBetweenMethodsCall(() ->
                ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
                    if (isThrowException.get()) {
                        isThrowException.set(false);
                        throw handledException;
                    }
                }));
    }

    @Test(dataProvider = "handledExceptions")
    public void testRetrierShouldWaitPollingTimeBetweenMethodsCallWithReturnValue(RuntimeException handledException) {
        AtomicBoolean isThrowException = new AtomicBoolean(true);
        checkRetrierShouldWaitPollingTimeBetweenMethodsCall(() ->
                ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
                    if (isThrowException.get()) {
                        isThrowException.set(false);
                        throw handledException;
                    }

                    return new Object();
                }));
    }

    private void checkRetrierShouldWaitPollingTimeBetweenMethodsCall(Runnable retryFunction) {
        Date startTime = new Date();
        retryFunction.run();
        long duration = new Date().getTime() - startTime.getTime();
        long doubledAccuracyPollingInterval = 2 * POLLING_INTERVAL + ACCURACY;
        assertTrue(duration >= POLLING_INTERVAL, String.format("duration '%s' should be more than polling interval '%s'", duration, POLLING_INTERVAL));
        assertTrue(duration <= doubledAccuracyPollingInterval,
                String.format("duration '%s' should be less than doubled polling interval '%s'", duration, doubledAccuracyPollingInterval));
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testRetrierShouldThrowUnhandledException() {
        ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
            throwInvalidArgumentException();
            return new Object();
        });
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testRetrierShouldThrowUnhandledException1() {
        ELEMENT_ACTION_RETRIER.doWithRetry(this::throwInvalidArgumentException);
    }

    private void throwInvalidArgumentException() {
        throw new InvalidArgumentException("");
    }

    @Test(dataProvider = "handledExceptions")
    public void testRetrierShouldWorkCorrectTimes(RuntimeException handledException) {
        AtomicInteger actualAttempts = new AtomicInteger(0);
        checkRetrierShouldWorkCorrectTimes(handledException, actualAttempts, () ->
                ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
                    LOGGER.info("current attempt is " + actualAttempts.incrementAndGet());
                    throw handledException;
                }));
    }

    @Test(dataProvider = "handledExceptions")
    public void testRetrierShouldWorkCorrectTimesWithReturnValue(RuntimeException handledException) {
        AtomicInteger actualAttempts = new AtomicInteger(0);
        checkRetrierShouldWorkCorrectTimes(handledException, actualAttempts, () ->
                ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
                    LOGGER.info("current attempt is " + actualAttempts.incrementAndGet());
                    throw handledException;
                }));
    }

    private void checkRetrierShouldWorkCorrectTimes(RuntimeException handledException, AtomicInteger actualAttempts, Runnable retryFunction) {
        try {
            retryFunction.run();
        } catch (RuntimeException e) {
            assertTrue(handledException.getClass().isInstance(e));
        }
        assertEquals(actualAttempts.get(), RETRIES_COUNT + 1, "actual attempts count is not match to expected");
    }

    @Test
    public void testRetrierShouldReturnValue() {
        Object obj = new Object();
        assertEquals(ELEMENT_ACTION_RETRIER.doWithRetry(() -> obj), obj, "Retrier should return value");
    }

    @Test(dataProvider = "handledExceptions", timeOut = 10000)
    public void testRetrierShouldNotThrowExceptionOnInterruption(RuntimeException handledException) {
        AtomicBoolean isRetrierPaused = new AtomicBoolean(false);
        Thread thread = new Thread(() -> ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
            isRetrierPaused.set(true);
            throw handledException;
        }));
        thread.start();
        try {
            while (!isRetrierPaused.get()) {
                Thread.sleep(POLLING_INTERVAL / 10);
            }
            Thread.sleep(POLLING_INTERVAL / 3);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            Assert.fail("Retrier should handle InterruptedException");
        }
        thread.interrupt();
    }
}

package tests.utilities;

import aquality.selenium.core.utilities.ElementActionRetrier;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;

public class ElementActionRetrierTests extends RetrierTest{

    private static final ElementActionRetrier ELEMENT_ACTION_RETRIER = new ElementActionRetrier(RETRY_CONFIGURATION);

    @DataProvider
    private Object[][] handledExceptions() {
        return new Object[][]{
                {new StaleElementReferenceException("")},
                {new InvalidElementStateException("")}
        };
    }

    @Test
    public void testElementRetrierShouldWorkOnceIfMethodSucceeded() {
        checkRetrierShouldWorkOnceIfMethodSucceeded(() -> ELEMENT_ACTION_RETRIER.doWithRetry(() -> ""));
    }

    @Test
    public void testElementRetrierShouldWorkOnceIfMethodSucceededWithReturnValue() {
        checkRetrierShouldWorkOnceIfMethodSucceeded(() -> ELEMENT_ACTION_RETRIER.doWithRetry(() -> true));
    }

    @Test(dataProvider = "handledExceptions")
    public void testElementRetrierShouldWaitPollingTimeBetweenMethodsCall(RuntimeException handledException) {
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
    public void testElementRetrierShouldWaitPollingTimeBetweenMethodsCallWithReturnValue(RuntimeException handledException) {
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

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testElementRetrierShouldThrowUnhandledException() {
        ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
            throwInvalidArgumentException();
            return new Object();
        });
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testElementRetrierShouldThrowUnhandledException1() {
        ELEMENT_ACTION_RETRIER.doWithRetry(this::throwInvalidArgumentException);
    }

    @Test(dataProvider = "handledExceptions")
    public void testElementRetrierShouldWorkCorrectTimes(RuntimeException handledException) {
        AtomicInteger actualAttempts = new AtomicInteger(0);
        checkRetrierShouldWorkCorrectTimes(handledException, actualAttempts, () ->
                ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
                    LOGGER.info("current attempt is " + actualAttempts.incrementAndGet());
                    throw handledException;
                }));
    }

    @Test(dataProvider = "handledExceptions")
    public void testElementRetrierShouldWorkCorrectTimesWithReturnValue(RuntimeException handledException) {
        AtomicInteger actualAttempts = new AtomicInteger(0);
        checkRetrierShouldWorkCorrectTimes(handledException, actualAttempts, () ->
                ELEMENT_ACTION_RETRIER.doWithRetry(() -> {
                    LOGGER.info("current attempt is " + actualAttempts.incrementAndGet());
                    throw handledException;
                }));
    }

    @Test
    public void testElementRetrierShouldReturnValue() {
        Object obj = new Object();
        assertEquals(ELEMENT_ACTION_RETRIER.doWithRetry(() -> obj), obj, "Retrier should return value");
    }

    @Test(dataProvider = "handledExceptions", timeOut = 10000)
    public void testElementRetrierShouldNotThrowExceptionOnInterruption(RuntimeException handledException) {
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

package tests.utilities;

import aquality.selenium.core.utilities.ActionRetrier;
import org.openqa.selenium.InvalidArgumentException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.testng.Assert.assertEquals;

public class ActionRetrierTests extends RetrierTest {

    private static final Collection<Class<? extends Throwable>> handledExceptions = Collections.singleton(IllegalStateException.class);
    private static final ActionRetrier ACTION_RETRIER = new ActionRetrier(RETRY_CONFIGURATION);

    @Test
    public void testRetrierShouldWorkWhenSupplierReturnsNull() {
        Assert.assertNull(ACTION_RETRIER.doWithRetry(() -> null, Collections.emptyList()));
    }

    @Test
    public void testRetrierShouldWorkOnceIfMethodSucceeded() {
        checkRetrierShouldWorkOnceIfMethodSucceeded(() -> ACTION_RETRIER.doWithRetry(() -> System.out.println(""), Collections.emptyList()));
    }

    @Test
    public void testRetrierShouldWorkOnceIfMethodSucceededWithReturnValue() {
        checkRetrierShouldWorkOnceIfMethodSucceeded(() -> ACTION_RETRIER.doWithRetry(() -> true, Collections.emptyList()));
    }

    @Test
    public void testRetrierShouldWaitPollingTimeBetweenMethodsCall() {
        AtomicBoolean isThrowException = new AtomicBoolean(true);
        checkRetrierShouldWaitPollingTimeBetweenMethodsCall(() ->
                ACTION_RETRIER.doWithRetry(() -> {
                    if (isThrowException.get()) {
                        isThrowException.set(false);
                        throw new IllegalStateException();
                    }
                }, handledExceptions));
    }

    @Test
    public void testRetrierShouldWaitPollingTimeBetweenMethodsCallWithReturnValue() {
        AtomicBoolean isThrowException = new AtomicBoolean(true);
        checkRetrierShouldWaitPollingTimeBetweenMethodsCall(() ->
                ACTION_RETRIER.doWithRetry(() -> {
                    if (isThrowException.get()) {
                        isThrowException.set(false);
                        throw new IllegalStateException();
                    }

                    return new Object();
                }, handledExceptions));
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testRetrierShouldThrowUnhandledException() {
        ACTION_RETRIER.doWithRetry(() -> {
            throwInvalidArgumentException();
            return new Object();
        }, Collections.emptyList());
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testRetrierShouldThrowUnhandledExceptionWithoutReturn() {
        ACTION_RETRIER.doWithRetry(this::throwInvalidArgumentException, Collections.emptyList());
    }

    @Test
    public void testRetrierShouldWorkCorrectTimes() {
        AtomicInteger actualAttempts = new AtomicInteger(0);
        checkRetrierShouldWorkCorrectTimes(new IllegalStateException(), actualAttempts, () ->
                ACTION_RETRIER.doWithRetry(() -> {
                    LOGGER.info("current attempt is " + actualAttempts.incrementAndGet());
                    throw new IllegalStateException();
                }, handledExceptions));
    }

    @Test
    public void testRetrierShouldWorkCorrectTimesWithReturnValue() {
        AtomicInteger actualAttempts = new AtomicInteger(0);
        checkRetrierShouldWorkCorrectTimes(new IllegalStateException(), actualAttempts, () ->
                ACTION_RETRIER.doWithRetry(() -> {
                    LOGGER.info("current attempt is " + actualAttempts.incrementAndGet());
                    throw new IllegalStateException();
                }, handledExceptions));
    }

    @Test
    public void testRetrierShouldReturnValue() {
        Object obj = new Object();
        assertEquals(ACTION_RETRIER.doWithRetry(() -> obj, Collections.emptyList()), obj, "Retrier should return value");
    }

    @Test
    public void testRetrierShouldNotThrowExceptionOnInterruption() {
        AtomicBoolean isRetrierPaused = new AtomicBoolean(false);
        Thread thread = new Thread(() -> ACTION_RETRIER.doWithRetry(() -> {
            isRetrierPaused.set(true);
            throw new IllegalStateException();
        }, handledExceptions));
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

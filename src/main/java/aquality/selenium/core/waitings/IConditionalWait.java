package aquality.selenium.core.waitings;

import org.openqa.selenium.support.ui.ExpectedCondition;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Utility used to wait for some condition.
 */
public interface IConditionalWait {

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition Condition with boolean result (predicate)
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition) {
        return waitFor(condition, null, null, null, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition Condition with boolean result (predicate)
     * @param message   Part of error message in case of Timeout exception
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, String message) {
        return waitFor(condition, null, null, message, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition          Condition with boolean result (predicate)
     * @param exceptionsToIgnore Exceptions to ignore
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        return waitFor(condition, null, null, null, exceptionsToIgnore);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition          Condition with boolean result (predicate)
     * @param message            Part of error message in case of Timeout exception
     * @param exceptionsToIgnore Exceptions to ignore
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        return waitFor(condition, null, null, message, exceptionsToIgnore);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition       Condition with boolean result (predicate)
     * @param timeout         Condition timeout
     * @param pollingInterval Condition check interval
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, Duration timeout, Duration pollingInterval) {
        return waitFor(condition, timeout, pollingInterval, null, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition       Condition with boolean result (predicate)
     * @param timeout         Condition timeout
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, Duration timeout) {
        return waitFor(condition, timeout, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition       Condition with boolean result (predicate)
     * @param timeout         Condition timeout
     * @param pollingInterval Condition check interval
     * @param message         Part of error message in case of Timeout exception
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, Duration timeout, Duration pollingInterval, String message) {
        return waitFor(condition, timeout, pollingInterval, message, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition          Condition with boolean result (predicate)
     * @param timeout            Condition timeout
     * @param pollingInterval    Condition check interval
     * @param exceptionsToIgnore Exceptions to ignore
     * @return true if the condition has been met during the timeout
     */
    default boolean waitFor(BooleanSupplier condition, Duration timeout, Duration pollingInterval, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        return waitFor(condition, timeout, pollingInterval, null, exceptionsToIgnore);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition          Condition with boolean result (predicate)
     * @param timeout            Condition timeout
     * @param pollingInterval    Condition check interval
     * @param message            Part of error message in case of Timeout exception
     * @param exceptionsToIgnore Exceptions to ignore
     * @return true if the condition has been met during the timeout
     */
    boolean waitFor(BooleanSupplier condition, Duration timeout, Duration pollingInterval, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore);

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition Condition with boolean result (predicate)
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition) throws TimeoutException {
        waitForTrue(condition, null, null, null, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition Condition with boolean result (predicate)
     * @param message   Part of error message in case of Timeout exception
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, String message) throws TimeoutException {
        waitForTrue(condition, null, null, message, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition          Condition with boolean result (predicate)
     * @param exceptionsToIgnore Exceptions to ignore
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, Collection<Class<? extends Throwable>> exceptionsToIgnore) throws TimeoutException {
        waitForTrue(condition, null, null, null, exceptionsToIgnore);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     * Default values for timeouts used from configuration settings file
     *
     * @param condition          Condition with boolean result (predicate)
     * @param message            Part of error message in case of Timeout exception
     * @param exceptionsToIgnore Exceptions to ignore
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) throws TimeoutException {
        waitForTrue(condition, null, null, message, exceptionsToIgnore);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition       Condition with boolean result (predicate)
     * @param timeout         Condition timeout
     * @param pollingInterval Condition check interval
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, Duration timeout, Duration pollingInterval) throws TimeoutException {
        waitForTrue(condition, timeout, pollingInterval, null, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition       Condition with boolean result (predicate)
     * @param timeout         Condition timeout
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, Duration timeout) throws TimeoutException {
        waitForTrue(condition, timeout, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition       Condition with boolean result (predicate)
     * @param timeout         Condition timeout
     * @param pollingInterval Condition check interval
     * @param message         Part of error message in case of Timeout exception
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, Duration timeout, Duration pollingInterval, String message) throws TimeoutException {
        waitForTrue(condition, timeout, pollingInterval, message, null);
    }

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition          Condition with boolean result (predicate)
     * @param timeout            Condition timeout
     * @param pollingInterval    Condition check interval
     * @param exceptionsToIgnore Exceptions to ignore
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    default void waitForTrue(BooleanSupplier condition, Duration timeout, Duration pollingInterval, Collection<Class<? extends Throwable>> exceptionsToIgnore) throws TimeoutException {
        waitForTrue(condition, timeout, pollingInterval, null, exceptionsToIgnore);
    }


    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition          Condition with boolean result (predicate)
     * @param timeout            Condition timeout
     * @param pollingInterval    Condition check interval
     * @param message            Part of error message in case of Timeout exception
     * @param exceptionsToIgnore Exceptions to ignore
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    void waitForTrue(BooleanSupplier condition, Duration timeout, Duration pollingInterval, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) throws TimeoutException;

    /**
     * Waits for function will be true or return some except false.
     * Default timeout condition from settings is using.
     * StaleElementReferenceException will be handled by default
     *
     * @param condition Function for waiting {@link Function}
     * @param <T>       Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occurred
     */
    default <T> T waitFor(ExpectedCondition<T> condition) {
        return waitFor(condition, null, null, null, null);
    }

    /**
     * Waits for function will be true or return some except false.
     * Default timeout condition from settings is using.
     * StaleElementReferenceException will be handled by default
     *
     * @param condition Function for waiting {@link Function}
     * @param message   the message that will be added to an error in case if the condition is not matched during the timeout
     * @param <T>       Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occurred
     */
    default <T> T waitFor(ExpectedCondition<T> condition, String message) {
        return waitFor(condition, null, null, message, null);
    }

    /**
     * Waits for function will be true or return some except false.
     * Default timeout condition from settings is using.
     *
     * @param condition          Function for waiting {@link Function}
     * @param exceptionsToIgnore list of exceptions that should be ignored during waiting
     * @param <T>                Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occurred
     */
    default <T> T waitFor(ExpectedCondition<T> condition, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        return waitFor(condition, null, null, null, exceptionsToIgnore);
    }

    /**
     * Waits for function will be true or return some except false.
     * Default timeout condition from settings is using.
     *
     * @param condition          Function for waiting {@link Function}
     * @param message            the message that will be added to an error in case if the condition is not matched during the timeout
     * @param exceptionsToIgnore list of exceptions that should be ignored during waiting
     * @param <T>                Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occurred
     */
    default <T> T waitFor(ExpectedCondition<T> condition, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        return waitFor(condition, null, null, message, exceptionsToIgnore);
    }

    /**
     * Waits for function will be true or return some except false.
     * StaleElementReferenceException will be handled by default
     *
     * @param condition        Function for waiting {@link Function}
     * @param timeout Duration before the time-out
     * @param pollingInterval  interval between checks whether condition match
     * @param <T>              Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occured
     */
    default <T> T waitFor(ExpectedCondition<T> condition, Duration timeout, Duration pollingInterval) {
        return waitFor(condition, timeout, pollingInterval, null, null);
    }

    /**
     * Waits for function will be true or return some except false.
     * StaleElementReferenceException will be handled by default
     *
     * @param condition        Function for waiting {@link Function}
     * @param timeout Duration before the time-out
     * @param <T>              Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occured
     */
    default <T> T waitFor(ExpectedCondition<T> condition, Duration timeout) {
        return waitFor(condition, timeout, null);
    }

    /**
     * Waits for function will be true or return some except false.
     * StaleElementReferenceException will be handled by default
     *
     * @param condition       Function for waiting {@link Function}
     * @param timeout         Duration before the time-out
     * @param pollingInterval interval between checks whether condition match
     * @param message         the message that will be added to an error in case if the condition is not matched during the timeout
     * @param <T>             Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occured
     */
    default <T> T waitFor(ExpectedCondition<T> condition, Duration timeout, Duration pollingInterval, String message) {
        return waitFor(condition, timeout, pollingInterval, message, null);
    }

    /**
     * Waits for function will be true or return some except false.
     *
     * @param condition          Function for waiting {@link Function}
     * @param timeout            Duration before the time-out
     * @param pollingInterval    interval between checks whether condition match
     * @param exceptionsToIgnore list of exceptions that should be ignored during waiting
     * @param <T>                Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occured
     */
    default <T> T waitFor(ExpectedCondition<T> condition, Duration timeout, Duration pollingInterval, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        return waitFor(condition, timeout, pollingInterval, null, exceptionsToIgnore);
    }

    /**
     * Waits for function will be true or return some except false.
     *
     * @param condition          Function for waiting {@link Function}
     * @param timeout            Duration before the time-out
     * @param pollingInterval    interval between checks whether condition match
     * @param message            the message that will be added to an error in case if the condition is not matched during the timeout
     * @param exceptionsToIgnore list of exceptions that should be ignored during waiting
     * @param <T>                Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occured
     */
    <T> T waitFor(ExpectedCondition<T> condition, Duration timeout, Duration pollingInterval, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore);
}

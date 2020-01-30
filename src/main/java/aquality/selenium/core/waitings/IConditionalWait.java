package aquality.selenium.core.waitings;

import org.openqa.selenium.support.ui.ExpectedCondition;

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
     * @param condition condition with boolean result (predicate)
     * @param message   Part of error message in case of Timeout exception
     * @return true if the condition has been met during the timeout
     */
    boolean waitForTrue(BooleanSupplier condition, String message);

    /**
     * Wait for some condition within timeout. Method does not use WebDriverWait
     *
     * @param condition                     condition with boolean result (predicate)
     * @param timeoutInSeconds              Condition timeout
     * @param pollingIntervalInMilliseconds Condition check interval
     * @param message                       Part of error message in case of Timeout exception
     * @throws TimeoutException will be thrown in case if timeout is over but condition was not met
     */
    void waitForTrue(BooleanSupplier condition, long timeoutInSeconds, long pollingIntervalInMilliseconds, String message) throws TimeoutException;

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
    <T> T waitFor(ExpectedCondition<T> condition, String message);

    /**
     * Waits for function will be true or return some except false.
     *
     * @param condition                     Function for waiting {@link Function}
     * @param timeOutInSeconds              Time-out in seconds
     * @param pollingIntervalInMilliseconds interval in milliseconds between checks whether condition match
     * @param message                       the message that will be added to an error in case if the condition is not matched during the timeout
     * @param exceptionsToIgnore            list of exceptions that should be ignored during waiting
     * @param <T>                           Type of object which is waiting
     * @return Object which waiting for or null - is exceptions occured
     */
    <T> T waitFor(ExpectedCondition<T> condition, long timeOutInSeconds, long pollingIntervalInMilliseconds, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore);
}

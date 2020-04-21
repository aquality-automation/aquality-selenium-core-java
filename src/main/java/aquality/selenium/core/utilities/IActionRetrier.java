package aquality.selenium.core.utilities;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Retries an action or function when handledExceptions occurs.
 */
public interface IActionRetrier {

    /**
     * Retries the action when the handled exception occurs.
     * @param runnable Action to be applied.
     * @param handledExceptions Exceptions to be handled.
     */
    void doWithRetry(Runnable runnable, Collection<Class<? extends Throwable>> handledExceptions);

    /**
     * Retries the function when the handled exception occurs.
     * @param function Function to be applied.
     * @param handledExceptions Exceptions to be handled.
     * @param <T> Return type of function.
     * @return Result of the function.
     */
    <T> T doWithRetry(Supplier<T> function, Collection<Class<? extends Throwable>> handledExceptions);
}

package aquality.selenium.core.utilities;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Retries an action or function when {@link #getHandledExceptions()} occurs.
 */
public interface IActionRetrier {

    /**
     * Retries the action when the handled exception {@link #getHandledExceptions()} occurs.
     * @param runnable Action to be applied.
     */
    void doWithRetry(Runnable runnable, Collection<Class<? extends Throwable>> handledExceptions);

    /**
     * Retries the function when the handled exception {@link #getHandledExceptions()} occurs.
     * @param function Function to be applied.
     * @param <T> Return type of function.
     * @return Result of the function.
     */
    <T> T doWithRetry(Supplier<T> function, Collection<Class<? extends Throwable>> handledExceptions);
}

package aquality.selenium.core.utilities;

import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.StaleElementReferenceException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * It was renamed to IActionRetrier.
 */
public interface IElementActionRetrier extends IActionRetrier {


    /**
     * Retries the action when the handled exception {@link #getHandledExceptions()} occurs.
     * @param runnable Action to be applied.
     */
    void doWithRetry(Runnable runnable);

    /**
     * Retries the function when the handled exception {@link #getHandledExceptions()} occurs.
     * @param function Function to be applied.
     * @param <T> Return type of function.
     * @return Result of the function.
     */
    <T> T doWithRetry(Supplier<T> function);

    /**
     * Exceptions to be ignored during action retrying.
     * @return By the default implementation, {@link StaleElementReferenceException} and {@link InvalidElementStateException} are handled.
     */
    default List<Class<? extends Throwable>> getHandledExceptions() {
        return Arrays.asList(StaleElementReferenceException.class, InvalidElementStateException.class);
    }
}

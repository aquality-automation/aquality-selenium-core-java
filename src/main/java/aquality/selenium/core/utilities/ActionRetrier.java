package aquality.selenium.core.utilities;

import aquality.selenium.core.configurations.IRetryConfiguration;
import com.google.inject.Inject;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

public class ActionRetrier implements IActionRetrier {

    private IRetryConfiguration retryConfiguration;

    @Inject
    public ActionRetrier(IRetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
    }

    @Override
    public void doWithRetry(Runnable runnable, Collection<Class<? extends Throwable>> handledExceptions) {
        Supplier<?> supplier = () -> {
            runnable.run();
            return true;
        };
        doWithRetry(supplier, handledExceptions);
    }

    @Override
    public <T> T doWithRetry(Supplier<T> function, Collection<Class<? extends Throwable>> handledExceptions) {
        int retryAttemptsLeft = retryConfiguration.getNumber();
        T result = null;
        while (retryAttemptsLeft >= 0) {
            try {
                result = function.get();
                break;
            } catch (Exception exception) {
                if (isExceptionHandled(handledExceptions, exception) && retryAttemptsLeft != 0) {
                    try {
                        Thread.sleep(retryConfiguration.getPollingInterval().toMillis());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    retryAttemptsLeft--;
                } else {
                    throw exception;
                }
            }
        }
        return result;
    }

    protected boolean isExceptionHandled(Collection<Class<? extends Throwable>> handledExceptions, Exception exception) {
        return handledExceptions.contains(exception.getClass());
    }
}

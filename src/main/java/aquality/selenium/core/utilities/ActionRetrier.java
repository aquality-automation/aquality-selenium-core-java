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
        Optional<T> result = Optional.empty();
        while (retryAttemptsLeft >= 0) {
            try {
                result = Optional.of(function.get());
                break;
            } catch (Exception exception) {
                if (handledExceptions.contains(exception.getClass()) && retryAttemptsLeft != 0) {
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
        return result.orElse(null);
    }
}

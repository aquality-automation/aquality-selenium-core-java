package aquality.selenium.core.utilities;

import aquality.selenium.core.configurations.IRetryConfiguration;
import com.google.inject.Inject;

import java.util.Optional;
import java.util.function.Supplier;

public class ElementActionRetrier implements IElementActionRetrier {

    private IRetryConfiguration retryConfiguration;

    @Inject
    public ElementActionRetrier(IRetryConfiguration retryConfiguration) {
        this.retryConfiguration = retryConfiguration;
    }

    @Override
    public void doWithRetry(Runnable runnable) {
        Supplier<?> supplier = () -> {
            runnable.run();
            return true;
        };
        doWithRetry(supplier);
    }

    @Override
    public <T> T doWithRetry(Supplier<T> function) {
        int retryAttemptsLeft = retryConfiguration.getNumber();
        Optional<T> result = Optional.empty();
        while (retryAttemptsLeft >= 0) {
            try {
                result = Optional.of(function.get());
                break;
            } catch (Exception exception) {
                if (isExceptionHandled(exception) && retryAttemptsLeft != 0) {
                    try {
                        Thread.sleep(retryConfiguration.getPollingInterval());
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

    protected boolean isExceptionHandled(Exception exception) {
        return getHandledExceptions().contains(exception.getClass());
    }
}

package aquality.selenium.core.utilities;

import aquality.selenium.core.configurations.IRetryConfiguration;
import com.google.inject.Inject;

import java.util.function.Supplier;

public class ElementActionRetrier extends ActionRetrier implements IElementActionRetrier {

    @Inject
    public ElementActionRetrier(IRetryConfiguration retryConfiguration) {
        super(retryConfiguration);
    }

    @Override
    public void doWithRetry(Runnable runnable) {
        doWithRetry(runnable, getHandledExceptions());
    }

    @Override
    public <T> T doWithRetry(Supplier<T> function) {
        return doWithRetry(function, getHandledExceptions());
    }
}

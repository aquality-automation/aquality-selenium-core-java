package aquality.selenium.core.utilities;

import aquality.selenium.core.configurations.IRetryConfiguration;
import com.google.inject.Inject;

/**
 * It was renamed to ActionRetrier.
 */
@Deprecated
public class ElementActionRetrier extends ActionRetrier implements IElementActionRetrier {

    @Inject
    public ElementActionRetrier(IRetryConfiguration retryConfiguration) {
        super(retryConfiguration);
    }
}

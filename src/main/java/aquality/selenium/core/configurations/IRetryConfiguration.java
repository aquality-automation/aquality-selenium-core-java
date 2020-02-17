package aquality.selenium.core.configurations;

import java.time.Duration;

/**
 * Describes retry configuration.
 */
public interface IRetryConfiguration {

    /**
     * Gets the number of attempts during retry.
     *
     * @return Number of retry attempts.
     */
    int getNumber();

    /**
     * Gets the polling interval used in retry.
     *
     * @return Polling interval for retry.
     */
    Duration getPollingInterval();
}

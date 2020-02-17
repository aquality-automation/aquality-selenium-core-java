package aquality.selenium.core.configurations;

import java.time.Duration;

/**
 * Provides timeouts configuration.
 */
public interface ITimeoutConfiguration {

    /**
     * Gets WedDriver ImplicitWait timeout.
     *
     * @return ImplicitWait timeout.
     */
    Duration getImplicit();

    /**
     * Gets default ConditionalWait timeout.
     *
     * @return ConditionalWait timeout.
     */
    Duration getCondition();

    /**
     * Gets ConditionalWait polling interval.
     *
     * @return polling interval.
     */
    Duration getPollingInterval();

    /**
     * Gets Command timeout.
     *
     * @return Command timeout.
     */
    Duration getCommand();
}

package aquality.selenium.core.configurations;

/**
 * Provides timeouts configuration.
 */
public interface ITimeoutConfiguration {

    /**
     * Gets WedDriver ImplicitWait timeout.
     *
     * @return ImplicitWait timeout.
     */
    long getImplicit();

    /**
     * Gets default ConditionalWait timeout.
     *
     * @return ConditionalWait timeout.
     */
    long getCondition();

    /**
     * Gets ConditionalWait polling interval.
     *
     * @return polling interval.
     */
    long getPollingInterval();

    /**
     * Gets Command timeout.
     *
     * @return Command timeout.
     */
    long getCommand();
}

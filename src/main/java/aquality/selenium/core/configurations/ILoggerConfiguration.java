package aquality.selenium.core.configurations;

/**
 * Describes logger configuration.
 */
public interface ILoggerConfiguration {

    /**
     * Gets language used inside the library for logging.
     * @return language used for logging.
     */
    String getLanguage();

    /**
     * Perform page source logging in case of catastrophic failures or not.
     * @return true if enabled, false otherwise.
     */
    boolean logPageSource();
}

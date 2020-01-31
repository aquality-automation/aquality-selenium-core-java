package aquality.selenium.core.localization;

/**
 * Log messages in current language.
 */
public interface ILocalizedLogger {
    /**
     * Logs localized message for action with INFO level which is applied for element, for example, click, send keys etc.
     * @param elementType Type of the element.
     * @param elementName Name of the element.
     * @param messageKey Key in resource file.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void infoElementAction(String elementType, String elementName, String messageKey, Object... args);

    /**
     * Logs localized message with INFO level.
     * @param messageKey Key in resource file.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void info(String messageKey, Object... args);

    /**
     * Logs localized message with DEBUG level.
     * @param messageKey Key in resource file.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void debug(String messageKey, Object... args);

    /**
     * Logs localized message with DEBUG level.
     * @param messageKey Key in resource file.
     * @param throwable Throwable to log.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void debug(String messageKey, Throwable throwable, Object... args);

    /**
     * Logs localized message with WARN level.
     * @param messageKey Key in resource file.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void warn(String messageKey, Object... args);

    /**
     * Logs localized message with ERROR level.
     * @param messageKey Key in resource file.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void error(String messageKey, Object... args);

    /**
     * Logs localized message with FATAL level.
     * @param messageKey Key in resource file.
     * @param throwable Throwable to log.
     * @param args Arguments, which will be provided to template of localized message.
     */
    void fatal(String messageKey, Throwable throwable, Object... args);
}

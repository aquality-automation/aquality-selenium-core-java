package aquality.selenium.core.logging;


import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * This class is using for a creating extended log. It implements a Singleton pattern
 */
public final class Logger {

    private static ThreadLocal<org.apache.logging.log4j.Logger> log4J = ThreadLocal.withInitial(()
            -> org.apache.logging.log4j.LogManager.getLogger(String.valueOf(Thread.currentThread().getId())));
    private static ThreadLocal<Logger> instance = ThreadLocal.withInitial(Logger::new);

    private Logger() {
    }

    public void removeLog4jLocalThread() {
        log4J.remove();
    }

    /**
     * Gets logger instance
     *
     * @return logger instance
     */
    public static Logger getInstance() {
        return instance.get();
    }

    /**
     * Adds appender
     *
     * @param appender Appender to be added
     * @return logger instance
     */
    public Logger addAppender(Appender appender) {
        appender.start();
        LoggerContext.getContext(false).getRootLogger().addAppender(appender);
        return getInstance();
    }

    /**
     * Removes appender
     *
     * @param appender Appender to be removed
     * @return logger instance
     */
    public Logger removeAppender(Appender appender) {
        appender.stop();
        LoggerContext.getContext(false).getRootLogger().removeAppender(appender);
        return getInstance();
    }

    /**
     * Debug log
     *
     * @param message Message
     */
    public void debug(String message) {
        log4J.get().debug(message);
    }

    /**
     * Debug log
     *
     * @param message   Message
     * @param throwable Throwable
     */
    public void debug(String message, Throwable throwable) {
        log4J.get().debug(message, throwable);
    }

    /**
     * Info log
     *
     * @param message Message
     */
    public void info(String message) {
        log4J.get().info(message);
    }

    /**
     * Warning log
     *
     * @param message Message
     */
    public void warn(String message) {
        log4J.get().warn(message);
    }

    /**
     * Error log
     *
     * @param message Message
     */
    public void error(String message) {
        log4J.get().error(message);
    }

    /**
     * Fatal log
     *
     * @param message   Message
     * @param throwable Throwable
     */
    public void fatal(final String message, Throwable throwable) {
        log4J.get().fatal(String.format("%s: %s", message, throwable.toString()));
    }

    /**
     * tries to get localized value by passed key, then applies String.format to fetched value and params
     * then makes record in the log output
     *
     * @param key    key from localized dictionary
     * @param params list of params to format
     */
    public void info(String key, Object... params) {
        log4J.get().info(String.format(key, params));
    }

    /**
     * tries to get localized value by passed key, then applies String.format to fetched value and params
     * then makes record in the log output
     *
     * @param key    key from localized dictionary
     * @param params list of params to format
     */
    public void debug(String key, Object... params) {
        log4J.get().debug(String.format(key, params));
    }
}

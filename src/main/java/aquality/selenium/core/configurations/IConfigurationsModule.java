package aquality.selenium.core.configurations;

/**
 * Describes implementations of configurations to be registered in DI container.
 */
public interface IConfigurationsModule {
    /**
     * @return class which implements {@link IVisualConfiguration}
     */
    default Class<? extends IVisualConfiguration> getVisualConfigurationImplementation() {
        return VisualConfiguration.class;
    }

    /**
     * @return class which implements {@link IElementCacheConfiguration}
     */
    default Class<? extends IElementCacheConfiguration> getElementCacheConfigurationImplementation() {
        return ElementCacheConfiguration.class;
    }

    /**
     * @return class which implements {@link ILoggerConfiguration}
     */
    default Class<? extends ILoggerConfiguration> getLoggerConfigurationImplementation() {
        return LoggerConfiguration.class;
    }

    /**
     * @return class which implements {@link IRetryConfiguration}
     */
    default Class<? extends IRetryConfiguration> getRetryConfigurationImplementation() {
        return RetryConfiguration.class;
    }

    /**
     * @return class which implements {@link ITimeoutConfiguration}
     */
    default Class<? extends ITimeoutConfiguration> getTimeoutConfigurationImplementation() {
        return TimeoutConfiguration.class;
    }
}

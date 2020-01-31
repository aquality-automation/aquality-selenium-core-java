package aquality.selenium.core.localization;

/**
 * Describes implementations of localization services to be registered in DI container.
 */
public interface ILocalizationModule {
    /**
     * @return class which implements ILocalizationManager
     */
    default Class<? extends ILocalizationManager> getLocalizationManagerImplementation() {
        return LocalizationManager.class;
    }

    /**
     * @return class which implements ILocalizedLogger
     */
    default Class<? extends ILocalizedLogger> getLocalizedLoggerImplementation() {
        return LocalizedLogger.class;
    }
}

package aquality.selenium.core.localization;

public interface ILocalizationModule {
    default Class<? extends ILocalizationManager> getLocalizationManagerImplementation() {
        return LocalizationManager.class;
    }

    default Class<? extends ILocalizedLogger> getLocalizedLoggerImplementation() {
        return LocalizedLogger.class;
    }
}

package aquality.selenium.core.localization;

import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import com.google.inject.Inject;

public class LocalizationManager implements ILocalizationManager {
    private static final String LANG_RESOURCE_TEMPLATE = "localization/%1$s.json";
    private final ISettingsFile localizationFile;
    private final Logger logger;

    @Inject
    public LocalizationManager(ILoggerConfiguration loggerConfiguration, Logger logger) {
        this.logger = logger;
        String language = loggerConfiguration.getLanguage();
        String locResourceName = String.format(LANG_RESOURCE_TEMPLATE, language.toLowerCase());
        localizationFile = new JsonSettingsFile(locResourceName);
    }

    @Override
    public String getLocalizedMessage(String messageKey, Object... args) {
        String jsonKeyPath = "/".concat(messageKey);
        if (localizationFile.isValuePresent(jsonKeyPath)) {
            return String.format(localizationFile.getValue(jsonKeyPath).toString(), args);
        }

        logger.warn(String.format("Cannot find localized message by key '%1$s'", jsonKeyPath));
        return messageKey;
    }
}

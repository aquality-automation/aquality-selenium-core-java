package aquality.selenium.core.localization;

import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import com.google.inject.Inject;

public class LocalizationManager implements ILocalizationManager {
    private static final String LANG_RESOURCE_TEMPLATE = "localization/%1$s.json";
    private final ISettingsFile localizationFile;
    private final ISettingsFile coreLocalizationFile;
    private final Logger logger;
    private final String locResourceName;

    @Inject
    public LocalizationManager(ILoggerConfiguration loggerConfiguration, Logger logger) {
        this.logger = logger;
        String language = loggerConfiguration.getLanguage();
        locResourceName = String.format(LANG_RESOURCE_TEMPLATE, language.toLowerCase());
        ISettingsFile locFile = getLocalizationFileIfExist(locResourceName);
        coreLocalizationFile = new JsonSettingsFile(String.format(LANG_RESOURCE_TEMPLATE, "core." + language.toLowerCase()));
        localizationFile = locFile == null ? coreLocalizationFile : locFile;
    }

    private static ISettingsFile getLocalizationFileIfExist(String fileName) {
        return LocalizationManager.class.getClassLoader().getResource(fileName) == null
                ? null
                : new JsonSettingsFile(fileName);
    }

    @Override
    public String getLocalizedMessage(String messageKey, Object... args) {
        String jsonKeyPath = "/".concat(messageKey);
        ISettingsFile locFile = localizationFile.isValuePresent(jsonKeyPath) ? localizationFile : coreLocalizationFile;
        if (locFile.isValuePresent(jsonKeyPath)) {
            return String.format(locFile.getValue(jsonKeyPath).toString(), args);
        }

        logger.warn(String.format("Cannot find localized message by key '%1$s' in resource file %2$s",
                jsonKeyPath, locResourceName));
        return messageKey;
    }
}

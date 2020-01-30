package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class LoggerConfiguration implements ILoggerConfiguration {

    private static final String DEFAULT_LANGUAGE = "en";
    private final ISettingsFile settingsFile;

    @Inject
    public LoggerConfiguration(ISettingsFile settingsFile){
        this.settingsFile = settingsFile;
    }

    @Override
    public String getLanguage() {
        return settingsFile.getValueOrDefault("/logger/language", DEFAULT_LANGUAGE).toString();
    }
}

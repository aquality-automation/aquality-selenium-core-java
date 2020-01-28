package aquality.selenium.core.configurations;

import aquality.selenium.core.localization.SupportedLanguage;
import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class LoggerConfiguration implements ILoggerConfiguration{

    private final ISettingsFile settingsFile;

    @Inject
    public LoggerConfiguration(ISettingsFile settingsFile){
        this.settingsFile = settingsFile;
    }

    @Override
    public SupportedLanguage getLanguage() {
        return SupportedLanguage.valueOf(settingsFile.getValue("/logger/language").toString().toUpperCase());
    }
}

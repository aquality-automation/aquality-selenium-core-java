package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class LoggerConfiguration implements ILoggerConfiguration {
    private final ISettingsFile settingsFile;

    @Inject
    public LoggerConfiguration(ISettingsFile settingsFile){
        this.settingsFile = settingsFile;
    }

    @Override
    public String getLanguage() {
        return settingsFile.getValue("/logger/language").toString();
    }
}

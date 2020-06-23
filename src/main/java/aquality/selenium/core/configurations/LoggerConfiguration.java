package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class LoggerConfiguration implements ILoggerConfiguration {

    private static final String DEFAULT_LANGUAGE = "en";
    private final String language;
    private final boolean doLogPageSource;

    @Inject
    public LoggerConfiguration(ISettingsFile settingsFile){
        language = settingsFile.getValueOrDefault("/logger/language", DEFAULT_LANGUAGE).toString();
        doLogPageSource = Boolean.parseBoolean(
                settingsFile.getValueOrDefault("/logger/logPageSource", true).toString());
    }

    @Override
    public String getLanguage() {
        return language;
    }

    @Override
    public boolean logPageSource() {
        return doLogPageSource;
    }
}

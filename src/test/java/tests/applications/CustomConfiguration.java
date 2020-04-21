package tests.applications;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class CustomConfiguration implements ICustomConfiguration {
    private final ISettingsFile settingsFile;

    @Inject
    public CustomConfiguration(ISettingsFile settingsFile){
        this.settingsFile = settingsFile;
    }

    public double getDoubleValue() {
        return (double) settingsFile.getValue("/custom/doubleValue");
    }

    public String getStringValue() {
        return settingsFile.getValue("/custom/stringValue").toString();
    }
}

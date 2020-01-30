package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class ElementCacheConfiguration implements IElementCacheConfiguration{

    private static final String JSON_PATH = "/elementCache/isEnabled";
    private boolean isEnabled;

    @Inject
    public ElementCacheConfiguration(ISettingsFile settingsFile){
        isEnabled = settingsFile.isValuePresent(JSON_PATH) && Boolean.valueOf(settingsFile.getValue(JSON_PATH).toString());
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}

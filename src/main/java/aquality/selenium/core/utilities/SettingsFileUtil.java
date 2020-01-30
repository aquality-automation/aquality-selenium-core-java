package aquality.selenium.core.utilities;

public class SettingsFileUtil {

    private SettingsFileUtil() {
    }

    public static Object getValueOrDefault(ISettingsFile settingsFile, String path, Object defaultValue) {
        return settingsFile.isValuePresent(path) ? settingsFile.getValue(path) : defaultValue;
    }
}

package aquality.selenium.core.utilities;

import java.util.List;
import java.util.Map;

/**
 * Describes reader of settings file.
 */
public interface ISettingsFile {

    /**
     * Gets value from settings file.
     *
     * @param path Path to the value. Depending on file format, it can be jsonPath, xpath etc.
     * @return Value.
     */
    Object getValue(String path);

    /**
     * Gets list of values from settings file.
     *
     * @param path Path to the value. Depending on file format, it can be jsonPath, xpath etc.
     * @return List of values.
     */
    List<String> getList(String path);

    /**
     * Gets map of keys and values from settings file.
     *
     * @param path Path to the value. Depending on file format, it can be jsonPath, xpath etc.
     * @return Map of keys and values.
     */
    Map<String, Object> getMap(String path);

    /**
     * Gets content of settings file.
     *
     * @return Content of settings file as string.
     */
    String getContent();

    /**
     * Checks if value exists in settings.
     *
     * @param path Path to the values. Depending on file format, it can be jsonPath, xpath etc.
     * @return True if exists, false otherwise.
     */
    boolean isValuePresent(String path);
}

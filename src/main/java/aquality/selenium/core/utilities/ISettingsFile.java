package aquality.selenium.core.utilities;

import java.util.List;
import java.util.Map;

public interface ISettingsFile {

    Object getValue(String jsonPath);

    List<String> getList(String jsonPath);

    Map<String, Object> getMap(String jsonPath);

    String getContent();

    boolean isValuePresent(String path);
}

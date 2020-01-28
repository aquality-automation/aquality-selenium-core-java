package aquality.selenium.core.utilities;

import aquality.selenium.core.logging.Logger;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class JsonSettingsFile implements ISettingsFile {

    private final ObjectMapper mapper = new ObjectMapper();
    private final String content;

    public JsonSettingsFile(File file) throws IOException {
        this.content = getFileContent(file.getCanonicalPath());
    }

    public JsonSettingsFile(String resourceName) {
        ResourceFile resourceFile = new ResourceFile(resourceName);
        this.content = resourceFile.getFileContent();
    }

    @Override
    public Object getValue(String jsonPath) {
        return getEnvValueOrDefault(jsonPath);
    }

    private Object getEnvValueOrDefault(String jsonPath) {
        String envVar = getEnvValue(jsonPath);
        if (envVar == null) {
            JsonNode node = getJsonNode(jsonPath);
            if (node.isBoolean()) {
                return node.asBoolean();
            } else if (node.isLong()) {
                return node.asLong();
            } else if (node.isInt()) {
                return node.asInt();
            } else {
                return node.asText();
            }
        }

        return envVar;
    }

    private String getEnvValue(String jsonPath) {
        String key = jsonPath.replace("/", ".").substring(1, jsonPath.length());
        String envVar = System.getProperty(key);
        if (envVar != null) {
            Logger.getInstance().debug(String.format("***** Using variable passed from environment %1$s=%2$s", key, envVar));
        }

        return envVar;
    }

    @Override
    public List<String> getList(String jsonPath) {
        List<String> list = new ArrayList<>();
        String envVar = getEnvValue(jsonPath);
        if (envVar != null) {
            Arrays.stream(envVar.split(",")).forEach(element -> list.add(element.trim()));
        } else {
            getJsonNode(jsonPath).elements().forEachRemaining(node -> list.add(node.asText()));
        }

        return list;
    }

    @Override
    public Map<String, Object> getMap(String jsonPath) {
        Iterator<Map.Entry<String, JsonNode>> iterator = getJsonNode(jsonPath).fields();
        final Map<String, Object> result = new HashMap<>();
        iterator.forEachRemaining(entry -> result.put(entry.getKey(), getValue(jsonPath + "/" + entry.getKey())));
        return result;
    }

    private JsonNode getJsonNode(String jsonPath) {
        try {
            JsonNode node = mapper.readTree(content);
            return node.at(jsonPath);
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Json field by json-path %1$s was not found in the file %2$s", jsonPath, content), e);
        }
    }

    private String getFileContent(String filename) {
        try {
            return new String(Files.readAllBytes(Paths.get(filename)));
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("Content of file %1$s can't be read as String", filename), e);
        }
    }

    @Override
    public boolean isValuePresent(String path) {
        String value = getValue(path).toString().trim();
        return !value.isEmpty();
    }
}

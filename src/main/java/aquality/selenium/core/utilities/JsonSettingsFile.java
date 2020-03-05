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
import java.util.Map.Entry;

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
        return getEnvValueOrDefault(jsonPath, true);
    }

    private Object getEnvValueOrDefault(String jsonPath, boolean throwIfEmpty) {
        String envVar = getEnvValue(jsonPath);
        JsonNode node = getJsonNode(jsonPath, throwIfEmpty && envVar == null);
        return !node.isMissingNode()
                ? castEnvOrDefaulValue(node, envVar)
                : envVar;
    }

    /**
     * Casts envVar to type, defined from JsonNode.
     *
     * @param node   node from json file
     * @param envVar value got from environment variable
     * @return Value, casted to specific type.
     */
    private Object castEnvOrDefaulValue(JsonNode node, String envVar) {
        if (node.isBoolean()) {
            return envVar == null ? node.asBoolean() : Boolean.parseBoolean(envVar);
        } else if (node.isLong()) {
            return envVar == null ? node.asLong() : Long.parseLong(envVar);
        } else if (node.isInt()) {
            return envVar == null ? node.asInt() : Integer.parseInt(envVar);
        } else {
            return envVar == null ? node.asText() : envVar;
        }
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
        Iterator<Entry<String, JsonNode>> iterator = getJsonNode(jsonPath).fields();
        final Map<String, Object> result = new HashMap<>();
        iterator.forEachRemaining(entry -> result.put(entry.getKey(), getValue(jsonPath + "/" + entry.getKey())));
        return result;
    }

    private JsonNode getJsonNode(String jsonPath) {
        return getJsonNode(jsonPath, true);
    }

    private JsonNode getJsonNode(String jsonPath, boolean throwIfEmpty) {
        JsonNode nodeAtPath;
        String errorMessage = String.format("Json field by json-path %1$s was not found in the file %2$s", jsonPath, content);
        try {
            JsonNode node = mapper.readTree(content);
            nodeAtPath = node.at(jsonPath);
        } catch (IOException e) {
            throw new UncheckedIOException(errorMessage, e);
        }
        if (throwIfEmpty && nodeAtPath.isMissingNode()) {
            throw new IllegalArgumentException(errorMessage);
        }
        return nodeAtPath;
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
        Object value = getEnvValueOrDefault(path, false);
        return value != null && !value.toString().trim().isEmpty();
    }
}

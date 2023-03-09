package aquality.selenium.core.configurations;

import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Represents visualization configuration, used for image comparison.
 * Uses {@link ISettingsFile} as source for configuration values.
 */
public class VisualConfiguration implements IVisualConfiguration {
    private String imageFormat;
    private Integer maxFullFileNameLength;
    private Float defaultThreshold;
    private Integer comparisonWidth;
    private Integer comparisonHeight;
    private String pathToDumps;

    private final ISettingsFile settingsFile;

    /**
     * Instantiates class using {@link ISettingsFile} with visualization settings.
     * @param settingsFile settings file.
     */
    @Inject
    public VisualConfiguration(ISettingsFile settingsFile) {
        this.settingsFile = settingsFile;
    }

    @Override
    public String getImageFormat() {
        if (imageFormat == null) {
            String[] supportedFormats = ImageIO.getWriterFormatNames();
            String valueFromConfig = settingsFile.getValueOrDefault("/visualization/imageExtension", "png").toString();
            String actualFormat = valueFromConfig.startsWith(".") ? valueFromConfig.substring(1) : valueFromConfig;
            if (Arrays.stream(supportedFormats).noneMatch(format -> format.equals(actualFormat))) {
                throw new IllegalArgumentException(String.format(
                        "Format [%s] is not supported by current JRE. Supported formats: %s", actualFormat, Arrays.toString(supportedFormats)));
            }
            imageFormat = actualFormat;
        }
        return imageFormat;
    }

    @Override
    public int getMaxFullFileNameLength() {
        if (maxFullFileNameLength == null) {
            maxFullFileNameLength = Integer.valueOf(
                    settingsFile.getValueOrDefault("/visualization/maxFullFileNameLength", 255).toString());
        }
        return maxFullFileNameLength;
    }

    @Override
    public float getDefaultThreshold() {
        if (defaultThreshold == null) {
            defaultThreshold = Float.valueOf(
                    settingsFile.getValueOrDefault("/visualization/defaultThreshold", 0.012f).toString());
        }
        return defaultThreshold;
    }

    @Override
    public int getComparisonWidth() {
        if (comparisonWidth == null) {
            comparisonWidth = Integer.valueOf(
                    settingsFile.getValueOrDefault("/visualization/comparisonWidth", 16).toString());
        }
        return comparisonWidth;
    }

    @Override
    public int getComparisonHeight() {
        if (comparisonHeight == null) {
            comparisonHeight = Integer.valueOf(
                    settingsFile.getValueOrDefault("/visualization/comparisonHeight", 16).toString());
        }
        return comparisonHeight;
    }

    @Override
    public String getPathToDumps() {
        if (pathToDumps == null) {
            pathToDumps = settingsFile.getValueOrDefault(".visualization.pathToDumps", "./src/test/resources/VisualDumps/").toString();
            if (pathToDumps.startsWith(".")) {
                try {
                    pathToDumps = new File(pathToDumps).getCanonicalPath();
                } catch (IOException e) {
                    String errorMessage = "Failed to resolve path to dumps: " + e.getMessage();
                    Logger.getInstance().fatal(errorMessage, e);
                    throw new IllegalArgumentException(errorMessage, e);
                }
            }
        }
        return pathToDumps;
    }
}

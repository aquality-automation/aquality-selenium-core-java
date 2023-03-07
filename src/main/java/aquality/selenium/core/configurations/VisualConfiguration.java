package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

import javax.imageio.ImageIO;
import java.util.Arrays;

/**
 * Represents visualization configuration, used for image comparison.
 * Uses {@link ISettingsFile} as source for configuration values.
 */
public class VisualConfiguration implements IVisualConfiguration {
    private String imageFormat;
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
}

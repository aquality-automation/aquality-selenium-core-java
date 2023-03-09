package aquality.selenium.core.configurations;

/**
 * Represents visualization configuration, used for image comparison.
 */
public interface IVisualConfiguration {
    /**
     * Image format for comparison.
     * @return image format.
     */
    String getImageFormat();

    /**
     * Gets maximum length of full file name with path for image comparison.
     * @return maximum symbols count in file path.
     */
    int getMaxFullFileNameLength();

    /**
     * Gets default threshold used for image comparison.
     * @return The default threshold value.
     */
    float getDefaultThreshold();

    /**
     * Gets width of the image resized for comparison.
     * @return comparison width.
     */
    int getComparisonWidth();

    /**
     * Gets height of the image resized for comparison.
     * @return comparison height.
     */
    int getComparisonHeight();

    /**
     * Gets path used to save and load page dumps.
     * @return path to dumps.
     */
    String getPathToDumps();
}

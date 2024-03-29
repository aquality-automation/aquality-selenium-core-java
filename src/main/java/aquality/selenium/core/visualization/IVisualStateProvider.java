package aquality.selenium.core.visualization;

import aquality.selenium.core.configurations.IVisualizationConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Provides visual state of the element.
 */
public interface IVisualStateProvider {

    /**
     * Gets a size object containing the height and width of this element.
     * @return size of the element.
     */
    Dimension getSize();

    /**
     * Gets a point object containing the coordinates of the upper-left
     * corner of this element relative to the upper-left corner of the page.
     * @return location of the element.
     */
    Point getLocation();

    /**
     * Gets an image containing the screenshot of the element.
     * @return screenshot of the element.
     */
    BufferedImage getImage();

    /**
     * Gets the difference between the image of the element and the provided image using {@link IImageComparator}.
     * @param theOtherOne the image to compare the element with.
     * @param threshold how big a difference will be ignored as a percentage - value between 0 and 1.
     * @return the difference between the two images as a percentage  - value between 0 and 1.
     */
    float getDifference(Image theOtherOne, float threshold);

    /**
     * Gets the difference between the image of the element and the provided image using {@link IImageComparator}.
     * The threshold value is got from {@link IVisualizationConfiguration}.
     * @param theOtherOne the image to compare the element with.
     * @return the difference between the two images as a percentage  - value between 0 and 1.
     */
    float getDifference(Image theOtherOne);
}

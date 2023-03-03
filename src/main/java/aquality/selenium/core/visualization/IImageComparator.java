package aquality.selenium.core.visualization;

import java.awt.*;

/**
 * Compares images with defined threshold.
 * Default implementation does resize and gray-scaling to simplify comparison.
 */
public interface IImageComparator {

    /**
     * Gets the difference between two images as a percentage.
     * @param thisOne The first image
     * @param theOtherOne The image to compare with
     * @param threshold How big a difference will be ignored as a percentage - value between 0 and 1.
     * @return The difference between the two images as a percentage - value between 0 and 1.
     */
    float percentageDifference(Image thisOne, Image theOtherOne, float threshold);

    /**
     * Gets the difference between two images as a percentage.
     * @param thisOne The first image
     * @param theOtherOne The image to compare with
     * @return The difference between the two images as a percentage - value between 0 and 1.
     */
    float percentageDifference(Image thisOne, Image theOtherOne);
}

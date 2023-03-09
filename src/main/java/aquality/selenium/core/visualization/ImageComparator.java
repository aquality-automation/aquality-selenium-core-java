package aquality.selenium.core.visualization;

import aquality.selenium.core.configurations.IVisualConfiguration;
import com.google.inject.Inject;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ImageComparator implements IImageComparator {
    private static final int THRESHOLD_DIVISOR = 255;
    private final IVisualConfiguration visualConfiguration;

    @Inject
    public ImageComparator(IVisualConfiguration visualConfiguration) {
        this.visualConfiguration = visualConfiguration;
    }

    private int getComparisonHeight() {
        return visualConfiguration.getComparisonHeight();
    }

    private int getComparisonWidth() {
        return visualConfiguration.getComparisonWidth();
    }

    public float percentageDifference(Image thisOne, Image theOtherOne, float threshold) {
        if (threshold < 0 || threshold > 1) {
            throw new IllegalArgumentException(String.format("Threshold should be between 0 and 1, but was [%s]", threshold));
        }

        int intThreshold = (int) (threshold * THRESHOLD_DIVISOR);
        return percentageDifference(thisOne, theOtherOne, intThreshold);
    }

    public float percentageDifference(Image thisOne, Image theOtherOne) {
        return percentageDifference(thisOne, theOtherOne, visualConfiguration.getDefaultThreshold());
    }

    protected float percentageDifference(Image thisOne, Image theOtherOne, int threshold) {
        int[][] differences = getDifferences(thisOne, theOtherOne);

        int diffPixels = 0;

        for (int[] bytes : differences) {
            for (int b : bytes) {
                if (b > threshold) {
                    diffPixels++;
                }
            }
        }

        return diffPixels / (float) (getComparisonWidth() * getComparisonHeight());
    }

    protected int[][] getDifferences(Image thisOne, Image theOtherOne) {
        int[][] firstGray = getResizedGrayScaleValues(thisOne);
        int[][] secondGray = getResizedGrayScaleValues(theOtherOne);

        int[][] differences = new int[getComparisonWidth()][getComparisonHeight()];
        for (int y = 0; y < getComparisonHeight(); y++) {
            for (int x = 0; x < getComparisonWidth(); x++) {
                differences[x][y] = (byte) Math.abs(firstGray[x][y] - secondGray[x][y]);
            }
        }

        return differences;
    }

    protected int[][] getResizedGrayScaleValues(Image image) {
        BufferedImage resizedImage = ImageFunctions.resize(image, getComparisonWidth(), getComparisonHeight());
        BufferedImage grayImage = ImageFunctions.grayscale(resizedImage);
        int[][] grayScale = new int[grayImage.getWidth()][grayImage.getHeight()];
        for (int y = 0; y < grayImage.getHeight(); y++) {
            for (int x = 0; x < grayImage.getWidth(); x++) {
                int pixel = resizedImage.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                grayScale[x][y] = Math.abs(red);
            }
        }

        return grayScale;
    }
}

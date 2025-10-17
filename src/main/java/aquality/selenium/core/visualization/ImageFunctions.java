package aquality.selenium.core.visualization;

import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.logging.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.Arrays;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Image and screenshot extensions.
 */
public class ImageFunctions {
    private ImageFunctions() throws InstantiationException {
        throw new InstantiationException("Static ImageFunctions should not be initialized");
    }

    /**
     * Reads image from file.
     * @param file The file to read the image from.
     * @return Instance of BufferedImage.
     */
    public static BufferedImage readImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException exception) {
            Logger.getInstance().fatal(String.format("Failed to get the file [%s] as an image", file), exception);
            throw new UncheckedIOException(exception);
        }
    }

    /**
     * Represents given element's screenshot as an image.
     *
     * @param element given element.
     * @return image object.
     */
    public static BufferedImage getScreenshotAsImage(IElement element) {
        return getScreenshotAsImage(element.getElement());
    }

    /**
     * Represents given element's screenshot as an image.
     *
     * @param element given element.
     * @return image object.
     */
    public static BufferedImage getScreenshotAsImage(RemoteWebElement element) {
        byte[] bytes = element.getScreenshotAs(OutputType.BYTES);
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(is);
        } catch (IOException exception) {
            Logger.getInstance().fatal("Failed to get element's screenshot as an image", exception);
            return new BufferedImage(0, 0, TYPE_INT_RGB);
        }
    }

    /**
     * Represents dimension of the given image.
     *
     * @param image given image.
     * @return size of the given image.
     */
    public static Dimension getSize(Image image) {
        if (image instanceof RenderedImage) {
            RenderedImage renderedImage = (RenderedImage) image;
            return new Dimension(renderedImage.getWidth(), renderedImage.getHeight());
        } else {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }
    }

    /**
     * Gray-scaling the image.
     *
     * @param image source image.
     * @return gray-scaled image.
     */
    public static BufferedImage grayscale(BufferedImage image) {
        BufferedImage result = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int y = 0; y < result.getHeight(); y++) {
            for (int x = 0; x < result.getWidth(); x++) {
                int pixel = image.getRGB(x, y);
                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = pixel & 0xff;
                int average = (r + g + b) / 3;
                pixel = (a << 24) | (average << 16) | (average << 8) | average;
                result.setRGB(x, y, pixel);
            }
        }
        return result;
    }

    /**
     * Resizes image giving higher priority to image smoothness than scaling speed.
     * @param image source image.
     * @param width target width.
     * @param height target height.
     * @return resized image.
     */
    public static BufferedImage resize(Image image, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, TYPE_INT_RGB);
        Graphics graphics = resizedImage.getGraphics();
        Image scaledImage = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        graphics.drawImage(scaledImage, 0, 0, width, height, null);
        graphics.dispose();
        return resizedImage;
    }

    /**
     * Redraw the image and saves it to target file.
     * @param image source image.
     * @param file target file.
     * @param format target format.
     * @throws IOException if an error occurs during file creation or writing.
     */
    public static void save(Image image, File file, String format) throws IOException {
        if (file.exists() || file.createNewFile()) {
            BufferedImage newImage = new BufferedImage(image.getWidth(null), image.getHeight(null), TYPE_INT_RGB);
            Graphics2D graphics = newImage.createGraphics();
            graphics.drawImage(image, 0, 0, null);
            graphics.dispose();
            ImageIO.write(newImage, format, file);
        }
    }

    /**
     * Validates that image format is supported by the current JRE.
     *
     * @param actualFormat image format to check.
     */
    public static void validateImageFormat(String actualFormat) {
        String[] supportedFormats = ImageIO.getWriterFormatNames();
        if (Arrays.stream(supportedFormats).noneMatch(format -> format.equals(actualFormat))) {
            throw new IllegalArgumentException(String.format(
                    "Format [%s] is not supported by current JRE. Supported formats: %s", actualFormat, Arrays.toString(supportedFormats)));
        }
    }
}

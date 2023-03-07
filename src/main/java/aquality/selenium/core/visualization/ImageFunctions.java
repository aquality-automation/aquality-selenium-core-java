package aquality.selenium.core.visualization;

import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.logging.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Image and screenshot extensions.
 */
public class ImageFunctions {
    /**
     * Represents given element's screenshot as an image.
     * @param element given element.
     * @return image object.
     */
    public static BufferedImage getScreenshotAsImage(IElement element) {
        return getScreenshotAsImage(element.getElement());
    }

    /**
     * Represents given element's screenshot as an image.
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
     * @param image given image.
     * @return size of the given image.
     */
    public static Dimension getSize(Image image) {
        if (image instanceof RenderedImage) {
            RenderedImage renderedImage = (RenderedImage) image;
            return new Dimension(renderedImage.getWidth(), renderedImage.getHeight());
        }
        else {
            return new Dimension(image.getWidth(null), image.getHeight(null));
        }

    }
}

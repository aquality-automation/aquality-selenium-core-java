package aquality.selenium.core.visualization;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.remote.RemoteWebElement;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

public class VisualStateProvider {

    private final Supplier<RemoteWebElement> getElement;

    public VisualStateProvider(Supplier<RemoteWebElement> getElement){
        this.getElement = getElement;
    }

    public Dimension getSize() {
        return getElement.get().getSize();
    }

    public Point getLocation() {
        return getElement.get().getLocation();
    }

    public Image getImage() {
        byte[] bytes = getElement.get().getScreenshotAs(OutputType.BYTES);
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            return ImageIO.read(is);
        } catch (IOException exception) {
            //log
            return new BufferedImage(0, 0, TYPE_INT_RGB);
        }
    }
}

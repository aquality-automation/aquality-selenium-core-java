package aquality.selenium.core.visualization;

import aquality.selenium.core.logging.ILogVisualState;
import aquality.selenium.core.utilities.IElementActionRetrier;
import org.openqa.selenium.remote.RemoteWebElement;

import java.awt.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class VisualStateProvider implements IVisualStateProvider {
    private final IImageComparator imageComparator;
    private final IElementActionRetrier actionRetrier;
    private final Supplier<RemoteWebElement> getElement;
    private final ILogVisualState stateLogger;

    public VisualStateProvider(IImageComparator imageComparator, IElementActionRetrier actionRetrier, Supplier<RemoteWebElement> getElement, ILogVisualState stateLogger){
        this.imageComparator = imageComparator;
        this.actionRetrier = actionRetrier;
        this.getElement = getElement;
        this.stateLogger = stateLogger;
    }

    @Override
    public Dimension getSize() {
        return getLoggedValue("size", element -> {
            final org.openqa.selenium.Dimension size = element.getSize();
            return new Dimension(size.getWidth(), size.getHeight());
        }, null);
    }

    @Override
    public Point getLocation() {
        return getLoggedValue("location", element -> {
            final org.openqa.selenium.Point location = element.getLocation();
            return new Point(location.getX(), location.getY());
        }, null);
    }

    @Override
    public Image getImage() {
        return getLoggedValue("image", ImageFunctions::getScreenshotAsImage,
                image -> getStringValue(ImageFunctions.getSize(image)));
    }

    @Override
    public float getDifference(Image theOtherOne, float threshold) {
        Image currentImage = getImage();
        float value = 1;
        stateLogger.logVisualState("loc.el.visual.getdifference.withthreshold",
                getStringValue(ImageFunctions.getSize(theOtherOne)), threshold * 100);
        if (!ImageFunctions.getSize(currentImage).equals(new Dimension())) {
            value = imageComparator.percentageDifference(currentImage, theOtherOne, threshold);
        }
        stateLogger.logVisualState("loc.el.visual.difference.value", value * 100);
        return value;
    }

    @Override
    public float getDifference(Image theOtherOne) {
        Image currentImage = getImage();
        float value = 1;
        stateLogger.logVisualState("loc.el.visual.getdifference",
                getStringValue(ImageFunctions.getSize(theOtherOne)));
        if (!ImageFunctions.getSize(currentImage).equals(new Dimension())) {
            value = imageComparator.percentageDifference(currentImage, theOtherOne);
        }
        stateLogger.logVisualState("loc.el.visual.difference.value", value * 100);
        return value;
    }

    private <T> T getLoggedValue(String name, Function<RemoteWebElement, T> getValue, Function<T, String> toString) {
        stateLogger.logVisualState("loc.el.visual.get" + name);
        T value = actionRetrier.doWithRetry(() -> getValue.apply(getElement.get()));
        String stringValue = toString == null ? getStringValue(value) : toString.apply(value);
        stateLogger.logVisualState(String.format("loc.el.visual.%s.value", name), stringValue);
        return value;
    }

    private <T> String getStringValue(T value) {
        return value.toString().replace(value.getClass().getName(), "");
    }
}

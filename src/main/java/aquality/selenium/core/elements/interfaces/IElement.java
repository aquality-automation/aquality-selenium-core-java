package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.visualization.IVisualStateProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.Duration;

public interface IElement extends IParent {
    /**
     * Gets unique locator of element.
     *
     * @return Element locator
     */
    By getLocator();

    /**
     * Gets unique name of element
     *
     * @return name
     */
    String getName();

    /**
     * Provides ability to define of element's state (whether it is displayed, exists or not) and respective waiting functions
     *
     * @return provider to define element's state
     */
    IElementStateProvider state();

    /**
     * Gets element visual state.
     * @return provider to define element's visual state.
     */
    IVisualStateProvider visual();

    /**
     * Gets current element by specified {@link #getLocator()}
     * Default timeout is provided in {@link aquality.selenium.core.configurations.ITimeoutConfiguration}
     * {@link org.openqa.selenium.NoSuchElementException} throws if element not found
     *
     * @return instance of {@link WebElement} if found.
     */
    default WebElement getElement() {
        return getElement(null);
    }

    /**
     * Gets current element by specified {@link #getLocator()}
     * {@link org.openqa.selenium.NoSuchElementException} throws if element not found
     *
     * @param timeout Timeout for waiting
     * @return instance of {@link WebElement} if found.
     */
    WebElement getElement(Duration timeout);

    /**
     * Gets the item text (inner text).
     *
     * @return text of element
     */
    String getText();

    /**
     * Gets attribute value of the element.
     *
     * @param attr Attribute name
     * @return Attribute value
     */
    String getAttribute(String attr);

    /**
     * Sends keys
     *
     * @param keys keys for sending
     */
    void sendKeys(String keys);

    /**
     * Clicks on the item.
     */
    void click();
}

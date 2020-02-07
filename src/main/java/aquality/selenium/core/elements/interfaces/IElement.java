package aquality.selenium.core.elements.interfaces;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;

public interface IElement extends IParent {

    /**
     * Gets clear WebElement.
     *
     * @return WebElement
     */
    default RemoteWebElement getElement() {
        return getElement(null);
    }

    /**
     * Gets clear WebElement.
     *
     * @param timeout Timeout for waiting
     * @return WebElement
     */
    RemoteWebElement getElement(Long timeout);

    /**
     * Gets element name.
     *
     * @return name
     */
    String getName();

    /**
     * Gets element locator.
     *
     * @return locator
     */
    By getLocator();
}

package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;

/**
 * Describes interface to supply class which implements IElement interface
 *
 * @param <T> type of the element to be supplied
 */
public interface IElementSupplier<T extends IElement> {
    /**
     * Returns an instance of element
     *
     * @param locator By locator
     * @param name    output name in logs
     * @param state   desired element's state
     * @return an instance of element
     */
    T get(By locator, String name, ElementState state);
}

package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * Allows to use cached element.
 */
public interface IElementCacheHandler {

    /**
     * Determines is the cached element refresh needed.
     *
     * @return true if the refresh is needed (element wasn't found previously or is stale), false otherwise.
     */
    default boolean isRefreshNeeded() {
        return isRefreshNeeded(null);
    }

    /**
     * Determines is the cached element refresh needed.
     *
     * @param customState custom element's existance state used for search.
     * @return true if the refresh is needed (element wasn't found previously or is stale), false otherwise.
     */
    boolean isRefreshNeeded(ElementState customState);

    /**
     * Determines is the element stale.
     * @return true if the element was found previously and is currently stale, false otherwise.
     */
    default boolean isStale() {
        return wasCached() && isRefreshNeeded();
    }

    /**
     * Determines was the element cached previously.
     * @return true if the element was found and cached previously, false otherwise.
     */
    boolean wasCached();

    /**
     * Allows to get cached element.
     *
     * @param timeout timeout used to retrive the element when {@see isRefreshNeeded()} is true.
     * @param customState custom element's existance state used for search.
     * @return cached element.
     */
    RemoteWebElement getElement(Long timeout, ElementState customState);

    /**
     * Allows to get cached element.
     *
     * @param timeout timeout used to retrive the element when {@see isRefreshNeeded()} is true.
     * @return cached element.
     */
    default RemoteWebElement getElement(Long timeout) {
        return getElement(timeout, null);
    }

    /**
     * Allows to get cached element.
     *
     * @param customState custom element's existance state used for search.
     * @return cached element.
     */
    default RemoteWebElement getElement(ElementState customState) {
        return getElement(null, customState);
    }

    /**
     * Allows to get cached element.
     *
     * @return cached element.
     */
    default RemoteWebElement getElement() {
        return getElement(null, null);
    }
}

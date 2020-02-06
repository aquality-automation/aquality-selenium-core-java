package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;

/**
 * Implementation of {@link IElementCacheHandler}.
 */
public class ElementCacheHandler implements IElementCacheHandler {

    private final By locator;
    private final ElementState state;
    private final IElementFinder finder;

    private RemoteWebElement remoteElement;

    public ElementCacheHandler(By locator, ElementState state, IElementFinder finder) {
        this.locator = locator;
        this.state = state;
        this.finder = finder;
    }

    @Override
    public boolean isRefreshNeeded(ElementState customState) {
        if (!wasCached()) {
            return true;
        }
        try {
            boolean isDisplayed = remoteElement.isDisplayed();
            // refresh is needed only if the property is not match to expected element state
            ElementState requiredState = customState == null ? state : customState;
            return requiredState == ElementState.DISPLAYED && !isDisplayed;
        } catch (Throwable e) {
            // refresh is needed if the property is not available
            return true;
        }
    }

    @Override
    public boolean wasCached() {
        return remoteElement != null;
    }

    @Override
    public RemoteWebElement getElement(Long timeout, ElementState customState) {
        ElementState requiredState = customState == null ? state : customState;
        if (isRefreshNeeded(requiredState)) {
            remoteElement = (RemoteWebElement) finder.findElement(locator, requiredState, timeout);
        }

        return remoteElement;
    }
}

package tests.applications.windowsApp;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.applications.ICachedElement;

public class CachedButton implements ICachedElement {
    private final By locator;
    private final ElementState elementState;
    private IElementCacheHandler elementCacheHandler;

    public CachedButton(By locator) {
        this.locator = locator;
        this.elementState = ElementState.EXISTS_IN_ANY_STATE;
    }


    @Override
    public By getLocator() {
        return locator;
    }

    @Override
    public ElementState getElementState() {
        return elementState;
    }

    @Override
    public IElementCacheHandler getElementCacheHandler() {
        return elementCacheHandler;
    }

    @Override
    public void setElementCacheHandler(IElementCacheHandler elementCacheHandler) {
        this.elementCacheHandler = elementCacheHandler;
    }

    @Override
    public IElementFinder getElementFinder() {
        return AqualityServices.get(IElementFinder.class);
    }

    @Override
    public IConditionalWait getConditionalWait() {
        return AqualityServices.get(IConditionalWait.class);
    }

    @Override
    public ILocalizedLogger getLocalizedLogger() {
        return AqualityServices.get(ILocalizedLogger.class);
    }
}

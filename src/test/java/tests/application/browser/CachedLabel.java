package tests.application.browser;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.application.ICachedElement;

public class CachedLabel implements ICachedElement {
    private final By locator;
    private final ElementState elementState;
    private IElementCacheHandler elementCacheHandler;

    public CachedLabel(By locator, ElementState state) {
        this.locator = locator;
        this.elementState = state;
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
}

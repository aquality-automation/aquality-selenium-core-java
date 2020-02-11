package tests.application;

import aquality.selenium.core.elements.CachedElementStateProvider;
import aquality.selenium.core.elements.ElementCacheHandler;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;

public interface ICachedElement {

    By getLocator();
    ElementState getElementState();
    IElementCacheHandler getElementCacheHandler();
    void setElementCacheHandler(IElementCacheHandler elementCacheHandler);
    IElementFinder getElementFinder();
    IConditionalWait getConditionalWait();
    ILocalizedLogger getLocalizedLogger();


    default IElementCacheHandler cache() {
        if (getElementCacheHandler() == null) {
            setElementCacheHandler(new ElementCacheHandler(getLocator(), getElementState(), getElementFinder()));
        }
        return getElementCacheHandler();
    }

    default RemoteWebElement getElement() {
        return cache().getElement();
    }

    default void click() {
        getElement().click();
    }

    default IElementStateProvider state() {
        return new CachedElementStateProvider(getLocator(), getConditionalWait(), cache(), getLocalizedLogger());
    }
}

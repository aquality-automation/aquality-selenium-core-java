package tests.applications.browser;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.utilities.IElementActionRetrier;
import aquality.selenium.core.visualization.IImageComparator;
import aquality.selenium.core.visualization.VisualStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.applications.ICachedElement;

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

    @Override
    public ILocalizedLogger getLocalizedLogger() {
        return AqualityServices.get(ILocalizedLogger.class);
    }

    @Override
    public ILocalizationManager getLocalizationManager() {
        return AqualityServices.get(ILocalizationManager.class);
    }

    public VisualStateProvider visual() {
        return new VisualStateProvider(AqualityServices.get(IImageComparator.class),
                AqualityServices.get(IElementActionRetrier.class), this::getElement, (messageKey, args) ->
                getLocalizedLogger().infoElementAction(CachedLabel.class.getSimpleName(), getLocator().toString(), messageKey, args));
    }
}

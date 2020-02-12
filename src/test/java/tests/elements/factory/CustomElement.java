package tests.elements.factory;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.IElementCacheConfiguration;
import aquality.selenium.core.elements.Element;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementFactory;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.utilities.IElementActionRetrier;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.applications.windowsApp.AqualityServices;

public class CustomElement extends Element implements ICustomElement {

    public CustomElement(By locator, String name, ElementState state) {
        super(locator, name, state);
    }

    @Override
    protected IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    @Override
    protected IElementFactory getElementFactory() {
        return AqualityServices.get(IElementFactory.class);
    }

    @Override
    protected IElementFinder getElementFinder() {
        return AqualityServices.get(IElementFinder.class);
    }

    @Override
    protected IElementCacheConfiguration getElementCacheConfiguration() {
        return AqualityServices.get(IElementCacheConfiguration.class);
    }

    @Override
    protected IElementActionRetrier getElementActionRetrier() {
        return AqualityServices.get(IElementActionRetrier.class);
    }

    @Override
    protected ILocalizedLogger getLocalizedLogger() {
        return AqualityServices.get(ILocalizedLogger.class);
    }

    @Override
    protected IConditionalWait getConditionalWait() {
        return AqualityServices.get(IConditionalWait.class);
    }

    @Override
    protected String getElementType() {
        return "Custom";
    }

    public ElementState getState() {
        return getElementState();
    }
}

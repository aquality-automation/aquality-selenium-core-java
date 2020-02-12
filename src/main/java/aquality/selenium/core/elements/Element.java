package aquality.selenium.core.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.IElementCacheConfiguration;
import aquality.selenium.core.elements.interfaces.*;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.IElementActionRetrier;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.function.Supplier;

/**
 * Abstract class, describing wrapper of WebElement.
 */
public abstract class Element implements IElement {

    private final String name;
    private final ElementState elementState;
    private final By locator;
    private IElementCacheHandler elementCacheHandler;

    protected Element(final By loc, final String name, final ElementState state) {
        locator = loc;
        this.name = name;
        elementState = state;
    }

    protected abstract IApplication getApplication();

    protected abstract IElementFactory getElementFactory();

    protected abstract IElementFinder getElementFinder();

    protected abstract IElementCacheConfiguration getElementCacheConfiguration();

    protected abstract IElementActionRetrier getElementActionRetrier();

    protected abstract ILocalizedLogger getLocalizedLogger();

    protected abstract IConditionalWait getConditionalWait();

    protected abstract String getElementType();

    protected IElementCacheHandler getCache() {
        if (elementCacheHandler == null) {
            elementCacheHandler = new ElementCacheHandler(locator, elementState, getElementFinder());
        }

        return elementCacheHandler;
    }

    protected Logger getLogger() {
        return Logger.getInstance();
    }

    protected ElementState getElementState() {
        return elementState;
    }

    @Override
    public By getLocator() {
        return locator;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public IElementStateProvider state() {
        return getElementCacheConfiguration().isEnabled()
                ? new CachedElementStateProvider(locator, getConditionalWait(), getCache(), getLocalizedLogger())
                : new DefaultElementStateProvider(locator, getConditionalWait(), getElementFinder());
    }

    @Override
    public RemoteWebElement getElement(Long timeout) {
        try {
            return getElementCacheConfiguration().isEnabled()
                    ? getCache().getElement(timeout)
                    : (RemoteWebElement) getElementFinder().findElement(locator, elementState, timeout);
        } catch (NoSuchElementException e) {
            logPageSource(e);
            throw e;
        }
    }

    protected void logPageSource(WebDriverException exception) {
        try {
            getLogger().debug("Page source:".concat(System.lineSeparator()).concat(getApplication().getDriver().getPageSource()), exception);
        } catch (WebDriverException e) {
            getLogger().error(exception.getMessage());
            getLocalizedLogger().fatal("loc.get.page.source.failed", e);
        }
    }

    @Override
    public String getText() {
        logElementAction("loc.get.text");
        return doWithRetry(() -> getElement().getText());
    }

    @Override
    public String getAttribute(String attr) {
        logElementAction("loc.el.getattr", attr);
        return doWithRetry(() -> getElement().getAttribute(attr));
    }

    @Override
    public void sendKeys(String keys) {
        logElementAction("loc.text.sending.keys", keys);
        doWithRetry(() -> getElement().sendKeys(keys));
    }

    @Override
    public void click() {
        logElementAction("loc.clicking");
        doWithRetry(() -> getElement().click());
    }

    @Override
    public <T extends IElement> T findChildElement(By childLoc, String name, Class<T> clazz, ElementState state) {
        return getElementFactory().findChildElement(this, childLoc, name, clazz, state);
    }

    @Override
    public <T extends IElement> T findChildElement(By childLoc, String name, IElementSupplier<T> supplier, ElementState state) {
        return getElementFactory().findChildElement(this, childLoc, name, supplier, state);
    }

    protected <T> T doWithRetry(Supplier<T> action) {
        return getElementActionRetrier().doWithRetry(action);
    }

    protected void doWithRetry(Runnable action) {
        getElementActionRetrier().doWithRetry(action);
    }

    protected void logElementAction(String messageKey, Object... args) {
        getLocalizedLogger().infoElementAction(getElementType(), name, messageKey, args);
    }
}

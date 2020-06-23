package aquality.selenium.core.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.IElementCacheConfiguration;
import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.elements.interfaces.*;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.IElementActionRetrier;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.RemoteWebElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

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

    protected abstract ILocalizationManager getLocalizationManager();

    protected abstract IConditionalWait getConditionalWait();

    protected abstract String getElementType();

    protected IElementCacheHandler getCache() {
        if (elementCacheHandler == null) {
            elementCacheHandler = new ElementCacheHandler(locator, elementState, getElementFinder());
        }

        return elementCacheHandler;
    }

    protected ILoggerConfiguration getLoggerConfiguration() {
        return getLocalizedLogger().getConfiguration();
    }

    protected Logger getLogger() {
        return Logger.getInstance();
    }

    protected ILogElementState logElementState() {
        return ((messageKey, stateKey) -> getLocalizedLogger().infoElementAction(getElementType(), getName(), messageKey,
                        getLocalizationManager().getLocalizedMessage(stateKey)));
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
                ? new CachedElementStateProvider(locator, getConditionalWait(), getCache(), logElementState())
                : new DefaultElementStateProvider(locator, getConditionalWait(), getElementFinder(), logElementState());
    }

    @Override
    public RemoteWebElement getElement(Duration timeout) {
        try {
            return getElementCacheConfiguration().isEnabled()
                    ? getCache().getElement(timeout)
                    : (RemoteWebElement) getElementFinder().findElement(locator, elementState, timeout);
        } catch (NoSuchElementException e) {
            if (getLoggerConfiguration().logPageSource()) {
                logPageSource(e);
            }
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
        String value = doWithRetry(() -> getElement().getText());
        logElementAction("loc.text.value", value);
        return value;
    }

    @Override
    public String getAttribute(String attr) {
        logElementAction("loc.el.getattr", attr);
        String value = doWithRetry(() -> getElement().getAttribute(attr));
        logElementAction("loc.el.attr.value", attr, value);
        return value;
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

    @Override
    public <T extends IElement> List<T> findChildElements(By childLoc, String name, Class<T> clazz, ElementState state, ElementsCount count) {
        return getElementFactory().findChildElements(this, childLoc, name, clazz, count, state);
    }

    @Override
    public <T extends IElement> List<T> findChildElements(By childLoc, String name, IElementSupplier<T> supplier, ElementState state, ElementsCount count) {
        return getElementFactory().findChildElements(this, childLoc, name, supplier, count, state);
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

    protected ElementState getElementState() {
        return elementState;
    }
}

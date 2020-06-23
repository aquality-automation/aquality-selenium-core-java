package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.elements.interfaces.ILogElementState;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

/**
 * Provides functions to retrive the state for cached element.
 */
public class CachedElementStateProvider extends ElementStateProvider {

    private final By locator;
    private final IConditionalWait conditionalWait;
    private final IElementCacheHandler elementCacheHandler;

    public CachedElementStateProvider(By locator, IConditionalWait conditionalWait,
                                      IElementCacheHandler elementCacheHandler, ILogElementState logger) {
        super(logger);
        this.locator = locator;
        this.conditionalWait = conditionalWait;
        this.elementCacheHandler = elementCacheHandler;
    }

    protected List<Class<? extends Exception>> getHandledExceptions() {
        return Arrays.asList(StaleElementReferenceException.class, NoSuchElementException.class);
    }

    protected boolean tryInvokeFunction(Predicate<WebElement> predicate) {
        return tryInvokeFunction(predicate, getHandledExceptions());
    }

    protected boolean tryInvokeFunction(Predicate<WebElement> predicate, List<Class<? extends Exception>> handledExceptions) {
        try {
            return predicate.test(elementCacheHandler.getElement(Duration.ZERO, ElementState.EXISTS_IN_ANY_STATE));
        } catch (Exception exception) {
            if (handledExceptions.contains(exception.getClass())) {
                return false;
            }
            throw exception;
        }
    }

    protected boolean waitForCondition(BooleanSupplier condition, String conditionKeyPart, Duration timeout) {
        logElementState("loc.wait.for.state", conditionKeyPart);
        boolean result = conditionalWait.waitFor(condition, timeout);
        if (!result) {
            logElementState("loc.wait.for.state.failed", conditionKeyPart);
        }
        return result;
    }

    @Override
    public boolean isClickable() {
        return tryInvokeFunction(elementClickable().getElementStateCondition());
    }

    @Override
    public void waitForClickable(Duration timeout) {
        String errorMessage = String.format("Element %1$s has not become clickable after timeout.", locator);
        String conditionKeyPart = elementClickable().getStateName();
        try {
            logElementState("loc.wait.for.state", conditionKeyPart);
            conditionalWait.waitForTrue(this::isClickable, timeout, null, errorMessage);
        } catch (TimeoutException e) {
            logElementState("loc.wait.for.state.failed", conditionKeyPart);
            throw new org.openqa.selenium.TimeoutException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isDisplayed() {
        return !elementCacheHandler.isStale() && tryInvokeFunction(WebElement::isDisplayed);
    }

    @Override
    public boolean waitForDisplayed(Duration timeout) {
        return waitForCondition(() -> tryInvokeFunction(WebElement::isDisplayed), "displayed", timeout);
    }

    @Override
    public boolean waitForNotDisplayed(Duration timeout) {
        return waitForCondition(() -> !isDisplayed(), "not.displayed", timeout);
    }

    @Override
    public boolean isExist() {
        return !elementCacheHandler.isStale() && tryInvokeFunction(element -> true);
    }

    @Override
    public boolean waitForExist(Duration timeout) {
        return waitForCondition(() -> tryInvokeFunction(element -> true), "exist", timeout);
    }

    @Override
    public boolean waitForNotExist(Duration timeout) {
        return waitForCondition(() -> !isExist(), "not.exist", timeout);
    }

    @Override
    public boolean isEnabled() {
        return tryInvokeFunction(elementEnabled().getElementStateCondition(), Collections.singletonList(StaleElementReferenceException.class));
    }

    @Override
    public boolean waitForEnabled(Duration timeout) {
        return waitForCondition(this::isEnabled, elementEnabled().getStateName(), timeout);
    }

    @Override
    public boolean waitForNotEnabled(Duration timeout) {
        return waitForCondition(() -> !isEnabled(), elementNotEnabled().getStateName(), timeout);
    }
}

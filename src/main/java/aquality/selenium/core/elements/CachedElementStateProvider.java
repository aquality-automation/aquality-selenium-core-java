package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementCacheHandler;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

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

    private final IElementCacheHandler elementCacheHandler;

    public CachedElementStateProvider(By locator, IConditionalWait conditionalWait, IElementCacheHandler elementCacheHandler) {
        super(locator, conditionalWait);
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
            return predicate.test(elementCacheHandler.getElement(ZERO_TIMEOUT, ElementState.EXISTS_IN_ANY_STATE));
        } catch (Exception exception) {
            if (handledExceptions.contains(exception.getClass())) {
                return false;
            }
            throw exception;
        }
    }

    protected boolean waitForCondition(BooleanSupplier condition, String conditionName, Long timeout) {
        boolean result = conditionalWait.waitFor(condition, timeout, null);
        if (!result) {
            String timeoutString = timeout == null ? "" : String.format("of %1$s seconds", timeout);
            String message = String.format(
                    "Element %1$s has not become %2$s after timeout %3$s", locator, conditionName.toUpperCase(), timeoutString);
            Logger.getInstance().warn(message);
        }
        return result;
    }

    @Override
    public boolean isClickable() {
        return tryInvokeFunction(elementClickable().getElementStateCondition());
    }

    @Override
    public void waitForClickable(Long timeout) {
        String errorMessage = String.format("Element %1$s has not become clickable after timeout.", locator);
        try {
            conditionalWait.waitForTrue(this::isClickable, timeout, null, errorMessage);
        } catch (TimeoutException e) {
            throw new org.openqa.selenium.TimeoutException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isDisplayed() {
        return !elementCacheHandler.isStale() && tryInvokeFunction(WebElement::isDisplayed);
    }

    @Override
    public boolean waitForDisplayed(Long timeout) {
        return waitForCondition(() -> tryInvokeFunction(WebElement::isDisplayed), ElementState.DISPLAYED.toString(), timeout);
    }

    @Override
    public boolean waitForNotDisplayed(Long timeout) {
        return waitForCondition(() -> tryInvokeFunction(element -> !element.isDisplayed()), "invisible or absent", timeout);
    }

    @Override
    public boolean isExist() {
        return !elementCacheHandler.isStale() && tryInvokeFunction(element -> true);
    }

    @Override
    public boolean waitForExist(Long timeout) {
        return waitForCondition(() -> tryInvokeFunction(element -> true), ElementState.EXISTS_IN_ANY_STATE.toString(), timeout);
    }

    @Override
    public boolean waitForNotExist(Long timeout) {
        return waitForCondition(() -> !isExist(), "absent", timeout);
    }

    @Override
    public boolean isEnabled() {
        return tryInvokeFunction(elementEnabled().getElementStateCondition(), Collections.singletonList(StaleElementReferenceException.class));
    }

    @Override
    public boolean waitForEnabled(Long timeout) {
        return waitForCondition(this::isEnabled, elementEnabled().getStateName(), timeout);
    }

    @Override
    public boolean waitForNotEnabled(Long timeout) {
        return waitForCondition(() -> !isEnabled(), elementNotEnabled().getStateName(), timeout);
    }
}

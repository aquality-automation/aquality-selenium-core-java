package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.function.Predicate;

public class ElementStateProvider implements IElementStateProvider {

    private static final long ZERO_TIMEOUT = 0L;
    private final By locator;
    private final IConditionalWait conditionalWait;
    private final IElementFinder elementFinder;

    ElementStateProvider(By locator, IConditionalWait conditionalWait, IElementFinder elementFinder) {
        this.locator = locator;
        this.conditionalWait = conditionalWait;
        this.elementFinder = elementFinder;
    }

    @Override
    public boolean isClickable() {
        return waitForClickable(ZERO_TIMEOUT, true);
    }

    @Override
    public void waitForClickable(Long timeout) {
        waitForClickable(timeout, false);
    }

    private boolean waitForClickable(Long timeout, boolean catchTimeoutException) {
        DesiredState desiredState =
                new DesiredState(webElement -> webElement.isDisplayed() && webElement.isEnabled(), "CLICKABLE");
        desiredState = catchTimeoutException ? desiredState.withCatchingTimeoutException() : desiredState;
        return isElementInDesiredCondition(desiredState, timeout);
    }

    private boolean isElementInDesiredCondition(DesiredState elementStateCondition, Long timeout) {
        return !elementFinder.findElements(locator, elementStateCondition, timeout).isEmpty();
    }

    @Override
    public boolean isDisplayed() {
        return waitForDisplayed(ZERO_TIMEOUT);
    }

    @Override
    public boolean waitForDisplayed(Long timeout) {
        return isAnyElementFound(timeout, ElementState.DISPLAYED);
    }

    private boolean isAnyElementFound(Long timeout, ElementState state) {
        return !elementFinder.findElements(locator, state, timeout).isEmpty();
    }

    @Override
    public boolean waitForNotDisplayed(Long timeout) {
        return conditionalWait.waitFor(() -> !isDisplayed(), timeout, null);
    }

    @Override
    public boolean isExist() {
        return waitForExist(ZERO_TIMEOUT);
    }

    @Override
    public boolean waitForExist(Long timeout) {
        return isAnyElementFound(timeout, ElementState.EXISTS_IN_ANY_STATE);
    }

    @Override
    public boolean waitForNotExist(Long timeout) {
        return conditionalWait.waitFor(() -> !isExist(), timeout, null);
    }

    @Override
    public boolean isEnabled() {
        return waitForEnabled(ZERO_TIMEOUT);
    }

    @Override
    public boolean waitForEnabled(Long timeout) {
        return isElementInDesiredCondition(this::isElementEnabled, "ENABLED", timeout);
    }

    protected boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    private boolean isElementInDesiredCondition(Predicate<WebElement> condition, String stateName, Long timeout) {
        DesiredState desiredState = new DesiredState(condition, stateName)
                .withCatchingTimeoutException()
                .withThrowingNoSuchElementException();
        return isElementInDesiredCondition(desiredState, timeout);
    }

    @Override
    public boolean waitForNotEnabled(Long timeout) {
        return isElementInDesiredCondition(this::isElementEnabled, "NOT ENABLED", timeout);
    }
}

package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;

import java.time.Duration;

public class DefaultElementStateProvider extends ElementStateProvider {

    private final By locator;
    private final IConditionalWait conditionalWait;
    private final IElementFinder elementFinder;

    public DefaultElementStateProvider(By locator, IConditionalWait conditionalWait, IElementFinder elementFinder) {
        this.locator = locator;
        this.conditionalWait = conditionalWait;
        this.elementFinder = elementFinder;
    }

    @Override
    public boolean isClickable() {
        return waitForIsClickable(Duration.ZERO, true);
    }

    @Override
    public void waitForClickable(Duration timeout) {
        waitForIsClickable(timeout, false);
    }

    private boolean waitForIsClickable(Duration timeout, boolean catchTimeoutException) {
        DesiredState desiredState = elementClickable();
        desiredState = catchTimeoutException ? desiredState.withCatchingTimeoutException() : desiredState;
        return isElementInDesiredCondition(desiredState, timeout);
    }

    private boolean isElementInDesiredCondition(DesiredState elementStateCondition, Duration timeout) {
        return !elementFinder.findElements(locator, elementStateCondition, timeout).isEmpty();
    }

    @Override
    public boolean isDisplayed() {
        return waitForDisplayed(Duration.ZERO);
    }

    @Override
    public boolean waitForDisplayed(Duration timeout) {
        return isAnyElementFound(timeout, ElementState.DISPLAYED);
    }

    private boolean isAnyElementFound(Duration timeout, ElementState state) {
        return !elementFinder.findElements(locator, state, timeout).isEmpty();
    }

    @Override
    public boolean waitForNotDisplayed(Duration timeout) {
        return conditionalWait.waitFor(() -> !isDisplayed(), timeout);
    }

    @Override
    public boolean isExist() {
        return waitForExist(Duration.ZERO);
    }

    @Override
    public boolean waitForExist(Duration timeout) {
        return isAnyElementFound(timeout, ElementState.EXISTS_IN_ANY_STATE);
    }

    @Override
    public boolean waitForNotExist(Duration timeout) {
        return conditionalWait.waitFor(() -> !isExist(), timeout);
    }

    @Override
    public boolean isEnabled() {
        return waitForEnabled(Duration.ZERO);
    }

    @Override
    public boolean waitForEnabled(Duration timeout) {
        return isElementInDesiredCondition(elementEnabled(), timeout);
    }

    @Override
    public boolean waitForNotEnabled(Duration timeout) {
        return isElementInDesiredCondition(elementNotEnabled(), timeout);
    }
}

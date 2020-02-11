package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;

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
        return waitForIsClickable(getZeroTImeout(), true);
    }

    @Override
    public void waitForClickable(Long timeout) {
        waitForIsClickable(timeout, false);
    }

    private boolean waitForIsClickable(Long timeout, boolean catchTimeoutException) {
        DesiredState desiredState = elementClickable();
        desiredState = catchTimeoutException ? desiredState.withCatchingTimeoutException() : desiredState;
        return isElementInDesiredCondition(desiredState, timeout);
    }

    private boolean isElementInDesiredCondition(DesiredState elementStateCondition, Long timeout) {
        return !elementFinder.findElements(locator, elementStateCondition, timeout).isEmpty();
    }

    @Override
    public boolean isDisplayed() {
        return waitForDisplayed(getZeroTImeout());
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
        return waitForExist(getZeroTImeout());
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
        return waitForEnabled(getZeroTImeout());
    }

    @Override
    public boolean waitForEnabled(Long timeout) {
        return isElementInDesiredCondition(elementEnabled(), timeout);
    }

    @Override
    public boolean waitForNotEnabled(Long timeout) {
        return isElementInDesiredCondition(elementNotEnabled(), timeout);
    }
}

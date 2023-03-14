package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.logging.ILogElementState;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;

import java.time.Duration;
import java.util.function.BooleanSupplier;

public class DefaultElementStateProvider extends ElementStateProvider {

    private final By locator;
    private final IConditionalWait conditionalWait;
    private final IElementFinder elementFinder;

    public DefaultElementStateProvider(By locator, IConditionalWait conditionalWait, IElementFinder elementFinder,
                                       ILogElementState logger) {
        super(logger);
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
        try {
            waitForIsClickable(timeout, false);
        } catch (Exception e) {
            logElementState("loc.wait.for.state.failed", elementClickable().getStateName());
            throw e;
        }
    }

    private boolean waitForIsClickable(Duration timeout, boolean catchTimeoutException) {
        DesiredState desiredState = elementClickable();
        desiredState = catchTimeoutException ? desiredState.withCatchingTimeoutException() : desiredState;
        return isElementInDesiredCondition(desiredState, timeout);
    }

    private boolean isElementInDesiredCondition(DesiredState elementStateCondition, Duration timeout) {
        return doAndLogWaitForState(
                () -> !elementFinder.findElements(locator, elementStateCondition, timeout).isEmpty(),
                elementStateCondition.getStateName(),
                timeout);
    }

    @Override
    public boolean isDisplayed() {
        return waitForDisplayed(Duration.ZERO);
    }

    @Override
    public boolean waitForDisplayed(Duration timeout) {
        return doAndLogWaitForState(
                () -> isAnyElementFound(timeout, ElementState.DISPLAYED),
                "displayed",
                timeout);
    }

    private boolean isAnyElementFound(Duration timeout, ElementState state) {
        return !elementFinder.findElements(locator, state, timeout).isEmpty();
    }

    @Override
    public boolean waitForNotDisplayed(Duration timeout) {
        return doAndLogWaitForState(
                () -> conditionalWait.waitFor(() -> !isDisplayed(), timeout),
                "not.displayed",
                timeout);
    }

    @Override
    public boolean isExist() {
        return waitForExist(Duration.ZERO);
    }

    @Override
    public boolean waitForExist(Duration timeout) {
        return doAndLogWaitForState(
                () -> isAnyElementFound(timeout, ElementState.EXISTS_IN_ANY_STATE),
                "exist",
                timeout);
    }

    @Override
    public boolean waitForNotExist(Duration timeout) {
        return doAndLogWaitForState(
                () -> conditionalWait.waitFor(() -> !isExist(), timeout),
                "not.exist",
                timeout);
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

    private boolean doAndLogWaitForState(BooleanSupplier waitingAction, String conditionKeyPart, Duration timeout)
    {
        if (Duration.ZERO.equals(timeout))
        {
            return waitingAction.getAsBoolean();
        }

        logElementState("loc.wait.for.state", conditionKeyPart);
        boolean result = waitingAction.getAsBoolean();
        if (!result)
        {
            logElementState("loc.wait.for.state.failed", conditionKeyPart);
        }

        return result;
    }
}

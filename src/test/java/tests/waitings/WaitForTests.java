package tests.waitings;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class WaitForTests extends BaseConditionalWaitTest {

    @DataProvider(name = "falseWaitForAction", parallel = true)
    public Object[][] falseWaitForAction() {
        return getDataProvider(() -> false);
    }

    @Test(dataProvider = "falseWaitForAction")
    public void testFalseShouldBeReturnedIfConditionIsNotMetAndTimeoutIsOver(BooleanSupplier waitAction, Duration timeout, Duration pollingInterval) {
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        double accuracyPollingInterval = timeout.getSeconds() + pollingInterval.getSeconds() + accuracy;
        assertFalse(result, "waitFor should return false when condition is not satisfied.");
        assertTrue(duration >= timeout.getSeconds() && duration < accuracyPollingInterval,
                String.format("Duration '%s' should be between '%s' and '%s' (timeout  and (timeout + pollingInterval + accuracy)) when condition is not satisfied.",
                        duration, timeout, accuracyPollingInterval));
    }

    @DataProvider(name = "trueWaitForAction", parallel = true)
    public Object[][] trueWaitForAction() {
        return getDataProvider(() -> true);
    }

    @Test(dataProvider = "trueWaitForAction")
    public void testTrueShouldBeReturnedIfConditionIsMetAndTimeoutIsNotOver(BooleanSupplier waitAction, Duration timeout, Duration pollingInterval) {
        double checkedTimeout = pollingInterval.getSeconds() + accuracy;
        checkWaitForMethodForPassedCondition(waitAction, checkedTimeout);
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithoutCustomParameters() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithExceptions = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), ignoredExceptions);
        double checkedTimeout = (double) timeoutConfiguration.getPollingInterval().getSeconds() * 2 + accuracy;
        checkWaitForMethodForPassedCondition(actionWithExceptions, checkedTimeout);
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithDefaultTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithMessageAndExceptions = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), "Condition should be true", ignoredExceptions);
        double checkedTimeout = (double) timeoutConfiguration.getPollingInterval().getSeconds() * 2 + accuracy;
        checkWaitForMethodForPassedCondition(actionWithMessageAndExceptions, checkedTimeout);
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeoutAndException() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, ignoredExceptions);
        double checkedTimeout = (double) waitForTimeoutPolling.getSeconds() * 2 + accuracy;
        checkWaitForMethodForPassedCondition(actionWithAllParameters, checkedTimeout);
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", ignoredExceptions);
        double checkedTimeout = (double) waitForTimeoutPolling.getSeconds() * 2 + accuracy;
        checkWaitForMethodForPassedCondition(actionWithAllParameters, checkedTimeout);
    }

    private void checkWaitForMethodForPassedCondition(BooleanSupplier waitAction, double checkedTimeout) {
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        assertTrue(result, "waitFor should return true when condition is satisfied.");
        assertTrue(duration < checkedTimeout,
                String.format("Duration '%s' should be less than timeout '%s'",
                        duration, checkedTimeout));
    }

    @DataProvider(name = "throwExceptionAction", parallel = true)
    public Object[][] throwExceptionAction() {
        BooleanSupplier throwEx = () -> {
            throw new StaleElementReferenceException("");
        };
        return getDataProvider(throwEx);
    }

    @Test(dataProvider = "throwExceptionAction", expectedExceptions = StaleElementReferenceException.class)
    public void testShouldThrowException(BooleanSupplier waitAction, Duration timeout, Duration pollingInterval) {
        waitAction.getAsBoolean();
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullCannotBePassedAsCondition() {
        conditionalWait.waitFor((BooleanSupplier) null, "Condition should not be null");
    }

    private Object[][] getDataProvider(BooleanSupplier action) {
        BooleanSupplier onlyAction = () -> conditionalWait.waitFor(action);
        BooleanSupplier actionWithMessage = () -> conditionalWait.waitFor(action, "Condition should be true");
        BooleanSupplier actionWithExceptions = () -> conditionalWait.waitFor(action, Collections.emptyList());
        BooleanSupplier actionWithMessageAndExceptions = () -> conditionalWait.waitFor(action, "Condition should be true", Collections.emptyList());
        BooleanSupplier actionWithCustomTimeouts = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling);
        BooleanSupplier actionWithCustomTimeoutsAndMessage = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true");
        BooleanSupplier actionWithCustomTimeoutsAndExceptions = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling, Collections.emptyList());
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", Collections.emptyList());
        return new Object[][]{
                {onlyAction, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithMessage, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithExceptions, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithMessageAndExceptions, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithCustomTimeouts, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithCustomTimeoutsAndMessage, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithCustomTimeoutsAndExceptions, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithAllParameters, waitForTimeoutCondition, waitForTimeoutPolling},
        };
    }
}

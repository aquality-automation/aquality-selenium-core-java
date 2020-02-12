package tests.waitings;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
    public void testFalseShouldBeReturnedIfConditionIsNotMetAndTimeoutIsOver(BooleanSupplier waitAction, long timeout) {
        Date startTime = new Date();
        boolean result = waitAction.getAsBoolean();
        long duration = (new Date().getTime() - startTime.getTime()) / 1000;
        long interval = 2 * timeout + accuracy;
        assertFalse(result, "waitFor should return false when condition is not satisfied.");
        assertTrue(duration >= timeout && duration < interval,
                String.format("Duration '%s' should be between '%s' and '%s' (timeout  and (2*timeout + accuracy)) when condition is not satisfied. ",
                        duration, timeout, interval));
    }

    @DataProvider(name = "trueWaitForAction", parallel = true)
    public Object[][] trueWaitForAction() {
        return getDataProvider(() -> true);
    }

    @Test(dataProvider = "trueWaitForAction")
    public void testTrueShouldBeReturnedIfConditionIsMetAndTimeoutIsNotOver(BooleanSupplier waitAction, long timeout) {
        checkWaitForMethodForPassedCondition(waitAction, timeout);
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithoutCustomParameters() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithExceptions = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithExceptions, timeoutConfiguration.getCondition().getSeconds());
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithDefaultTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithMessageAndExceptions = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), "Condition should be true", ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithMessageAndExceptions, timeoutConfiguration.getCondition().getSeconds());
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeoutAndException() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithAllParameters, waitForTimeoutCondition);
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithAllParameters, waitForTimeoutCondition);
    }

    private void checkWaitForMethodForPassedCondition(BooleanSupplier waitAction, long timeout) {
        long accuracyTimeout = timeout + accuracy;
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        assertTrue(result, "waitFor should return true when condition is satisfied.");
        assertTrue(duration <= timeout,
                String.format("Duration '%s' should be less than accuracyTimeout '%s'",
                        duration, accuracyTimeout));
    }

    @DataProvider(name = "throwExceptionAction", parallel = true)
    public Object[][] throwExceptionAction() {
        BooleanSupplier throwEx = () -> {
            throw new StaleElementReferenceException("");
        };
        return getDataProvider(throwEx);
    }

    @Test(dataProvider = "throwExceptionAction", expectedExceptions = StaleElementReferenceException.class)
    public void testShouldThrowException(BooleanSupplier waitAction, long timeout) {
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
                {onlyAction, timeoutConfiguration.getCondition().getSeconds()},
                {actionWithMessage, timeoutConfiguration.getCondition().getSeconds()},
                {actionWithExceptions, timeoutConfiguration.getCondition().getSeconds()},
                {actionWithMessageAndExceptions, timeoutConfiguration.getCondition().getSeconds()},
                {actionWithCustomTimeouts, waitForTimeoutCondition},
                {actionWithCustomTimeoutsAndMessage, waitForTimeoutCondition},
                {actionWithCustomTimeoutsAndExceptions, waitForTimeoutCondition},
                {actionWithAllParameters, waitForTimeoutCondition},
        };
    }
}

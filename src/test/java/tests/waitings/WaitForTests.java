package tests.waitings;

import org.testng.annotations.Test;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class WaitForTests extends BaseConditionalWaitTest {

    @Test
    public void testFalseShouldBeReturnedIfConditionIsNotMetAndDefaultTimeoutIsOver() {
        checkWaitForMethodForFailedCondition(
                () -> conditionalWait.waitFor(() -> false, "Condition should be true"),
                timeoutConfiguration.getCondition());
    }

    @Test
    public void testFalseShouldBeReturnedIfConditionIsNotMetAndCustomTimeoutIsOver() {
        checkWaitForMethodForFailedCondition(
                () -> conditionalWait.waitFor(() -> false, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true"),
                waitForTimeoutCondition);
    }

    private void checkWaitForMethodForFailedCondition(BooleanSupplier waitAction, long timeout) {
        Date startTime = new Date();
        boolean result = waitAction.getAsBoolean();
        long duration = (new Date().getTime() - startTime.getTime())/1000;
        assertFalse(result, "waitFor should return false when condition is not satisfied.");
        assertTrue(duration > timeout - ACCURANCY && duration < 2 * timeout + ACCURANCY,
                String.format("Duration '%s' should be between timeout '%s' and 2*timeoutCondition when condition is not satisfied.",
                        duration, timeout));
    }

    @Test
    public void testTrueShouldBeReturnedIfConditionIsMetAndDefaultTimeoutIsNotOver() {
        checkWaitForMethodForPassedCondition(
                () -> conditionalWait.waitFor(() -> true, "Condition should be true"),
                timeoutConfiguration.getCondition());
    }

    @Test
    public void testTrueShouldBeReturnedIfConditionIsMetAndCustomTimeoutIsNotOver() {
        checkWaitForMethodForPassedCondition(
                () -> conditionalWait.waitFor(() -> true, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true"),
                waitForTimeoutCondition);
    }

    @Test
    public void testWaitForWithDefaultTimeoutsShouldCatchExceptions() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkWaitForMethodForPassedCondition(
                () -> conditionalWait.waitFor(() -> {
                    if (atomicBoolean.get()) {
                        atomicBoolean.set(false);
                        throw new IllegalArgumentException("");
                    }

                    return true;
                }, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval(), "Condition should be true"),
                timeoutConfiguration.getCondition());
    }

    @Test
    public void testWaitForCatchExceptions() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkWaitForMethodForPassedCondition(
                () -> conditionalWait.waitFor(() -> {
                    if (atomicBoolean.get()) {
                        atomicBoolean.set(false);
                        throw new IllegalArgumentException("");
                    }

                    return true;
                }, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true"),
                waitForTimeoutCondition);
    }

    private void checkWaitForMethodForPassedCondition(BooleanSupplier waitAction, long timeout) {
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        assertTrue(result, "waitFor should return true when condition is satisfied.");
        assertTrue(duration < timeout,
                String.format("Duration '%s' should be leas than timeoutCondition '%s'",
                        duration, timeout));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullCannotBePassedAsCondition() {
        conditionalWait.waitFor((BooleanSupplier) null, "Condition should not be null");
    }
}

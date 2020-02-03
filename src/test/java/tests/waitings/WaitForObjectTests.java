package tests.waitings;

import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class WaitForObjectTests extends BaseConditionalWaitTest {

    private static final String RESULT_STRING = "result";

    @BeforeMethod
    public void startApp() {
        application.get().getDriver();
    }

    @AfterMethod
    public void quit() {
        if (application.get().isStarted()) {
            application.get().getDriver().quit();
        }
    }

    @DataProvider(name = "failWaitForAction", parallel = true)
    public Object[][] successWaitForAction1() {
        return getDataProvider((app) -> false);
    }

    @Test(dataProvider = "successWaitForAction")
    public void testShouldThrowTimeoutExceptionIfConditionIsNotMetAndTimeoutIsOver(Callable failedAction, long timeout) throws Exception {
        timer.get().start();
        try {
            failedAction.call();
        } catch (TimeoutException e) {
            double duration = timer.get().stop();
            long interval = 2 * timeout + accuracy;
            assertTrue(duration >= timeout && duration < interval,
                    String.format("Duration '%s' should be between '%s' and '%s' (timeout  and (2*timeout + accuracy)) when condition is not satisfied. ",
                            duration, timeout, interval));
        }
    }

    @DataProvider(name = "successWaitForAction", parallel = true)
    public Object[][] successWaitForAction() {
        return getDataProvider((app) -> RESULT_STRING);
    }

    @Test(dataProvider = "successWaitForAction")
    public void testShouldReturnAnObjectIfConditionIsMetAndTimeoutIsNotOver(Callable<String> successAction, long timeout) throws Exception {
        timer.get().start();
        String result = successAction.call();
        double duration = timer.get().stop();
        assertTrue(duration <= timeout,
                String.format("Duration '%s' should be less than accuracyTimeout '%s'",
                        duration, timeout));
        assertEquals(result, RESULT_STRING, "Method should return correct object");
    }

    @DataProvider(name = "throwWaitForAction", parallel = true)
    public Object[][] throwWaitForAction() {
        return getDataProvider((app) -> {
            throw new IllegalArgumentException("I am exception");
        });
    }

    @Test(dataProvider = "throwWaitForAction")
    public void testShouldThrowException(Callable<String> throwAction, long timeout) throws Exception {
        try {
            timer.get().start();
            throwAction.call();
        } catch (IllegalArgumentException e) {
            double duration = timer.get().stop();
            assertTrue(duration <= timeout,
                    String.format("Duration '%s' should be less than accuracyTimeout '%s'",
                            duration, timeout));
            assertEquals(e.getMessage(), "I am exception", "It should be custom exception");
        }
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithoutCustomParameters() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithExceptions = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithExceptions, timeoutConfiguration.getCondition());
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithDefaultTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithMessageAndExceptions = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), "Condition should be true", ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithMessageAndExceptions, timeoutConfiguration.getCondition());
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeoutAndExceptions() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), waitForTimeoutCondition, waitForTimeoutPolling, ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithAllParameters, waitForTimeoutCondition);
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithAllParameters, waitForTimeoutCondition);
    }

    private void checkWaitForMethodForPassedCondition(BooleanSupplier waitAction, long timeout) {
        long accuracyTimeout = timeout + accuracy;
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        assertTrue(result, "waitFor should return true when condition is satisfied.");
        assertTrue(duration <= accuracyTimeout,
                String.format("Duration '%s' should be less than accuracyTimeout '%s'",
                        duration, accuracyTimeout));
    }

    private Object[][] getDataProvider(ExpectedCondition<Object> action) {
        Callable onlyAction = () -> conditionalWait.waitFor(action);
        Callable actionWithMessage = () -> conditionalWait.waitFor(action, "Condition should be true");
        Callable actionWithExceptions = () -> conditionalWait.waitFor(action, Collections.emptyList());
        Callable actionWithMessageAndExceptions = () -> conditionalWait.waitFor(action, "Condition should be true", Collections.emptyList());
        Callable actionWithCustomTimeouts = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling);
        Callable actionWithCustomTimeoutsAndMessage = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true");
        Callable actionWithCustomTimeoutsAndExceptions = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling, Collections.emptyList());
        Callable actionWithAllParameters = () -> conditionalWait.waitFor(action, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", Collections.emptyList());
        return new Object[][]{
                {onlyAction, timeoutConfiguration.getCondition()},
                {actionWithMessage, timeoutConfiguration.getCondition()},
                {actionWithExceptions, timeoutConfiguration.getCondition()},
                {actionWithMessageAndExceptions, timeoutConfiguration.getCondition()},
                {actionWithCustomTimeouts, waitForTimeoutCondition},
                {actionWithCustomTimeoutsAndMessage, waitForTimeoutCondition},
                {actionWithCustomTimeoutsAndExceptions, waitForTimeoutCondition},
                {actionWithAllParameters, waitForTimeoutCondition},
        };
    }
}

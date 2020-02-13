package tests.waitings;

import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Callable;
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
    public Object[][] failWaitForAction() {
        return getDataProvider((app) -> false);
    }

    @Test(dataProvider = "failWaitForAction")
    public void testShouldThrowTimeoutExceptionIfConditionIsNotMetAndTimeoutIsOver(Callable failedAction, Duration timeout, Duration pollingInterval) throws Exception {
        timer.get().start();
        try {
            failedAction.call();
            Assert.fail("TimeoutException should be thrown but not");
        } catch (TimeoutException e) {
            double duration = timer.get().stop();
            double interval = timeout.getSeconds() + pollingInterval.getSeconds() + accuracy;
            assertTrue(duration >= timeout.getSeconds() && duration < interval,
                    String.format("Duration '%s' should be between '%s' and '%s' (timeout  and (timeout + pollingInterval + accuracy)) when condition is not satisfied.",
                            duration, timeout, interval));
        }
    }

    @DataProvider(name = "successWaitForAction", parallel = true)
    public Object[][] successWaitForAction() {
        return getDataProvider((app) -> RESULT_STRING);
    }

    @Test(dataProvider = "successWaitForAction")
    public void testShouldReturnAnObjectIfConditionIsMetAndTimeoutIsNotOver(Callable<String> successAction, Duration timeout, Duration pollingInterval) throws Exception {
        timer.get().start();
        String result = successAction.call();
        double duration = timer.get().stop();
        double accuracyPollingInterval = pollingInterval.getSeconds() + accuracy;
        assertTrue(duration < accuracyPollingInterval,
                String.format("Duration '%s' should be less than accuracy polling interval '%s'",
                        duration, accuracyPollingInterval));
        assertEquals(result, RESULT_STRING, "Method should return correct object");
    }

    @DataProvider(name = "throwWaitForAction", parallel = true)
    public Object[][] throwWaitForAction() {
        return getDataProvider((app) -> {
            throw new IllegalArgumentException("I am exception");
        });
    }

    @Test(dataProvider = "throwWaitForAction")
    public void testShouldThrowException(Callable<String> throwAction, Duration timeout, Duration pollingInterval) throws Exception {
        try {
            timer.get().start();
            throwAction.call();
            Assert.fail("IllegalArgumentException should be thrown but not");
        } catch (IllegalArgumentException e) {
            double duration = timer.get().stop();
            double accuracyPollingInterval = pollingInterval.getSeconds() + accuracy;
            assertTrue(duration < accuracyPollingInterval,
                    String.format("Duration '%s' should be less than accuracy polling interval '%s'",
                            duration, accuracyPollingInterval));
            assertEquals(e.getMessage(), "I am exception", "It should be custom exception");
        }
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithoutCustomParameters() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithExceptions = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithExceptions, timeoutConfiguration.getPollingInterval());
    }

    @Test
    public void testShouldIgnoreExceptionForWaitingWithDefaultTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithMessageAndExceptions = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), "Condition should be true", ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithMessageAndExceptions, timeoutConfiguration.getPollingInterval());
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeoutAndExceptions() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), waitForTimeoutCondition, waitForTimeoutPolling, ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithAllParameters, waitForTimeoutPolling);
    }

    @Test
    public void testShouldIgnoreExceptionWaitingWithCustomTimeout() {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        BooleanSupplier actionWithAllParameters = () -> conditionalWait.waitFor((driver) -> throwNewException(atomicBoolean).getAsBoolean(), waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", ignoredExceptions);
        checkWaitForMethodForPassedCondition(actionWithAllParameters, waitForTimeoutPolling);
    }

    private void checkWaitForMethodForPassedCondition(BooleanSupplier waitAction, Duration pollingInterval) {
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        double doubleAccuracyPollingInterval = 2 * pollingInterval.getSeconds() + accuracy;
        assertTrue(result, "waitFor should return true when condition is satisfied.");
        assertTrue(duration < doubleAccuracyPollingInterval,
                String.format("Duration '%s' should be less than double accuracy polling interval '%s'",
                        duration, doubleAccuracyPollingInterval));
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
                {onlyAction, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithMessage, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithExceptions, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithMessageAndExceptions, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithCustomTimeouts, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithCustomTimeoutsAndMessage, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithCustomTimeoutsAndExceptions, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithAllParameters, waitForTimeoutCondition, waitForTimeoutPolling}
        };
    }
}

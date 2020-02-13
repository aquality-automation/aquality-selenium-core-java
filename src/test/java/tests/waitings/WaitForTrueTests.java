package tests.waitings;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertTrue;

public class WaitForTrueTests extends BaseConditionalWaitTest {

    @DataProvider(name = "falseWaitForTrueAction", parallel = true)
    public Object[][] falseWaitForAction() {
        return getDataProvider(() -> false);
    }

    @Test(dataProvider = "falseWaitForTrueAction")
    public void testTimeoutExceptionShouldBeThrownIfConditionIsMetAndTimeoutIsOver(Callable waitForTrueAction, Duration timeout, Duration pollingInterval) throws Exception {
        timer.get().start();
        try {
            waitForTrueAction.call();
            Assert.fail("TimeoutException should be thrown but not");
        } catch (TimeoutException e) {
            double duration = timer.get().stop();
            double accuracyPollingInterval = timeout.getSeconds() + pollingInterval.getSeconds() + accuracy;
            assertTrue(duration >= timeout.getSeconds() && duration < accuracyPollingInterval,
                    String.format("Duration '%s' should be between '%s' and '%s' (timeout  and (timeout + pollingInterval + accuracy)) when condition is not satisfied.",
                            duration, timeout, accuracyPollingInterval));
        }
    }

    @DataProvider(name = "successWaitForAction", parallel = true)
    public Object[][] successWaitForAction() {
        return getDataProvider(() -> true);
    }

    @Test(dataProvider = "successWaitForAction")
    public void testTimeoutExceptionShouldNotBeThrownIfConditionIsMetAndTimeoutIsNotOver(Callable waitForTrueAction, Duration timeout, Duration pollingInterval) throws Exception {
        timer.get().start();
        waitForTrueAction.call();
        double duration = timer.get().stop();
        double accuracyPollingInterval = pollingInterval.getSeconds() + accuracy;
        assertTrue(duration < accuracyPollingInterval,
                String.format("Duration '%s' should be less than accuracy polling interval '%s'", duration, accuracyPollingInterval));
    }

    @DataProvider(name = "throwExceptionAction", parallel = true)
    public Object[][] throwExceptionAction() {
        BooleanSupplier throwEx = () -> {
            throw new StaleElementReferenceException("");
        };
        return getDataProvider(throwEx);
    }

    @Test(dataProvider = "throwExceptionAction")
    public void testCustomExceptionShouldBeThrown(Callable waitForTrueAction, Duration timeout, Duration pollingInterval) throws Exception {

        try {
            timer.get().start();
            waitForTrueAction.call();
            Assert.fail("StaleElementReferenceException should be thrown but not");
        } catch (StaleElementReferenceException e) {
            double duration = timer.get().stop();
            double accuracyPollingInterval = pollingInterval.getSeconds() + accuracy;
            assertTrue(duration < accuracyPollingInterval,
                    String.format("Duration '%s' should be less than accuracy polling interval '%s'", duration, accuracyPollingInterval));
        }
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullCannotBePassedAsCondition() throws TimeoutException {
        conditionalWait.waitForTrue(null, "Condition should not be null");
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithoutCustomParameters() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), ignoredExceptions);
            return true;
        }, timeoutConfiguration.getPollingInterval());
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithDefaultTimeout() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), "Condition should be true", ignoredExceptions);
            return true;
        }, timeoutConfiguration.getPollingInterval());
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithCustomTimeoutAndException() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, ignoredExceptions);
            return true;
        }, waitForTimeoutPolling);
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithCustomTimeout() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", ignoredExceptions);
            return true;
        }, waitForTimeoutPolling);
    }

    private void checkExceptionIsIgnored(Callable waitForTrueAction, Duration pollingInterval) throws Exception {
        timer.get().start();
        waitForTrueAction.call();
        double duration = timer.get().stop();
        double doubleAccuracyPollingInterval = 2 * pollingInterval.getSeconds() + accuracy;
        assertTrue(duration < doubleAccuracyPollingInterval,
                String.format("Duration '%s' should be less than double accuracy polling interval '%s'",
                        duration, doubleAccuracyPollingInterval));
    }

    private Object[][] getDataProvider(BooleanSupplier action) {

        Callable onlyAction = () -> {
            conditionalWait.waitForTrue(action);
            return true;
        };

        Callable actionWithMessage = () -> {
            conditionalWait.waitForTrue(action, "Condition should be true");
            return true;
        };

        Callable actionWithExceptions = () -> {
            conditionalWait.waitForTrue(action, Collections.emptyList());
            return true;
        };

        Callable actionWithMessageAndExceptions = () -> {
            conditionalWait.waitForTrue(action, "Condition should be true", Collections.emptyList());
            return true;
        };

        Callable actionWithCustomTimeouts = () -> {
            conditionalWait.waitForTrue(action, waitForTimeoutCondition, waitForTimeoutPolling);
            return true;
        };

        Callable actionWithCustomTimeoutsAndMessage = () -> {
            conditionalWait.waitForTrue(action, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true");
            return true;
        };

        Callable actionWithCustomTimeoutsAndException = () -> {
            conditionalWait.waitForTrue(action, waitForTimeoutCondition, waitForTimeoutPolling, Collections.emptyList());
            return true;
        };

        Callable actionWithAllParameters = () -> {
            conditionalWait.waitForTrue(action, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", Collections.emptyList());
            return true;
        };

        return new Object[][]{
                {onlyAction, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithMessage, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithExceptions, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithMessageAndExceptions, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval()},
                {actionWithCustomTimeouts, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithCustomTimeoutsAndMessage, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithCustomTimeoutsAndException, waitForTimeoutCondition, waitForTimeoutPolling},
                {actionWithAllParameters, waitForTimeoutCondition, waitForTimeoutPolling},
        };
    }
}

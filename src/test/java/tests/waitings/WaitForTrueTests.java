package tests.waitings;

import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

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
    public void testTimeoutExceptionShouldBeThrownIfConditionIsMetAndTimeoutIsOver(Callable waitForTrueAction, long timeout) throws Exception {
        timer.get().start();
        try {
            waitForTrueAction.call();
        } catch (TimeoutException e) {
            double duration = timer.get().stop();
            double interval = 2 * timeout + accuracy;
            assertTrue(duration >= timeout && duration < interval,
                    String.format("Duration '%s' should be between '%s' and '%s' (timeout  and (2*timeout + accuracy)) when condition is not satisfied.",
                            duration, timeout, interval));
        }
    }

    @DataProvider(name = "successWaitForAction", parallel = true)
    public Object[][] successWaitForAction() {
        return getDataProvider(() -> true);
    }

    @Test(dataProvider = "successWaitForAction")
    public void testTimeoutExceptionShouldNotBeThrownIfConditionIsMetAndTimeoutIsNotOver(Callable waitForTrueAction, long timeout) throws Exception {
        timer.get().start();
        waitForTrueAction.call();
        double duration = timer.get().stop();
        assertTrue(duration < timeout,
                String.format("Duration '%s' should be less than timeout '%s' when condition is satisfied.",
                        duration, timeout));
    }

    @DataProvider(name = "throwExceptionAction", parallel = true)
    public Object[][] throwExceptionAction() {
        BooleanSupplier throwEx = () -> {
            throw new StaleElementReferenceException("");
        };
        return getDataProvider(throwEx);
    }

    @Test(dataProvider = "throwExceptionAction", expectedExceptions = StaleElementReferenceException.class)
    public void testCustomExceptionShouldBeThrown(Callable waitForTrueAction, long timeout) throws Exception {
        timer.get().start();
        waitForTrueAction.call();
        double duration = timer.get().stop();
        assertTrue(duration < timeout,
                String.format("Duration '%s' should be less than timeout '%s' when condition is satisfied.",
                        duration, timeout));
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
        }, timeoutConfiguration.getCondition());
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithDefaultTimeout() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), "Condition should be true", ignoredExceptions);
            return true;
        }, timeoutConfiguration.getCondition());
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithCustomTimeoutAndException() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, ignoredExceptions);
            return true;
        }, waitForTimeoutCondition);
    }

    @Test
    public void testCustomExceptionShouldBeIgnoredWithCustomTimeout() throws Exception {
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        checkExceptionIsIgnored(() -> {
            conditionalWait.waitForTrue(throwNewException(atomicBoolean), waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true", ignoredExceptions);
            return true;
        }, waitForTimeoutCondition);
    }

    private void checkExceptionIsIgnored(Callable waitForTrueAction, long timeout) throws Exception {
        timer.get().start();
        waitForTrueAction.call();
        double duration = timer.get().stop();
        assertTrue(duration < timeout,
                String.format("Duration '%s' should be less than timeout '%s' when condition is satisfied.",
                        duration, timeout));
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
            conditionalWait.waitFor(action, "Condition should be true", Collections.emptyList());
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
                {onlyAction, timeoutConfiguration.getCondition()},
                {actionWithMessage, timeoutConfiguration.getCondition()},
                {actionWithExceptions, timeoutConfiguration.getCondition()},
                {actionWithMessageAndExceptions, timeoutConfiguration.getCondition()},
                {actionWithCustomTimeouts, waitForTimeoutCondition},
                {actionWithCustomTimeoutsAndMessage, waitForTimeoutCondition},
                {actionWithCustomTimeoutsAndException, waitForTimeoutCondition},
                {actionWithAllParameters, waitForTimeoutCondition},
        };
    }
}

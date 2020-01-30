package tests.waitings;

import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import utils.Timer;

import java.util.Collections;
import java.util.concurrent.TimeoutException;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class ConditionalWaitTests {

    private static final long waitForTimeoutCondition = 10;
    private static final long waitForTimeoutPolling = 150;
    private ThreadLocal<Timer> timer = ThreadLocal.withInitial(Timer::new);
    private ITimeoutConfiguration timeoutConfiguration;
    private IConditionalWait conditionalWait;

    @BeforeMethod
    public void init() {
        timeoutConfiguration = CustomAqualityServices.getServiceProvider().getInstance(ITimeoutConfiguration.class);
        conditionalWait = CustomAqualityServices.getServiceProvider().getInstance(IConditionalWait.class);
    }

    @AfterMethod
    public void quitApplication() {
        CustomAqualityServices.getApplication().getDriver().quit();
    }

    @Test
    public void testFalseShouldBeReturnedIfConditionIsNotMetAndDefaultTimeoutIsOver() {
        long timeoutCondition = timeoutConfiguration.getCondition();
        boolean result = conditionalWait.waitForTrue(() ->
        {
            timer.get().start();
            return false;
        }, "Condition should be true");
        double duration = timer.get().stop();
        assertFalse(result, "waitForTrue should return false when condition is not satisfied.");
        assertTrue(duration > timeoutCondition && duration < 2 * timeoutCondition,
                String.format("Duration '%s' should be between timeoutCondition '%s' and 2*timeoutCondition when condition is not satisfied.",
                        duration, timeoutCondition));
    }

    @Test
    public void testTimeoutExceptionShouldBeThrownIfConditionIsMetAndTimeoutIsOver() {
        try {
            conditionalWait.waitForTrue(() ->
            {
                timer.get().start();
                return false;
            }, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true");
        } catch (TimeoutException e) {
            double duration = timer.get().stop();
            assertTrue(duration > waitForTimeoutCondition && duration < 2 * waitForTimeoutCondition,
                    String.format("Duration '%s' should be between waitForTimeoutCondition '%s' and 2*waitForTimeoutCondition when condition is not satisfied.",
                            duration, waitForTimeoutCondition));
        }
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfConditionIsMetAndDefaultTimeoutIsNotOver() {
        long timeoutCondition = timeoutConfiguration.getCondition();

        boolean result = conditionalWait.waitForTrue(() ->
        {
            timer.get().start();
            return true;
        }, "Timeout exception should not be thrown");
        double duration = timer.get().stop();
        assertTrue(result, "waitForTrue should return true when condition is satisfied.");
        assertTrue(duration < timeoutCondition,
                String.format("Duration '%s' should be less than timeoutCondition '%s' when condition is satisfied.",
                duration, timeoutCondition));
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfConditionMetAndTimeoutIsNotOver() throws TimeoutException {
        conditionalWait.waitForTrue(() ->
        {
            timer.get().start();
            return true;
        }, waitForTimeoutCondition, waitForTimeoutPolling, "Timeout exception should not be thrown");
        double duration = timer.get().stop();
        assertTrue(duration < waitForTimeoutCondition,
                String.format("Duration '%s' should be less than waitForTimeoutCondition '%s' when condition is satisfied.",
                        duration, waitForTimeoutCondition));
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testNullCannotBePassedAsCondition() {
        conditionalWait.waitForTrue(null, "Condition should not be null");
    }

    @Test
    public void testTimeoutExceptionShouldBeThrownIfDriverConditionIsNotMetAndDefaultTimeoutIsOver() {
        long timeoutCondition = timeoutConfiguration.getCondition();
        try {
            conditionalWait.waitFor((driver) ->
                    {
                        timer.get().start();
                        return false;
                    },
                    "Condition should be true");
        } catch (org.openqa.selenium.TimeoutException e) {
            double duration = timer.get().stop();
            assertTrue(duration > timeoutCondition && duration < 2 * timeoutCondition,
                    String.format("Duration '%s' before throwing should be between timeoutCondition '%s' and 2*timeoutCondition ",
                            duration, timeoutCondition));
        }
    }

    @Test
    public void testTimeoutExceptionShouldBeThrownIfDriverConditionIsNotMetAndTimeoutIsOver() {
        try {
            conditionalWait.waitFor((driver) ->
                    {
                        timer.get().start();
                        return false;
                    }, waitForTimeoutCondition, waitForTimeoutPolling,
                    "Conditional should be true", Collections.singleton(StaleElementReferenceException.class));

        } catch (org.openqa.selenium.TimeoutException e) {
            double duration = timer.get().stop();
            assertTrue(duration > waitForTimeoutCondition && duration < 2 * waitForTimeoutCondition,
                    String.format("Duration '%s' before throwing should be between waitForTimeoutCondition '%s' and 2*waitForTimeoutCondition",
                            duration, waitForTimeoutCondition));
        }
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfDriverConditionIsMetAndDefaultTimeoutIsNotOver() {
        conditionalWait.waitFor((driver) ->
                {
                    timer.get().start();
                    return true;
                },
                "Conditional should be true");
        double duration = timer.get().stop();
        assertTrue(duration < timeoutConfiguration.getCondition(),
                String.format("Duration '%s' should be less than condition timeout '%s' when condition is satisfied.",
                        duration, timeoutConfiguration.getCondition()));
    }

    @Test
    public void testExceptionShouldBeCaughtConditionIsMetAndDefaultTimeoutIsNotOver() {
        try {
            conditionalWait.waitFor((driver) ->
                    {
                        timer.get().start();
                        throw new IllegalArgumentException("I am exception");
                    }, waitForTimeoutCondition, waitForTimeoutPolling,
                    "Conditional should be true", Collections.singleton(IllegalArgumentException.class));
        } catch (org.openqa.selenium.TimeoutException e) {
            double duration = timer.get().stop();
            assertTrue(duration > waitForTimeoutCondition && duration < 2 * waitForTimeoutCondition,
                    String.format("Duration '%s' before throwing should be between waitForTimeoutCondition '%s' and 2*waitForTimeoutCondition",
                            duration, waitForTimeoutCondition));
        }
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfDriverConditionIsMetAndTimeoutIsNotOver() {
        conditionalWait.waitFor((driver) ->
                {
                    timer.get().start();
                    return true;
                }, waitForTimeoutCondition, waitForTimeoutPolling,
                "Conditional should be true", Collections.singleton(IllegalArgumentException.class));
        double duration = timer.get().stop();
        assertTrue(duration < waitForTimeoutCondition,
                String.format("Duration '%s' should be less than waitForTimeoutCondition '%s' when condition is satisfied.",
                        duration, waitForTimeoutCondition));
    }
}

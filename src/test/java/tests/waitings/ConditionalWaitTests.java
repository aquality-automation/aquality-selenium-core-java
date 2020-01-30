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
    private Timer timer;
    private ITimeoutConfiguration timeoutConfiguration;
    private IConditionalWait conditionalWait;

    @BeforeMethod
    public void init() {
        timeoutConfiguration = CustomAqualityServices.getServiceProvider().getInstance(ITimeoutConfiguration.class);
        conditionalWait = CustomAqualityServices.getServiceProvider().getInstance(IConditionalWait.class);
        timer = new Timer();
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
            timer.start();
            return false;
        }, "Condition should be true");
        double duration = timer.stop();

        assertFalse(result);
        assertTrue(duration > timeoutCondition && duration < 2 * timeoutCondition);
    }

    @Test
    public void testTimeoutExceptionShouldBeThrownIfConditionIsMetAndTimeoutIsOver() {
        try {
            conditionalWait.waitForTrue(() ->
            {
                timer.start();
                return false;
            }, waitForTimeoutCondition, waitForTimeoutPolling, "Condition should be true");
        } catch (TimeoutException e) {
            double duration = timer.stop();
            assertTrue(duration > waitForTimeoutCondition && duration < 2 * waitForTimeoutCondition);
        }
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfConditionIsMetAndDefaultTimeoutIsNotOver() {
        long timeoutCondition = timeoutConfiguration.getCondition();

        boolean result = conditionalWait.waitForTrue(() ->
        {
            timer.start();
            return true;
        }, "Timeout exception should not be thrown");
        double duration = timer.stop();

        assertTrue(result);
        assertTrue(duration < timeoutCondition);
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfConditionMetAndTimeoutIsNotOver() throws TimeoutException {
        conditionalWait.waitForTrue(() ->
        {
            timer.start();
            return true;
        }, waitForTimeoutCondition, waitForTimeoutPolling, "Timeout exception should not be thrown");
        double duration = timer.stop();

        assertTrue(duration < waitForTimeoutCondition);
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
                        timer.start();
                        return false;
                    },
                    "Condition should be true");

        } catch (org.openqa.selenium.TimeoutException e) {
            double duration = timer.stop();

            assertTrue(duration > timeoutCondition && duration < 2 * timeoutCondition);
        }
    }

    @Test
    public void testTimeoutExceptionShouldBeThrownIfDriverConditionIsNotMetAndTimeoutIsOver() {
        try {
            conditionalWait.waitFor((driver) ->
                    {
                        timer.start();
                        return false;
                    }, waitForTimeoutCondition, waitForTimeoutPolling,
                    "Conditional should be true", Collections.singleton(StaleElementReferenceException.class));

        } catch (org.openqa.selenium.TimeoutException e) {
            double duration = timer.stop();

            assertTrue(duration > waitForTimeoutCondition && duration < 2 * waitForTimeoutCondition);

        }
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfDriverConditionIsMetAndDefaultTimeoutIsNotOver() {
        conditionalWait.waitFor((driver) ->
                {
                    timer.start();
                    return true;
                },
                "Conditional should be true");
        double duration = timer.stop();

        assertTrue(duration < timeoutConfiguration.getCondition());
    }

    @Test
    public void testExceptionShouldBeCaughtConditionIsMetAndDefaultTimeoutIsNotOver() {
        try {
            conditionalWait.waitFor((driver) ->
                    {
                        timer.start();
                        throw new IllegalArgumentException("I am exception");
                    }, waitForTimeoutCondition, waitForTimeoutPolling,
                    "Conditional should be true", Collections.singleton(IllegalArgumentException.class));
        } catch (org.openqa.selenium.TimeoutException e) {
            double duration = timer.stop();

            assertTrue(duration > waitForTimeoutCondition && duration < 2 * waitForTimeoutCondition);
        }
    }

    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfDriverConditionIsMetAndTimeoutIsNotOver() {
        conditionalWait.waitFor((driver) ->
                {
                    timer.start();
                    return true;
                }, waitForTimeoutCondition, waitForTimeoutPolling,
                "Conditional should be true", Collections.singleton(IllegalArgumentException.class));
        double duration = timer.stop();

        assertTrue(duration < waitForTimeoutCondition);
    }
}

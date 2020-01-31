package tests.waitings;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.ConditionalWait;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import utils.Timer;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class WaitForObjectTests extends BaseConditionalWaitTest{

    private static final long waitForTimeoutCondition = 10;
    private static final long waitForTimeoutPolling = 150;
    private ThreadLocal<Timer> timer = ThreadLocal.withInitial(Timer::new);
    private ITimeoutConfiguration timeoutConfiguration;
    private Provider<IApplication> application;
    private ConditionalWait conditionalWait;

    @BeforeMethod
    public void init() {
        Injector serviceProvider = CustomAqualityServices.getServiceProvider();
        ILocalizationManager localizationManager = serviceProvider.getInstance(ILocalizationManager.class);
        application = serviceProvider.getProvider(IApplication.class);
        timeoutConfiguration = serviceProvider.getInstance(ITimeoutConfiguration.class);
        conditionalWait = new ConditionalWait(application, timeoutConfiguration, localizationManager);
    }

    @AfterMethod
    public void quitApplication() {
        application.get().getDriver().quit();
    }

    //--------- boolean WaitFor tests-------------//
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
        timer.get().start();
        boolean result = waitAction.getAsBoolean();
        double duration = timer.get().stop();
        assertFalse(result, "waitFor should return false when condition is not satisfied.");
        assertTrue(duration > timeout && duration < 2 * timeout,
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

    //--------- boolean WaitForTrue tests-------------//

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

    private void checkWaitForTrueMethodForFailedCondition(Callable waitForTrueAction, long timeout) {
        /*timer.get().start();
        try {
            waitForTrueAction.call();
        } catch (TimeoutException e) {
            double duration = timer.get().stop();
            assertTrue(duration > timeout && duration < 2 * timeout,
                    String.format("Duration '%s' should be between timeout '%s' and 2*timeout when condition is not satisfied.",
                            duration, timeout));
        }*/
    }


    @Test
    public void testTimeoutExceptionShouldNotBeThrownIfConditionIsMetAndDefaultTimeoutIsNotOver() {
        long timeoutCondition = timeoutConfiguration.getCondition();

        boolean result = conditionalWait.waitFor(() ->
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

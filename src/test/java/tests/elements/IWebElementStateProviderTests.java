package tests.elements;

import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.applications.browser.AqualityServices;
import tests.applications.browser.ITheInternetPageTest;
import theinternet.DynamicControlsForm;
import theinternet.DynamicLoadingForm;
import theinternet.TheInternetPage;

import java.time.Duration;
import java.util.function.Predicate;

import static utils.TimeUtil.calculateDuration;
import static utils.TimeUtil.getCurrentTime;

public interface IWebElementStateProviderTests extends ITheInternetPageTest {
    double OPERATION_TIME = 5;
    Duration CUSTOM_WAIT_TIME = Duration.ofSeconds(2L);
    By ABSENT_ELEMENT_LOCATOR = By.xpath("//div[@class='not exist element']");

    default DynamicControlsForm getDynamicControlsForm() {
        return new DynamicControlsForm(this::getBrowser, this::state);
    }

    default DynamicLoadingForm getDynamicLoadingForm() {
        return new DynamicLoadingForm(this::getBrowser, this::state);
    }

    IElementStateProvider state(By locator);

    default Duration getConditionTimeout() {
        return AqualityServices.get(ITimeoutConfiguration.class).getCondition();
    }

    default void checkWaitingTimeForInputState(Duration waitTime, Predicate<IElementStateProvider> notExpectedCondition) {
        long startTime = getCurrentTime();
        boolean isConditionSatisfied = notExpectedCondition.test(getDynamicControlsForm().inputState());
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isConditionSatisfied);
        Assert.assertTrue(duration >= waitTime.getSeconds() && duration <= (waitTime.getSeconds() + OPERATION_TIME));
    }

    @Test
    default void testElementShouldWaitForEnabledWithCustomTimeout() {
        Duration waitTime = CUSTOM_WAIT_TIME;
        checkWaitingTimeForInputState(waitTime, state -> state.waitForEnabled(waitTime));
    }

    @Test
    default void testElementShouldWaitForEnabledWithDefaultTimeout() {
        Duration waitTime = getConditionTimeout();
        checkWaitingTimeForInputState(waitTime, IElementStateProvider::waitForEnabled);
    }

    @Test
    default void testElementShouldWaitForNotEnabledWithCustomTimeout() {
        Duration waitTime = CUSTOM_WAIT_TIME;
        getDynamicControlsForm().clickEnable();
        getDynamicControlsForm().inputState().waitForEnabled();

        checkWaitingTimeForInputState(waitTime, state -> state.waitForNotEnabled(waitTime));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    default void testNoSuchShouldBeThrownForWaitEnabledIfElementNotFound() {
        state(ABSENT_ELEMENT_LOCATOR).waitForEnabled(CUSTOM_WAIT_TIME);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    default void testNoSuchShouldBeThrownForWaitNotEnabledIfElementNotFound() {
        state(ABSENT_ELEMENT_LOCATOR).waitForNotEnabled(CUSTOM_WAIT_TIME);
    }

    @Test
    default void testElementShouldWaitForNotEnabledWithDefaultTimeout() {
        Duration waitTime = getConditionTimeout();

        getDynamicControlsForm().clickEnable();
        getDynamicControlsForm().inputState().waitForEnabled();

        checkWaitingTimeForInputState(waitTime, IElementStateProvider::waitForNotEnabled);
    }

    @Test
    default void testTextBoxEnabled() {
        getDynamicControlsForm().clickEnable();
        getDynamicControlsForm().inputState().waitForEnabled();
        Assert.assertTrue(getDynamicControlsForm().inputState().isEnabled());
    }

    @Test
    default void testWaitInvisibility() {
        navigate(TheInternetPage.DYNAMIC_LOADING);

        getDynamicLoadingForm().startButtonState().waitForClickable();
        getDynamicLoadingForm().clickStart();

        getDynamicLoadingForm().loaderState().waitForDisplayed(getDynamicLoadingForm().getSmallTimeout());

        boolean status = getDynamicLoadingForm().loaderState().waitForNotDisplayed(getDynamicLoadingForm().getSmallTimeout());
        Assert.assertFalse(status);

        status = getDynamicLoadingForm().loaderState().waitForNotDisplayed();
        Assert.assertTrue(status);
    }

    @Test
    default void testWaitForExist() {
        navigate(TheInternetPage.DYNAMIC_LOADING);
        boolean status = getDynamicLoadingForm().loaderState().waitForExist(getDynamicLoadingForm().getTimeout());

        Assert.assertFalse(status);
        Assert.assertTrue(getDynamicLoadingForm().startButtonState().waitForExist());
    }

    @Test
    default void testShouldBePossibleToWaitElementNotExistsCustomTime() {
        Duration waitTime = CUSTOM_WAIT_TIME;
        getDynamicControlsForm().clickRemove();

        checkWaitingTimeForInputState(waitTime, inputState -> getDynamicControlsForm().checkboxState().waitForNotExist(waitTime));
    }

    @Test
    default void testShouldBePossibleToWaitElementNotExists() {
        Duration waitTime = getConditionTimeout();
        getDynamicControlsForm().clickRemove();

        long startTime = getCurrentTime();
        boolean isMissed = getDynamicControlsForm().checkboxState().waitForNotExist();
        double duration = calculateDuration(startTime);

        Assert.assertTrue(isMissed);
        Assert.assertTrue(duration < waitTime.getSeconds());
    }
}

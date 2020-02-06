package tests.elements;

import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.elements.ElementStateProvider;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.application.browser.AqualityServices;
import tests.application.browser.ITheInternetPageTest;
import theinternet.DynamicControlsForm;
import theinternet.DynamicLoadingForm;
import theinternet.TheInternetPage;

import java.util.function.Predicate;

import static utils.TimeUtil.calculateDuration;
import static utils.TimeUtil.getCurrentTime;

public class ElementStateProviderTests implements ITheInternetPageTest {
    private static final double OPERATION_TIME = 5;
    private static final long CUSTOM_WAIT_TIME = 2L;
    private static final By absentElementLocator = By.xpath("//div[@class='not exist element']");
    private final DynamicControlsForm dynamicControlsForm = new DynamicControlsForm(this::getBrowser, this::state);
    private final DynamicLoadingForm dynamicLoadingForm = new DynamicLoadingForm(this::getBrowser, this::state);

    private ElementStateProvider state(By locator) {
        return new ElementStateProvider(locator,
                AqualityServices.get(IConditionalWait.class),
                AqualityServices.get(IElementFinder.class));
    }

    private long getConditionTimeout() {
        return AqualityServices.get(ITimeoutConfiguration.class).getCondition();
    }

    @Test
    public void testElementShouldWaitForEnabledWithCustomTimeout() {
        long waitTime = CUSTOM_WAIT_TIME;
        checkWaitingTimeForInputState(waitTime, state -> state.waitForEnabled(waitTime));
    }

    @Test
    public void testElementShouldWaitForEnabledWithDefaultTimeout() {
        long waitTime = getConditionTimeout();
        checkWaitingTimeForInputState(waitTime, IElementStateProvider::waitForEnabled);
    }

    @Test
    public void testElementShouldWaitForNotEnabledWithCustomTimeout() {
        long waitTime = CUSTOM_WAIT_TIME;
        dynamicControlsForm.clickEnable();
        dynamicControlsForm.inputState().waitForEnabled();

        checkWaitingTimeForInputState(waitTime, state -> state.waitForNotEnabled(waitTime));
    }

    private void checkWaitingTimeForInputState(Long waitTime, Predicate<IElementStateProvider> notExpectedCondition) {
        long startTime = getCurrentTime();
        boolean isConditionSatisfied = notExpectedCondition.test(dynamicControlsForm.inputState());
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isConditionSatisfied);
        Assert.assertTrue(duration >= waitTime && duration <= (waitTime + OPERATION_TIME));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNoSuchShouldBeThrownForWaitEnabledIfElementNotFound() {
        state(absentElementLocator).waitForEnabled(CUSTOM_WAIT_TIME);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNoSuchShouldBeThrownForWaitNotEnabledIfElementNotFound() {
        state(absentElementLocator).waitForNotEnabled(CUSTOM_WAIT_TIME);
    }

    @Test
    public void testElementShouldWaitForNotEnabledWithDefaultTimeout() {
        long waitTime = getConditionTimeout();

        dynamicControlsForm.clickEnable();
        dynamicControlsForm.inputState().waitForEnabled();

        checkWaitingTimeForInputState(waitTime, IElementStateProvider::waitForNotEnabled);
    }

    @Test
    public void testTextBoxEnabled() {
        dynamicControlsForm.clickEnable();
        dynamicControlsForm.inputState().waitForEnabled();
        Assert.assertTrue(dynamicControlsForm.inputState().isEnabled());
    }

    @Test
    public void testWaitInvisibility() {
        navigate(TheInternetPage.DYNAMIC_LOADING);

        dynamicLoadingForm.startButtonState().waitForClickable();
        dynamicLoadingForm.clickStart();

        dynamicLoadingForm.loaderState().waitForDisplayed(dynamicLoadingForm.getSmallTimeout());

        boolean status = dynamicLoadingForm.loaderState().waitForNotDisplayed(dynamicLoadingForm.getSmallTimeout());
        Assert.assertFalse(status);

        status = dynamicLoadingForm.loaderState().waitForNotDisplayed();
        Assert.assertTrue(status);
    }

    @Test
    public void testWaitForExist() {
        navigate(TheInternetPage.DYNAMIC_LOADING);
        boolean status = dynamicLoadingForm.loaderState().waitForExist(dynamicLoadingForm.getTimeout());

        Assert.assertFalse(status);
        Assert.assertTrue(dynamicLoadingForm.startButtonState().waitForExist());
    }

    @Test
    public void testShouldBePossibleToWaitElementNotExistsCustomTime() {
        long waitTime = CUSTOM_WAIT_TIME;
        dynamicControlsForm.clickRemove();

        checkWaitingTimeForInputState(waitTime, inputState -> dynamicControlsForm.checkboxState().waitForNotExist(waitTime));
    }

    @Test
    public void testShouldBePossibleToWaitElementNotExists() {
        long waitTime = getConditionTimeout();
        dynamicControlsForm.clickRemove();

        long startTime = getCurrentTime();
        boolean isMissed = dynamicControlsForm.checkboxState().waitForNotExist();
        double duration = calculateDuration(startTime);

        Assert.assertTrue(isMissed);
        Assert.assertTrue(duration < waitTime);
    }
}

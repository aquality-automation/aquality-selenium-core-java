package tests.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.elements.ElementStateProvider;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.browser.AqualityServices;
import theinternet.DynamicControlsForm;
import theinternet.DynamicLoadingForm;
import theinternet.TheInternetPage;

import static utils.TimeUtil.calculateDuration;
import static utils.TimeUtil.getCurrentTime;

public class ElementStateProviderTests {
    private static final double OPERATION_TIME = 5;
    private static final long CUSTOM_WAIT_TIME = 2L;
    private static final Dimension DEFAULT_SIZE = new Dimension(1024, 768);
    private static final By absentElementLocator = By.xpath("//div[@class='not exist element']");

    private IApplication getBrowser() {
        return AqualityServices.getApplication();
    }

    private boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    private void navigate(TheInternetPage page) {
        getBrowser().getDriver().navigate().to(page.getAddress());
    }

    @BeforeMethod
    protected void beforeMethod() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        getBrowser().getDriver().manage().window().setSize(DEFAULT_SIZE);
    }

    @AfterMethod
    private void cleanUp () {
        if (isApplicationStarted()) {
            getBrowser().getDriver().quit();
        }
    }

    private ElementStateProvider state(By locator) {
        return new ElementStateProvider(locator,
                AqualityServices.get(IConditionalWait.class),
                AqualityServices.get(IElementFinder.class));
    }

    private ElementStateProvider inputState() {
        return state(DynamicControlsForm.getInputTextboxLocator());
    }

    private ElementStateProvider checkboxState() {
        return state(DynamicControlsForm.getCheckboxLocator());
    }

    private ElementStateProvider loaderState() {
        return state(DynamicLoadingForm.getLoadingLabelLocator());
    }

    private ElementStateProvider startButtonState() {
        return state(DynamicLoadingForm.getStartButtonLocator());
    }

    private ElementStateProvider absentElementState() {
        return state(absentElementLocator);
    }

    private void click(By locator) {
        locator.findElement(getBrowser().getDriver()).click();
    }

    private void clickEnable() {
        click(DynamicControlsForm.getEnableButtonLocator());
    }

    private void clickRemove() {
        click(DynamicControlsForm.getRemoveButtonLocator());
    }

    private void clickStart() {
        click(DynamicLoadingForm.getStartButtonLocator());
    }

    private long getConditionTimeout() {
        return AqualityServices.get(ITimeoutConfiguration.class).getCondition();
    }

    @Test
    public void testElementShouldWaitForEnabledWithCustomTimeout() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        long waitTime = CUSTOM_WAIT_TIME;

        long startTime = getCurrentTime();
        boolean isEnabled = inputState().waitForEnabled(waitTime);
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isEnabled);
        Assert.assertTrue(duration >= waitTime && duration <= (waitTime + OPERATION_TIME));
    }

    @Test
    public void testElementShouldWaitForEnabledWithDefaultTimeout() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        long waitTime = getConditionTimeout();

        long startTime = getCurrentTime();
        boolean isEnabled = inputState().waitForEnabled();
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isEnabled);
        Assert.assertTrue(duration >= waitTime && duration <= (waitTime + OPERATION_TIME));
    }

    @Test
    public void testElementShouldWaitForNotEnabledWithCustomTimeout() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        long waitTime = CUSTOM_WAIT_TIME;

        clickEnable();
        inputState().waitForEnabled();

        long startTime = getCurrentTime();
        boolean isDisabled = inputState().waitForNotEnabled(waitTime);
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isDisabled);
        Assert.assertTrue(duration >= waitTime && duration <= (waitTime + OPERATION_TIME));
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNoSuchShouldBeThrownForWaitEnabledIfElementNotFound(){
        absentElementState().waitForEnabled(CUSTOM_WAIT_TIME);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testNoSuchShouldBeThrownForWaitNotEnabledIfElementNotFound(){
        absentElementState().waitForNotEnabled(CUSTOM_WAIT_TIME);
    }

    @Test
    public void testElementShouldWaitForNotEnabledWithDefaultTimeout() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        long waitTime = getConditionTimeout();


        clickEnable();
        inputState().waitForEnabled();

        long startTime = getCurrentTime();
        boolean isDisabled = inputState().waitForNotEnabled();
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isDisabled);
        Assert.assertTrue(duration >= waitTime && duration <= (waitTime + OPERATION_TIME));
    }

    @Test
    public void testTextBoxEnabled() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        clickEnable();
        inputState().waitForEnabled();
        Assert.assertTrue(inputState().isEnabled());
    }

    @Test
    public void testWaitInvisibility() {
        navigate(TheInternetPage.DYNAMIC_LOADING);

        startButtonState().waitForClickable();
        clickStart();

        loaderState().waitForDisplayed(DynamicLoadingForm.getSmallTimeout());

        boolean status = loaderState().waitForNotDisplayed(DynamicLoadingForm.getSmallTimeout());
        Assert.assertFalse(status);

        status = loaderState().waitForNotDisplayed();
        Assert.assertTrue(status);
    }

    @Test
    public void testWaitForExist(){
        navigate(TheInternetPage.DYNAMIC_LOADING);
        boolean status = loaderState().waitForExist(DynamicLoadingForm.getTimeout());

        Assert.assertFalse(status);
        Assert.assertTrue(startButtonState().waitForExist());
    }

    @Test
    public void testShouldBePossibleToWaitElementNotExistsCustom(){
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        long waitTime = CUSTOM_WAIT_TIME;
        clickRemove();
        long startTime = getCurrentTime();
        boolean isMissed = checkboxState().waitForNotExist(waitTime);
        double duration = calculateDuration(startTime);

        Assert.assertFalse(isMissed);
        Assert.assertTrue(duration >= waitTime && duration <= (waitTime + OPERATION_TIME));
    }

    @Test
    public void testShouldBePossibleToWaitElementNotExists(){
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        long waitTime = getConditionTimeout();
        clickRemove();

        long startTime = getCurrentTime();
        boolean isMissed = checkboxState().waitForNotExist();
        double duration = calculateDuration(startTime);

        Assert.assertTrue(isMissed);
        Assert.assertTrue(duration < waitTime);
    }
}

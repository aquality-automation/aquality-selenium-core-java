package tests.elements;

import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.application.browser.AqualityServices;
import tests.application.browser.CachedLabel;
import tests.application.browser.ITheInternetPageTest;
import theinternet.DynamicControlsForm;
import theinternet.DynamicLoadingForm;
import theinternet.TheInternetPage;

import java.util.concurrent.TimeoutException;
import java.util.function.Predicate;

public class CachedWebElementTests implements ITheInternetPageTest, ICachedElementTests {

    private void startLoading() {
        navigate(TheInternetPage.DYNAMIC_LOADING);
        DynamicLoadingForm.getStartLabel().click();
    }

    private void openDynamicControls() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
    }

    private void waitForLoading(CachedLabel loader) {
        Assert.assertTrue(loader.state().waitForDisplayed(), "Loader should be displayed in the beginning");
        Assert.assertTrue(loader.state().waitForNotDisplayed(), "Loader should not be displayed in the end");
    }

    private IConditionalWait conditionalWait() {
        return AqualityServices.get(IConditionalWait.class);
    }

    @Test
    public void testShouldReturnFalseAtWaitForDisplayedWhenElementIsNotDisplayed() {
        CachedLabel loader = DynamicLoadingForm.getLoadingLabel();
        startLoading();
        waitForLoading(loader);
        Assert.assertFalse(loader.state().waitForDisplayed(ZERO_TIMEOUT));
    }

    @Test
    public void testShouldReturnTrueAtWaitForExistWhenElementIsNotDisplayed() {
        CachedLabel loader = DynamicLoadingForm.getLoadingLabel();
        startLoading();
        waitForLoading(loader);
        Assert.assertTrue(loader.state().waitForExist(ZERO_TIMEOUT));
    }

    @Test
    public void testShouldBeStaleWhenBecameInvisible() {
        startLoading();
        CachedLabel loader = DynamicLoadingForm.getLoadingLabel();
        Assert.assertTrue(loader.state().waitForDisplayed(), "Loader should be displayed in the beginning");
        Assert.assertTrue(conditionalWait().waitFor(() -> loader.cache().isStale()),
                "Loader should become invisible and be treated as stale");
        Assert.assertFalse(loader.state().isDisplayed(), "Invisible loader should be not displayed");
        Assert.assertFalse(loader.state().isExist(),
                "Loader that was displayed previously and become invisible should be treated as disappeared");
        Assert.assertTrue(loader.state().waitForExist(ZERO_TIMEOUT),
                "When waiting for existance, we should get an actual element's state");
    }

    @Test
    public void testShouldRefreshElementWhenItIsStale() {
        openDynamicControls();
        CachedLabel example = DynamicControlsForm.getContentLabel();
        String exampleToString = example.getElement().getId();
        getBrowser().getDriver().navigate().refresh();
        Assert.assertTrue(example.cache().isRefreshNeeded(), "refresh should be needed when refreshed the page");
        String newToString = example.getElement().getId();
        Assert.assertNotEquals(newToString, exampleToString, "new and old element's IDs should be different");
    }

    @Test(dataProvider = "stateFunctionsFalseWhenElementStale")
    public void testShouldReturnCorrectStateFalseWhenWindowIsRefreshed(Predicate<IElementStateProvider> stateCondition) throws TimeoutException {
        assertStateConditionAfterRefresh(stateCondition, false);
    }

    @Test(dataProvider = "stateFunctionsTrueWhenElementStaleWhichRetriveElement")
    public void testShouldReturnCorrectStateTrueWhenWindowIsRefreshed(Predicate<IElementStateProvider> stateCondition) throws TimeoutException {
        assertStateConditionAfterRefresh(stateCondition, true);
    }

    private void assertStateConditionAfterRefresh(Predicate<IElementStateProvider> stateCondition, boolean expectedValue) throws TimeoutException {
        openDynamicControls();
        CachedLabel testElement = DynamicControlsForm.getCheckboxLabel();
        testElement.state().waitForClickable();
        DynamicControlsForm.getRemoveLabel().click();
        conditionalWait().waitForTrue(
                () -> testElement.cache().isStale(), "Element should be stale when it disappeared.");
        getBrowser().getDriver().navigate().refresh();
        Assert.assertTrue(testElement.cache().isStale(), "Element should remain stale after the page refresh.");
        Assert.assertEquals(stateCondition.test(testElement.state()), expectedValue,
                "Element state condition is not expected after refreshing the window");
    }
}

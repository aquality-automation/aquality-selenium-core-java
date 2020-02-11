package tests.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.ITestWithApplication;
import tests.application.windowsApp.AqualityServices;
import tests.application.windowsApp.CachedButton;
import tests.application.windowsApp.CalculatorWindow;

import java.util.function.Predicate;

public class CachedWindowsElementTests implements ICachedElementTests, ITestWithApplication {

    @Override
    public IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    @Override
    public boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    @Test
    public void testShouldWorkWithCalculatorWithCachedElement() {
        new CachedButton(CalculatorWindow.getOneButton()).click();
        new CachedButton(CalculatorWindow.getPlusButton()).click();
        new CachedButton(CalculatorWindow.getOneButton()).click();
        new CachedButton(CalculatorWindow.getEqualsButton()).click();
        String result = new CachedButton(CalculatorWindow.getResultsLabel()).getElement().getText();
        Assert.assertTrue(result.contains("2"));
    }

    @Test
    public void testShouldReturnSameElementAfterInteraction() {
        CachedButton oneButton = new CachedButton(CalculatorWindow.getOneButton());
        String initialElement = oneButton.getElement().getId();
        oneButton.click();
        String resultElement = oneButton.getElement().getId();
        Assert.assertEquals(resultElement, initialElement, "Element should be the same after getting interaction");
    }

    @Test
    public void testShouldReturnSameElementAfterGetState() {
        CachedButton oneButton = new CachedButton(CalculatorWindow.getOneButton());
        String initialElement = oneButton.getElement().getId();
        oneButton.state().waitForClickable();
        String resultElement = oneButton.getElement().getId();
        Assert.assertEquals(resultElement, initialElement, "Element should be the same after getting state");
    }

    @Test
    public void testShouldReturnNewElementWhenWindowIsReopened() {
        CachedButton oneButton = new CachedButton(CalculatorWindow.getOneButton());
        String initialElement = oneButton.getElement().getId();
        getApplication().getDriver().quit();
        oneButton.state().waitForClickable();
        String resultElement = oneButton.getElement().getId();
        Assert.assertNotEquals(resultElement, initialElement, "Element is still the same after reopening the window");
    }

    @Test(dataProvider = "stateFunctionsFalseWhenElementStale")
    public void testShouldReturnCorrectStateWhenWindowIsClosed(Predicate<IElementStateProvider> stateCondition) {
        assertStateConditionAfterQuit(stateCondition, false, false);
    }

    @Test(dataProvider = "stateFunctionsTrueWhenElementStaleWhichRetriveElement")
    public void testShouldReturnCorrectStateAndReopenApplicationWhenWindowIsClosed(Predicate<IElementStateProvider> stateCondition) {
        assertStateConditionAfterQuit(stateCondition, true, true);
    }

    private void assertStateConditionAfterQuit(Predicate<IElementStateProvider> stateCondition, boolean expectedValue, boolean shouldAppRestart){
        CachedButton oneButton = new CachedButton(CalculatorWindow.getOneButton());
        oneButton.getElement();
        getApplication().getDriver().quit();
        Assert.assertEquals(stateCondition.test(oneButton.state()), expectedValue,
                "Element state condition is not expected after closing the window");
        Assert.assertEquals(isApplicationStarted(), shouldAppRestart,
                String.format("Window was %1$s reopened when retrived the element state.", shouldAppRestart ? "not" : ""));
    }
}

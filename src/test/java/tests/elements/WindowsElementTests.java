package tests.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.ITestWithApplication;
import tests.applications.windowsApp.AqualityServices;
import tests.applications.windowsApp.CalculatorWindow;
import tests.elements.factory.CustomElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WindowsElementTests implements ITestWithApplication {

    private static final By LOCATOR = By.xpath("//id");
    private static final String ELEMENT_NAME = "CustomElement";
    private static final ElementState ELEMENT_STATE = ElementState.EXISTS_IN_ANY_STATE;
    private static final CustomElement CUSTOM_WEB_ELEMENT = new CustomElement(LOCATOR, ELEMENT_NAME, ELEMENT_STATE);

    @Override
    public IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    @Override
    public boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    @Test
    public void testShouldBePossibleToGetLocator() {
        Assert.assertEquals(CUSTOM_WEB_ELEMENT.getLocator(), LOCATOR);
    }

    @Test
    public void testShouldBePossibleToGetElementName() {
        Assert.assertEquals(CUSTOM_WEB_ELEMENT.getName(), ELEMENT_NAME);
    }

    @Test
    public void testShouldBePossibleToGetState() {
        Assert.assertNotNull(CUSTOM_WEB_ELEMENT.state(), "State provider should not be null");
    }

    @Test
    public void testShouldBePossibleToGetElement() {
        RemoteWebElement element = CalculatorWindow.getOneButton().getElement();
        Assert.assertNotNull(element, "RemoteWebElement should not be null");
    }

    @Test
    public void testShouldBePossibleToGetElementWithCustomTimeout() {
        RemoteWebElement element = CalculatorWindow.getOneButton().getElement(Duration.ofSeconds(1L));
        Assert.assertNotNull(element, "RemoteWebElement should not be null");
    }

    @Test
    public void testShouldBePossibleToClickOnElementAndGetText() {
        CalculatorWindow.getOneButton().click();
        Assert.assertEquals(CalculatorWindow.getLeftValueTextBox().getText(), "1", "1 should be entered");
    }

    @Test
    public void testShouldBePossibleToSendKeysToElement() {
        String keys = "12";
        CalculatorWindow.getLeftValueTextBox().sendKeys(keys);
        Assert.assertEquals(CalculatorWindow.getLeftValueTextBox().getText(), keys, "Keys should be entered");
    }

    @Test
    public void testShouldBePossibleToGetAttributeFromElement() {
        String value = CalculatorWindow.getOneButton().getAttribute("AutomationId");
        Assert.assertEquals(value, "26", "Keys should be entered");
    }

    @Test(dataProvider = "getChildElementFunctions")
    public void testShouldBePossibleToFindChildElement(Callable<CustomElement> findChildElement) {
        try {
            CustomElement element = findChildElement.call();
            Assert.assertNotNull(element, "Child element should be found");
            Assert.assertTrue(element.state().isDisplayed(), "Child element should be displayed");
        } catch (Exception e) {
            Assert.fail("Cannot find child element", e);
        }
    }

    @DataProvider(name = "getChildElementFunctions")
    public Object[] getChildElementFunctions() {
        String name = "Child button";
        By childLoc = CalculatorWindow.getOneButtonLoc();
        List<Callable<CustomElement>> findFunctions = new ArrayList<>();
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, name, CustomElement.class, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, name, CustomElement.class));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, CustomElement.class, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, CustomElement.class));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, name, CustomElement::new, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, name, CustomElement::new));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, CustomElement::new, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> CalculatorWindow.getWindowLabel().findChildElement(childLoc, CustomElement::new));
        return findFunctions.toArray();
    }
}

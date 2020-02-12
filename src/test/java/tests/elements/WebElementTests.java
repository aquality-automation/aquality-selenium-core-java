package tests.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.ITestWithApplication;
import tests.applications.browser.AqualityServices;
import tests.elements.factory.CustomWebElement;
import theinternet.DynamicControlsForm;
import theinternet.InputsForm;
import theinternet.TheInternetPage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class WebElementTests implements ITestWithApplication {

    private static final By LOCATOR = By.xpath("//id");
    private static final String ELEMENT_NAME = "CustomElement";
    private static final ElementState ELEMENT_STATE = ElementState.EXISTS_IN_ANY_STATE;
    private static final CustomWebElement CUSTOM_WEB_ELEMENT = new CustomWebElement(LOCATOR, ELEMENT_NAME, ELEMENT_STATE);

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
        getApplication().getDriver().navigate().to(TheInternetPage.DYNAMIC_CONTROLS.getAddress());
        RemoteWebElement element = DynamicControlsForm.getEnableButton().getElement();
        Assert.assertNotNull(element, "RemoteWebElement should not be null");
    }

    @Test
    public void testShouldBePossibleToGetElementWithCustomTimeout() {
        getApplication().getDriver().navigate().to(TheInternetPage.DYNAMIC_CONTROLS.getAddress());
        RemoteWebElement element = DynamicControlsForm.getEnableButton().getElement(1L);
        Assert.assertNotNull(element, "RemoteWebElement should not be null");
    }

    @Test
    public void testShouldBePossibleToClickOnElement() {
        getApplication().getDriver().navigate().to(TheInternetPage.DYNAMIC_CONTROLS.getAddress());
        DynamicControlsForm.getEnableButton().click();
        Assert.assertTrue(DynamicControlsForm.getLoadingLabel().state().isDisplayed(), "Loading element should be displayed after click");
    }

    @Test
    public void testShouldBePossibleToSendKeysToElement() {
        getApplication().getDriver().navigate().to(TheInternetPage.INPUTS.getAddress());
        String keys = "12345";
        InputsForm.getInputTextBox().sendKeys(keys);
        Assert.assertEquals(InputsForm.getInputTextBox().getAttribute("value"), keys, "Text should be entered");
    }

    @Test
    public void testShouldBePossibleToGetAttributeFromElement() {
        getApplication().getDriver().navigate().to(TheInternetPage.DYNAMIC_CONTROLS.getAddress());
        String type = DynamicControlsForm.getRemoveButton().getAttribute("type");
        Assert.assertEquals(type, "button", "Attribute should be got successfully");
    }

    @Test
    public void testShouldBePossibleToGetTextFromElement() {
        getApplication().getDriver().navigate().to(TheInternetPage.DYNAMIC_CONTROLS.getAddress());
        String text = DynamicControlsForm.getRemoveButton().getText();
        Assert.assertEquals(text, "Remove", "Text should be got successfully");
    }

    @Test(dataProvider = "getChildElementFunctions")
    public void testShouldBePossibleToFindChildElement(Callable<CustomWebElement> findChildElement) {
        getApplication().getDriver().navigate().to(TheInternetPage.DYNAMIC_CONTROLS.getAddress());
        try {
            CustomWebElement element = findChildElement.call();
            Assert.assertNotNull(element, "Child element should be found");
            Assert.assertTrue(element.state().isDisplayed(), "Child element should be displayed");
        } catch (Exception e) {
            Assert.fail("Cannot find child element", e);
        }
    }

    @DataProvider(name = "getChildElementFunctions", parallel = true)
    public Object[] getChildElementFunctions() {
        String name = "Child checkbox";
        By childLoc = DynamicControlsForm.getCheckboxLocator();
        List<Callable<CustomWebElement>> findFunctions = new ArrayList<>();
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, name, CustomWebElement.class, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, name, CustomWebElement.class));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, CustomWebElement.class, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, CustomWebElement.class));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, name, CustomWebElement::new, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, name, CustomWebElement::new));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, CustomWebElement::new, ElementState.EXISTS_IN_ANY_STATE));
        findFunctions.add(() -> DynamicControlsForm.getContentLabel().findChildElement(childLoc, CustomWebElement::new));
        return findFunctions.toArray();
    }
}

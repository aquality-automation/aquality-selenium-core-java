package tests.elements.factory;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementFactory;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementSupplier;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.ITestWithApplication;
import tests.applications.windowsApp.AqualityServices;
import tests.applications.windowsApp.CalculatorWindow;

import java.util.List;

public class ElementFactoryTests implements ITestWithApplication, IFindElementsTests {

    private IElementFactory defaultFactory() {
        return AqualityServices.get(IElementFactory.class);
    }

    private IElement getParentElement() {
        return customFactory().getCustomElement(ICustomElement.class, CalculatorWindow.getWindowLocator(), "window");
    }

    @Test
    public void shouldBePossibleToCreateCustomElementViaCustomFactory() {
        Assert.assertNotNull(customFactory().getCustomElement(ICustomElement.class, By.name("any"), "any"));
    }

    @Test
    public void shouldBePossibleToCreateCustomElementViaCustomFactoryUsingImplementation() {
        Assert.assertNotNull(customFactory().getCustomElement(CustomElement.class, By.name("any"), "any"));
    }

    @Test
    public void shouldBePossibleToCreateCustomElementViaDefaultFactoryUsingSupplier() {
        Assert.assertNotNull(defaultFactory().getCustomElement(CustomElement::new, By.name("any"), "any"));
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaDefaultFactoryUsingImplementation() {
        Assert.assertTrue(defaultFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), CustomElement.class).size() > 1);
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionInFindElementsElementTypeIsUnknown() {
        Assert.assertThrows(IllegalArgumentException.class, () -> defaultFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionInFindChildElementElementTypeIsUnknown() {
        Assert.assertThrows(IllegalArgumentException.class, () -> defaultFactory().findChildElement(getParentElement(), CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class));
    }

    @Test
    public void shouldThrowIllegalArgumentExceptionInGetCustomElementElementTypeIsUnknown() {
        Assert.assertThrows(IllegalArgumentException.class, () -> defaultFactory().getCustomElement(ICustomElement.class, CalculatorWindow.getEqualsButtonByXPath(), ""));
    }

    @Test
    public void shouldBePossibleToFindChildElementViaCustomFactory() {
        IElement parent = getParentElement();
        Assert.assertNotNull(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButtonLoc(), ICustomElement.class));
    }

    @Test
    public void shouldBePossibleToFindChildElementWithSpecificName() {
        IElement parent = getParentElement();
        String name = "123";
        Assert.assertEquals(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButtonLoc(), name, ICustomElement.class).getName(), name);
    }

    @Test
    public void shouldBePossibleToFindChildElementWithSpecificNameUsingSupplier() {
        IElement parent = getParentElement();
        String name = "123";
        Assert.assertEquals(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButtonLoc(), name, CustomElement::new).getName(), name);
    }

    @Test
    public void shouldBePossibleToFindChildElementViaCustomFactoryUsingSupplier() {
        IElement parent = getParentElement();
        Assert.assertNotNull(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButtonLoc(), CustomElement::new));
    }

    @Test
    public void shouldBePossibleToFindChildElementViaDefaultFactoryUsingImplementation() {
        IElement parent = defaultFactory().getCustomElement(CustomElement.class, CalculatorWindow.getWindowLocator(), "window");
        Assert.assertNotNull(defaultFactory().findChildElement(parent, CalculatorWindow.getEqualsButtonLoc(), CustomElement.class));
    }

    @Test
    public void shouldBePossibleToFindChildElementWithCustomState() {
        IElement parent = getParentElement();
        Assert.assertEquals(
                defaultFactory().findChildElement(parent, CalculatorWindow.getEqualsButtonLoc(), CustomElement.class,
                        ElementState.EXISTS_IN_ANY_STATE).getState(),
                ElementState.EXISTS_IN_ANY_STATE);
    }

    @Test
    public void shouldSetCorrectParametersWhenGettingElement() {
        String name = "1some2";
        CustomElement element = defaultFactory().getCustomElement(CustomElement::new, CalculatorWindow.getEqualsButtonLoc(), name, ElementState.EXISTS_IN_ANY_STATE);
        Assert.assertEquals(element.getLocator(), CalculatorWindow.getEqualsButtonLoc());
        Assert.assertEquals(element.getName(), name);
        Assert.assertEquals(element.getState(), ElementState.EXISTS_IN_ANY_STATE);
    }

    @Override
    public IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    @Override
    public boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, Class<T> clazz, ElementsCount count) {
        return customFactory().findElements(locator, clazz, count);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, Class<T> clazz) {
        return customFactory().findElements(locator, clazz);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, String name, Class<T> clazz) {
        return customFactory().findElements(locator, name, clazz);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier, ElementsCount count, ElementState state) {
        return customFactory().findElements(locator, name, supplier, count, state);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, IElementSupplier<T> supplier, ElementsCount count, ElementState state) {
        return customFactory().findElements(locator, supplier, count, state);
    }
}

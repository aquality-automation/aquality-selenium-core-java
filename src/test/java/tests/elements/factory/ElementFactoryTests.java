package tests.elements.factory;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementFactory;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Test;
import tests.ITestWithApplication;
import tests.application.windowsApp.AqualityServices;
import tests.application.windowsApp.CalculatorWindow;

public class ElementFactoryTests implements ITestWithApplication {
    private static final String CONDITION_TIMEOUT_KEY = "timeouts.timeoutCondition";
    private static final String NEW_TIMEOUT_VALUE = "5";
    private static final int XPATH_SUBSTRING_BEGIN_INDEX = 10;

    private CustomElementFactory customFactory() {
        return new CustomElementFactory(AqualityServices.get(IConditionalWait.class),
                AqualityServices.get(IElementFinder.class), AqualityServices.get(ILocalizationManager.class));
    }

    private IElementFactory defaultFactory() {
        return AqualityServices.get(IElementFactory.class);
    }

    @BeforeGroups("timeout")
    public void setupTimeout() {
        System.setProperty(CONDITION_TIMEOUT_KEY, NEW_TIMEOUT_VALUE);
        AqualityServices.resetInjector();
    }

    @AfterGroups("timeout")
    public void resetTimeout() {
        System.clearProperty(CONDITION_TIMEOUT_KEY);
        AqualityServices.resetInjector();
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
    public void shouldBePossibleToFindCustomElementsViaCustomFactory() {
        Assert.assertTrue(customFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class).size() > 1);
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaDefaultFactoryUsingImplementation() {
        Assert.assertTrue(defaultFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), CustomElement.class).size() > 1);
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaCustomFactoryWithCustomElementsCount() {
        Assert.assertTrue(customFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class, ElementsCount.MORE_THEN_ZERO).size() > 1);
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaSupplierWithDefaultName() {
        Assert.assertTrue(customFactory().findElements(
                CalculatorWindow.getEqualsButtonByXPath(), CustomElement::new, ElementsCount.MORE_THEN_ZERO,
                ElementState.EXISTS_IN_ANY_STATE).size() > 1);
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaSupplierWithCustomName() {
        String customName = "56789";
        Assert.assertTrue(customFactory().findElements(
                CalculatorWindow.getEqualsButtonByXPath(), customName, CustomElement::new, ElementsCount.MORE_THEN_ZERO,
                ElementState.EXISTS_IN_ANY_STATE).get(0).getName().contains(customName));
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaSupplierWithCustomState() {
        Assert.assertEquals(customFactory().findElements(
                CalculatorWindow.getEqualsButtonByXPath(), CustomElement::new, ElementsCount.MORE_THEN_ZERO,
                ElementState.EXISTS_IN_ANY_STATE).get(0).getState(), ElementState.EXISTS_IN_ANY_STATE);
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaCustomFactoryWithDefaultElementsCount() {
        Assert.assertTrue(customFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class, ElementsCount.ANY).size() > 1);
    }

    @Test
    public void shouldSetCorrectParametersWhenFindElements() {
        String customName = "asd";
        ICustomElement element = customFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), customName, ICustomElement.class).get(1);
        Assert.assertNotEquals(element.getName(), customName);
        Assert.assertTrue(element.getName().contains(customName));
        Assert.assertTrue(element.getLocator().toString().contains(CalculatorWindow.getEqualsButtonByXPath().toString().substring(XPATH_SUBSTRING_BEGIN_INDEX)));
    }

    @Test
    public void shouldBePossibleToFindCustomElementsViaCustomFactoryWithZeroElementsCount() {
        Assert.assertTrue(customFactory().findElements(By.name("any"), ICustomElement.class, ElementsCount.ANY).isEmpty());
    }

    @Test(groups = "timeout")
    public void shouldThrowTimeoutExceptionWhenCountIsNotExpected() {
        Assert.assertThrows(TimeoutException.class, () -> customFactory().findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class, ElementsCount.ZERO));
    }

    @Test
    public void shouldThrowInvalidArgumentExceptionInFindElementsWhenLocatorIsNotSupported() {
        Assert.assertThrows(InvalidArgumentException.class, () -> customFactory().findElements(CalculatorWindow.getEqualsButton(), ICustomElement.class, ElementsCount.MORE_THEN_ZERO));
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
        Assert.assertNotNull(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButton(), ICustomElement.class));
    }

    @Test
    public void shouldBePossibleToFindChildElementWithSpecificName() {
        IElement parent = getParentElement();
        String name = "123";
        Assert.assertEquals(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButton(), name, ICustomElement.class).getName(), name);
    }

    @Test
    public void shouldBePossibleToFindChildElementWithSpecificNameUsingSupplier() {
        IElement parent = getParentElement();
        String name = "123";
        Assert.assertEquals(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButton(), name, CustomElement::new).getName(), name);
    }

    @Test
    public void shouldBePossibleToFindChildElementViaCustomFactoryUsingSupplier() {
        IElement parent = getParentElement();
        Assert.assertNotNull(customFactory().findChildElement(parent, CalculatorWindow.getEqualsButton(), CustomElement::new));
    }

    @Test
    public void shouldBePossibleToFindChildElementViaDefaultFactoryUsingImplementation() {
        IElement parent = defaultFactory().getCustomElement(CustomElement.class, CalculatorWindow.getWindowLocator(), "window");
        Assert.assertNotNull(defaultFactory().findChildElement(parent, CalculatorWindow.getEqualsButton(), CustomElement.class));
    }

    @Test
    public void shouldBePossibleToFindChildElementWithCustomState() {
        IElement parent = getParentElement();
        Assert.assertEquals(
                defaultFactory().findChildElement(parent, CalculatorWindow.getEqualsButton(), CustomElement.class,
                        ElementState.EXISTS_IN_ANY_STATE).getState(),
                ElementState.EXISTS_IN_ANY_STATE);
    }

    @Test
    public void shouldSetCorrectParametersWhenGettingElement() {
        String name = "1some2";
        CustomElement element = defaultFactory().getCustomElement(CustomElement::new, CalculatorWindow.getEqualsButton(), name, ElementState.EXISTS_IN_ANY_STATE);
        Assert.assertEquals(element.getLocator(), CalculatorWindow.getEqualsButton());
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
}

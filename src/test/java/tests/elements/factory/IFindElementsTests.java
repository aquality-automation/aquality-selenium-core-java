package tests.elements.factory;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementSupplier;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.TimeoutException;
import org.testng.Assert;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.applications.windowsApp.AqualityServices;
import tests.applications.windowsApp.CalculatorWindow;

import java.util.ArrayList;
import java.util.List;

public interface IFindElementsTests {
    String CONDITION_TIMEOUT_KEY = "timeouts.timeoutCondition";
    String NEW_TIMEOUT_VALUE = "5";
    int XPATH_SUBSTRING_BEGIN_INDEX = 10;

    <T extends IElement> List<T> findElements(By locator, Class<T> clazz, ElementsCount count);

    <T extends IElement> List<T> findElements(By locator, String name, Class<T> clazz, ElementState state);

    <T extends IElement> List<T> findElements(By locator, Class<T> clazz, ElementState state);

    <T extends IElement> List<T> findElements(By locator, Class<T> clazz);

    <T extends IElement> List<T> findElements(By locator, String name, Class<T> clazz);

    <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier, ElementsCount count,
                                              ElementState state);

    <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier, ElementState state);

    <T extends IElement> List<T> findElements(By locator, IElementSupplier<T> supplier, ElementState state);

    <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier);

    <T extends IElement> List<T> findElements(By locator, IElementSupplier<T> supplier, ElementsCount count,
                                              ElementState state);

    default CustomElementFactory customFactory() {
        return new CustomElementFactory(AqualityServices.get(IConditionalWait.class),
                AqualityServices.get(IElementFinder.class), AqualityServices.get(ILocalizationManager.class));
    }

    @Test(dataProvider = "getSupportedLocators")
    default void testShouldBePossibleToWorkWithElementsFoundBySpecificLocator(By locator) {
        List<ICustomElement> elements = findElements(locator, "some elements", CustomElement::new, ElementState.EXISTS_IN_ANY_STATE);
        Assert.assertFalse(elements.isEmpty());
        Assert.assertEquals(elements.stream().map(IElement::getElement).toArray().length, elements.size(),
                "elements count not match to expected");
    }

    @DataProvider
    default Object[] getSupportedLocators() {
        List<By> locators = new ArrayList<>();
        locators.add(CalculatorWindow.getDottedXPathLocator());
        locators.add(CalculatorWindow.getTagXpathLocator());
        locators.add(CalculatorWindow.getRelativeXPathLocator());
        return locators.toArray();
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaCustomFactory() {
        Assert.assertTrue(findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class).size() > 1);
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaCustomFactoryWithCustomElementsCount() {
        Assert.assertTrue(findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class, ElementsCount.MORE_THEN_ZERO).size() > 1);
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaSupplierWithDefaultName() {
        Assert.assertTrue(findElements(
                CalculatorWindow.getEqualsButtonByXPath(), CustomElement::new, ElementsCount.MORE_THEN_ZERO,
                ElementState.EXISTS_IN_ANY_STATE).size() > 1);
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaSupplierWithCustomName() {
        String customName = "56789";
        Assert.assertTrue(findElements(
                CalculatorWindow.getEqualsButtonByXPath(), customName, CustomElement::new)
                .get(0).getName().contains(customName));
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaSupplierWithoutName() {
        Assert.assertFalse(findElements(
                CalculatorWindow.getEqualsButtonByXPath(), CustomElement::new, ElementState.EXISTS_IN_ANY_STATE)
                .get(0).getName().isEmpty());
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaSupplierWithCustomState() {
        Assert.assertEquals(findElements(
                CalculatorWindow.getEqualsButtonByXPath(), "equals", CustomElement::new, ElementsCount.ANY, ElementState.EXISTS_IN_ANY_STATE)
                .get(0).getState(), ElementState.EXISTS_IN_ANY_STATE);
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaCustomFactoryWithDefaultElementsCount() {
        Assert.assertTrue(findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class, ElementState.EXISTS_IN_ANY_STATE).size() > 1);
    }

    @Test
    default void shouldSetCorrectParametersWhenFindElements() {
        String customName = "asd";
        ICustomElement element = findElements(CalculatorWindow.getEqualsButtonByXPath(), customName, ICustomElement.class).get(1);
        Assert.assertNotEquals(element.getName(), customName);
        Assert.assertTrue(element.getName().contains(customName));
        Assert.assertTrue(element.getLocator().toString().contains(CalculatorWindow.getEqualsButtonByXPath().toString().substring(XPATH_SUBSTRING_BEGIN_INDEX)));
    }

    @Test
    default void shouldBePossibleToFindCustomElementsViaCustomFactoryWithZeroElementsCount() {
        Assert.assertTrue(findElements(By.xpath("//any"), ICustomElement.class, ElementsCount.ANY).isEmpty());
    }

    @Test(groups = "timeout")
    default void shouldThrowTimeoutExceptionWhenCountIsNotExpected() {
        Assert.assertThrows(TimeoutException.class, () -> findElements(CalculatorWindow.getEqualsButtonByXPath(), ICustomElement.class, ElementsCount.ZERO));
    }

    @Test
    default void shouldThrowInvalidArgumentExceptionInFindElementsWhenLocatorIsNotSupported() {
        Assert.assertThrows(InvalidArgumentException.class,
                () -> findElements(CalculatorWindow.getEqualsButtonLoc(), "equals", ICustomElement.class, ElementState.EXISTS_IN_ANY_STATE));
    }

    @BeforeGroups("timeout")
    default void setupTimeout() {
        System.setProperty(CONDITION_TIMEOUT_KEY, NEW_TIMEOUT_VALUE);
        AqualityServices.resetInjector();
    }

    @AfterGroups("timeout")
    default void resetTimeout() {
        System.clearProperty(CONDITION_TIMEOUT_KEY);
        AqualityServices.resetInjector();
    }
}

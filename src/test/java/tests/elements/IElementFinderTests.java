package tests.elements;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.ITestWithApplication;
import tests.application.windowsApp.AqualityServices;
import tests.application.windowsApp.CalculatorWindow;

public interface IElementFinderTests extends ITestWithApplication {
    IElementFinder getElementFinder();

    default IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    default boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    @DataProvider
    default Object[] elementStates() {
        return ElementState.values();
    }

    default Long getCustomTimeout() {
        return 10L;
    }

    default Long getSmallTimeout() {
        return 1L;
    }

    default By getAbsentLocator() {
        return By.name("absent");
    }

    @Test()
    default void shouldFindMultipleElementsInDefaultStateWithCustomTimeout() {
        Assert.assertTrue(getElementFinder()
                .findElements(CalculatorWindow.getEqualsButton(), getCustomTimeout())
                .size() > 1);
    }

    @Test(dataProvider = "elementStates")
    default void shouldFindMultipleElements(ElementState state) {
        Assert.assertTrue(getElementFinder()
                .findElements(CalculatorWindow.getEqualsButton(), state)
                .size() > 1);
    }

    @Test(dataProvider = "elementStates")
    default void shouldFindSingleElementViaFindElements(ElementState state) {
        Assert.assertEquals(getElementFinder()
                .findElements(CalculatorWindow.getOneButton(), state, getCustomTimeout())
                .size(), 1);
    }

    @Test(dataProvider = "elementStates")
    default void shouldFindSingleElement(ElementState state) {
        Assert.assertNotNull(getElementFinder()
                .findElement(CalculatorWindow.getOneButton(), state));
    }

    @Test(dataProvider = "elementStates")
    default void shouldFindSingleElementWithCustomTimeout(ElementState state) {
        Assert.assertNotNull(getElementFinder()
                .findElement(CalculatorWindow.getOneButton(), state, getCustomTimeout()));
    }

    @Test
    default void shouldFindSingleElementByPredicate() {
        Assert.assertNotNull(getElementFinder()
                .findElement(CalculatorWindow.getOneButton(), WebElement::isEnabled));
    }

    @Test
    default void shouldFindMultipleElementsByPredicate() {
        Assert.assertTrue(getElementFinder()
                .findElements(CalculatorWindow.getEqualsButton(), WebElement::isEnabled).size() > 1);
    }

    @Test
    default void shouldFindSingleElementByPredicateWithCustomTimeout() {
        Assert.assertNotNull(getElementFinder()
                .findElement(CalculatorWindow.getOneButton(), WebElement::isEnabled, getCustomTimeout()));
    }

    @Test(dataProvider = "elementStates")
    default void shouldResolveState(ElementState state) {
        Assert.assertNotNull(getElementFinder().resolveState(state));
    }

    @Test
    default void shouldThrowWhenElementAbsent() {
        Assert.assertThrows(NoSuchElementException.class, () -> getElementFinder().findElement(getAbsentLocator(), getSmallTimeout()));
    }

    @Test
    default void shouldReturnEmptyListWhenElementsAreAbsent() {
        Assert.assertTrue(getElementFinder().findElements(getAbsentLocator(), getSmallTimeout()).isEmpty());
    }
}

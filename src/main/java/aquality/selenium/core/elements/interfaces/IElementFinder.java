package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.DesiredState;
import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Predicate;

/**
 * Provides ability to find elements in desired ElementState.
 */
public interface IElementFinder extends SearchContext {

    /**
     * Provides DesiredState with predicate for desired ElementState.
     * @param state desired ElementState
     * @return DesiredState with predicate.
     */
    default DesiredState resolveState(ElementState state) {
        Predicate<WebElement> elementStateCondition;
        if (state == ElementState.DISPLAYED) {
            elementStateCondition = WebElement::isDisplayed;
        } else if (state == ElementState.EXISTS_IN_ANY_STATE) {
            elementStateCondition = webElement -> true;
        } else {
            throw new UnsupportedOperationException(state.toString().concat(" state is not recognized"));
        }
        return new DesiredState(elementStateCondition, state.name());
    }

    /**
     * Finds element in desired ElementState
     * @param locator elements locator
     * @param state desired ElementState
     * @param timeoutInSeconds timeout for search
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     * @return found element
     */
    default WebElement findElement(By locator, ElementState state, Long timeoutInSeconds) {
        return findElements(locator, resolveState(state).withThrowingNoSuchElementException(), timeoutInSeconds).get(0);
    }

    /**
     * Finds element in desired ElementState with default timeout.
     * @param locator elements locator
     * @param state desired ElementState
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     * @return found element
     */
    default WebElement findElement(By locator, ElementState state) {
        return findElement(locator, state, null);
    }

    /**
     * Finds element in any state.
     * @param locator elements locator
     * @param timeoutInSeconds timeout for search
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time
     * @return found element
     */
    default WebElement findElement(By locator, Long timeoutInSeconds) {
        return findElement(locator, ElementState.EXISTS_IN_ANY_STATE, timeoutInSeconds);
    }

    /**
     * Finds element in any state with default timeout.
     * @param locator elements locator
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time
     * @return found element
     */
    default WebElement findElement(By locator) {
        return findElement(locator, ElementState.EXISTS_IN_ANY_STATE, null);
    }

    /**
     * Finds element in desired ElementState defined by predicate.
     * @param locator elements locator
     * @param elementStateCondition predicate to define element state
     * @param timeoutInSeconds timeout for search
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     * @return found element
     */
    default WebElement findElement(By locator, Predicate<WebElement> elementStateCondition, Long timeoutInSeconds) {
        DesiredState state = new DesiredState(elementStateCondition, "desired")
                .withThrowingNoSuchElementException();
        return findElements(locator, state, timeoutInSeconds).get(0);
    }

    /**
     * Finds element in desired ElementState defined by predicate with default timeout.
     * @param locator elements locator
     * @param elementStateCondition predicate to define element state
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     * @return found element
     */
    default WebElement findElement(By locator, Predicate<WebElement> elementStateCondition) {
        return findElement(locator, elementStateCondition, null);
    }

    /**
     * Finds elements in desired ElementState.
     * @param locator elements locator
     * @param state desired ElementState
     * @param timeoutInSeconds timeout for search
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, ElementState state, Long timeoutInSeconds) {
        return findElements(locator, resolveState(state).withCatchingTimeoutException(), timeoutInSeconds);
    }

    /**
     * Finds elements in any state.
     * @param locator elements locator
     * @param timeoutInSeconds timeout for search
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, Long timeoutInSeconds) {
        return findElements(locator, ElementState.EXISTS_IN_ANY_STATE, timeoutInSeconds);
    }

    /**
     * Finds elements in desired ElementState with default timeout.
     * @param locator elements locator
     * @param state desired ElementState
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, ElementState state) {
        return findElements(locator, state, null);
    }

    /**
     * Finds elements in any state with default timeout.
     * @param locator elements locator
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator) {
        return findElements(locator, ElementState.EXISTS_IN_ANY_STATE, null);
    }

    /**
     * Finds elements in state defined by predicate.
     * @param locator elements locator
     * @param timeoutInSeconds timeout for search
     * @param elementStateCondition predicate to define element state
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, Predicate<WebElement> elementStateCondition, Long timeoutInSeconds) {
        DesiredState state = new DesiredState(elementStateCondition, "desired").withCatchingTimeoutException();
        return findElements(locator, state, timeoutInSeconds);
    }

    /**
     * Finds elements in state defined by predicate with default timeout.
     * @param locator elements locator
     * @param elementStateCondition predicate to define element state
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, Predicate<WebElement> elementStateCondition) {
        return findElements(locator, elementStateCondition, null);
    }

    /**
     * Finds elements in state defined by DesiredState.
     * @param locator elements locator
     * @param timeoutInSeconds timeout for search
     * @param desiredState predicate to define element state
     * @return list of found elements
     */
    List<WebElement> findElements(By locator, DesiredState desiredState, Long timeoutInSeconds);

    /**
     * Finds elements in state defined by DesiredState with default timeout.
     * @param locator elements locator
     * @param desiredState predicate to define element state
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, DesiredState desiredState) {
        return findElements(locator, desiredState, null);
    }
}

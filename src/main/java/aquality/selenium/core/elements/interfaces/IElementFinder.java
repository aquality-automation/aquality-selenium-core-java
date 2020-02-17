package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.DesiredState;
import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Predicate;

/**
 * Provides ability to find elements by locator and search criteria.
 * The criteria for search could be:
 * - empty - to get all elements;
 * - desired {@link ElementState};
 * - with {@link DesiredState};
 * - with {@link Predicate<WebElement>}.
 */
public interface IElementFinder extends SearchContext {

    /**
     * Provides {@link DesiredState} with predicate for desired {@link ElementState}.
     *
     * @param state desired {@link ElementState}
     * @return {@link DesiredState} with predicate.
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
     * Finds element in desired {@link ElementState}
     *
     * @param locator elements locator
     * @param state   desired {@link ElementState}
     * @param timeout timeout for search
     * @return found element
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     */
    default WebElement findElement(By locator, ElementState state, Duration timeout) {
        return findElements(locator, resolveState(state).withThrowingNoSuchElementException(), timeout).get(0);
    }

    /**
     * Finds element in desired {@link ElementState} with default timeout.
     *
     * @param locator elements locator
     * @param state   desired {@link ElementState}
     * @return found element
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     */
    default WebElement findElement(By locator, ElementState state) {
        return findElement(locator, state, null);
    }

    /**
     * Finds element in any state.
     *
     * @param locator elements locator
     * @param timeout timeout for search
     * @return found element
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time
     */
    default WebElement findElement(By locator, Duration timeout) {
        return findElement(locator, ElementState.EXISTS_IN_ANY_STATE, timeout);
    }

    /**
     * Finds element in any state with default timeout.
     *
     * @param locator elements locator
     * @return found element
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time
     */
    default WebElement findElement(By locator) {
        return findElement(locator, ElementState.EXISTS_IN_ANY_STATE, null);
    }

    /**
     * Finds element in desired element's state defined by predicate.
     *
     * @param locator               elements locator
     * @param elementStateCondition predicate to define element state
     * @param timeout               timeout for search
     * @return found element
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     */
    default WebElement findElement(By locator, Predicate<WebElement> elementStateCondition, Duration timeout) {
        DesiredState state = new DesiredState(elementStateCondition, "desired")
                .withThrowingNoSuchElementException();
        return findElements(locator, state, timeout).get(0);
    }

    /**
     * Finds element in desired ElementState defined by predicate with default timeout.
     *
     * @param locator               elements locator
     * @param elementStateCondition predicate to define element state
     * @return found element
     * @throws org.openqa.selenium.NoSuchElementException if element was not found in time in desired state
     */
    default WebElement findElement(By locator, Predicate<WebElement> elementStateCondition) {
        return findElement(locator, elementStateCondition, null);
    }

    /**
     * Finds elements in desired {@link ElementState}.
     *
     * @param locator elements locator
     * @param state   desired {@link ElementState}
     * @param timeout timeout for search
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, ElementState state, Duration timeout) {
        return findElements(locator, resolveState(state).withCatchingTimeoutException(), timeout);
    }

    /**
     * Finds elements in any state.
     *
     * @param locator elements locator
     * @param timeout timeout for search
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, Duration timeout) {
        return findElements(locator, ElementState.EXISTS_IN_ANY_STATE, timeout);
    }

    /**
     * Finds elements in desired {@link ElementState} with default timeout.
     *
     * @param locator elements locator
     * @param state   desired {@link ElementState}
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, ElementState state) {
        return findElements(locator, state, null);
    }

    /**
     * Finds elements in any state with default timeout.
     *
     * @param locator elements locator
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator) {
        return findElements(locator, ElementState.EXISTS_IN_ANY_STATE, null);
    }

    /**
     * Finds elements in state defined by predicate.
     *
     * @param locator               elements locator
     * @param timeout               timeout for search
     * @param elementStateCondition predicate to define element state
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, Predicate<WebElement> elementStateCondition, Duration timeout) {
        DesiredState state = new DesiredState(elementStateCondition, "desired").withCatchingTimeoutException();
        return findElements(locator, state, timeout);
    }

    /**
     * Finds elements in state defined by predicate with default timeout.
     *
     * @param locator               elements locator
     * @param elementStateCondition predicate to define element state
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, Predicate<WebElement> elementStateCondition) {
        return findElements(locator, elementStateCondition, null);
    }

    /**
     * Finds elements in state defined by {@link DesiredState}.
     *
     * @param locator      elements locator
     * @param timeout      timeout for search
     * @param desiredState object with predicate to define element state
     * @return list of found elements
     */
    List<WebElement> findElements(By locator, DesiredState desiredState, Duration timeout);

    /**
     * Finds elements in state defined by {@link DesiredState} with default timeout.
     *
     * @param locator      elements locator
     * @param desiredState object with predicate to define element state
     * @return list of found elements
     */
    default List<WebElement> findElements(By locator, DesiredState desiredState) {
        return findElements(locator, desiredState, null);
    }
}

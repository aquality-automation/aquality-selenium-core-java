package aquality.selenium.core.elements.interfaces;

import java.time.Duration;

/**
 * Provides ability to define of element's state (whether it is displayed, exist or not)
 * Also provides respective positive and negative waiting functions
 */
public interface IElementStateProvider {

    /**
     * Is an element clickable on the form.
     *
     * @return true if element clickable, false otherwise
     */
    boolean isClickable();

    /**
     * Waits for is element clickable on the form.
     *
     * @param timeout Timeout for waiting
     * @throws org.openqa.selenium.TimeoutException when timeout exceeded and element is not clickable.
     */
    void waitForClickable(Duration timeout);

    /**
     * Waits for is element clickable on the form.
     * Uses condition timeout from settings file for waiting
     *
     * @throws org.openqa.selenium.TimeoutException when timeout exceeded and element is not clickable.
     */
    default void waitForClickable() {
        waitForClickable(null);
    }

    /**
     * Is an element displayed on the form.
     *
     * @return true if element displayed, false otherwise
     */
    boolean isDisplayed();

    /**
     * Waits for is element displayed on the form.
     *
     * @param timeout Timeout for waiting
     * @return true if element displayed after waiting, false otherwise
     */
    boolean waitForDisplayed(Duration timeout);

    /**
     * Waits for is element displayed on the form.
     * Uses condition timeout from settings file for waiting
     *
     * @return true if element displayed after waiting, false otherwise
     */
    default boolean waitForDisplayed() {
        return waitForDisplayed(null);
    }

    /**
     * Waits for is element displayed on the form.
     *
     * @param timeout Timeout for waiting
     * @return true if element displayed after waiting, false otherwise
     */
    boolean waitForNotDisplayed(Duration timeout);


    /**
     * Waits for is element displayed on the form.
     * Uses condition timeout from settings file for waiting
     *
     * @return true if element displayed after waiting, false otherwise
     */
    default boolean waitForNotDisplayed() {
        return waitForNotDisplayed(null);
    }

    /**
     * Is an element exist in DOM (without visibility check)
     *
     * @return true if element exist, false otherwise
     */
    boolean isExist();

    /**
     * Waits until element is exist in DOM (without visibility check).
     *
     * @param timeout Timeout for waiting
     * @return true if element exist after waiting, false otherwise
     */
    boolean waitForExist(Duration timeout);


    /**
     * Waits until element is exist in DOM (without visibility check).
     * Uses condition timeout from settings file for waiting
     *
     * @return true if element exist after waiting, false otherwise
     */
    default boolean waitForExist() {
        return waitForExist(null);
    }

    /**
     * Waits until element does not exist in DOM (without visibility check).
     *
     * @param timeout Timeout for waiting
     * @return true if element does not exist after waiting, false otherwise
     */
    boolean waitForNotExist(Duration timeout);

    /**
     * Waits until element does not exist in DOM (without visibility check).
     * Uses condition timeout from settings file for waiting
     *
     * @return true if element does not exist after waiting, false otherwise
     */
    default boolean waitForNotExist() {
        return waitForNotExist(null);
    }

    /**
     * Check that the element is enabled
     *
     * @return true if enabled
     * @throws org.openqa.selenium.NoSuchElementException when timeout exceeded and element not found.
     */
    boolean isEnabled();

    /**
     * Check that the element is enabled
     *
     * @param timeout Timeout for waiting
     * @return true if enabled
     * @throws org.openqa.selenium.NoSuchElementException when timeout exceeded and element not found.
     */
    boolean waitForEnabled(Duration timeout);


    /**
     * Check that the element is enabled (performed by a class member)
     * Uses condition timeout from settings file for waiting
     *
     * @return true if enabled
     * @throws org.openqa.selenium.NoSuchElementException when timeout exceeded and element not found.
     */
    default boolean waitForEnabled() {
        return waitForEnabled(null);
    }

    /**
     * Waits until element does not enabled in DOM
     *
     * @param timeout Timeout for waiting
     * @return true if element does not enabled after waiting, false otherwise
     * @throws org.openqa.selenium.NoSuchElementException when timeout exceeded and element not found.
     */
    boolean waitForNotEnabled(Duration timeout);

    /**
     * Waits until element does not enabled in DOM
     * Uses condition timeout from settings file for waiting
     *
     * @return true if element does not enabled after waiting, false otherwise
     * @throws org.openqa.selenium.NoSuchElementException when timeout exceeded and element not found.
     */
    default boolean waitForNotEnabled() {
        return waitForNotEnabled(null);
    }
}
package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import org.openqa.selenium.By;

import java.util.List;

/**
 * Defines the interface used to create the elements.
 */
public interface IElementFactory {

    /**
     * Create custom element according to passed parameters.
     *
     * @param elementSupplier Delegate that defines constructor of element.
     * @param locator         Locator of the target element.
     * @param name            Name of the target element.
     * @param <T>             Type of the target element.
     * @return Instance of custom element.
     */
    default <T extends IElement> T getCustomElement(IElementSupplier<T> elementSupplier, By locator, String name) {
        return getCustomElement(elementSupplier, locator, name, ElementState.DISPLAYED);
    }

    /**
     * Create custom element according to passed parameters.
     *
     * @param elementSupplier Delegate that defines constructor of element.
     * @param locator         Locator of the target element.
     * @param name            Name of the target element.
     * @param state           Visibility state of the target element.
     * @param <T>             Type of the target element.
     * @return Instance of custom element.
     */
    <T extends IElement> T getCustomElement(IElementSupplier<T> elementSupplier, By locator, String name, ElementState state);

    /**
     * Finds child element by its locator relative to parent element.
     *
     * @param childLoc      Locator of child element relative to its parent.
     * @param clazz         Class or interface of the element to be obtained.
     * @param name          Child element name.
     * @param parentElement Parent element for relative search of child element.
     * @param state         Visibility state of target element.
     * @return Child element.
     */
    <T extends IElement> T findChildElement(IElement parentElement, By childLoc, String name,
                                            Class<? extends IElement> clazz, ElementState state);

    /**
     * Finds child element by its locator relative to parent element.
     *
     * @param childLoc      Locator of child element relative to its parent.
     * @param supplier      Required element's supplier.
     * @param name          Child element name.
     * @param parentElement Parent element for relative search of child element.
     * @param state         Visibility state of child element.
     * @return Child element.
     */
    <T extends IElement> T findChildElement(IElement parentElement, By childLoc, String name,
                                            IElementSupplier<T> supplier, ElementState state);

    /**
     * Finds child element by its locator relative to parent element.
     *
     * @param childLoc      Locator of child element relative to its parent.
     * @param supplier      Required element's supplier.
     * @param parentElement Parent element for relative search of child element.
     * @param state         Visibility state of child element.
     * @return Child element.
     */
    default <T extends IElement> T findChildElement(IElement parentElement, By childLoc,
                                            IElementSupplier<T> supplier, ElementState state) {
        return findChildElement(parentElement, childLoc, null, supplier, state);
    }

    /**
     * Find list of elements.
     *
     * @param locator  Elements selector.
     * @param name    Child element name.
     * @param supplier Required elements' supplier.
     * @param count    Expected number of elements that have to be found (zero, more then zero, any).
     * @param state    Visibility state of target elements.
     * @return List of elements.
     */
    <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier, ElementsCount count,
                                              ElementState state);

    /**
     * Find list of elements.
     *
     * @param locator  Elements selector.
     * @param supplier Required elements' supplier.
     * @param count    Expected number of elements that have to be found (zero, more then zero, any).
     * @param state    Visibility state of target elements.
     * @return List of elements.
     */
    default <T extends IElement> List<T> findElements(By locator, IElementSupplier<T> supplier, ElementsCount count,
                                              ElementState state) {
        return findElements(locator, null, supplier, count, state);
    }

    /**
     * Find list of elements.
     *
     * @param locator Elements selector.
     * @param name    Child element name.
     * @param clazz   Class or interface of the element to be obtained.
     * @param count   Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of elements.
     */
    <T extends IElement> List<T> findElements(By locator, String name, Class<? extends IElement> clazz, ElementsCount count, ElementState state);

    /**
     * Find list of elements.
     *
     * @param locator Elements selector.
     * @param clazz   Class or interface of the element to be obtained.
     * @param count   Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of elements.
     */
    default <T extends IElement> List<T> findElements(By locator, Class<? extends IElement> clazz, ElementsCount count, ElementState state) {
        return findElements(locator, null, clazz, count, state);
    }

    /**
     * Find list of elements.
     *
     * @param locator Elements selector.
     * @param clazz   Class or interface of elements to be obtained.
     * @return List of elements.
     */
    default <T extends IElement> List<T> findElements(By locator, Class<? extends IElement> clazz) {
        return findElements(locator, clazz, ElementsCount.ANY, ElementState.DISPLAYED);
    }

    /**
     * Find list of elements.
     *
     * @param locator Elements selector.
     * @param clazz   Class or interface of elements to be obtained.
     * @param count   Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of elements.
     */
    default <T extends IElement> List<T> findElements(By locator, Class<? extends IElement> clazz, ElementsCount count) {
        return findElements(locator, clazz, count, ElementState.DISPLAYED);
    }
}

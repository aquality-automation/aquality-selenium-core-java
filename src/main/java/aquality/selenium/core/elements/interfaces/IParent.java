package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import org.openqa.selenium.By;

import java.util.List;

public interface IParent {

    /**
     * Find an element in the parent element
     *
     * @param childLoc child element locator
     * @param name     output name in logs
     * @param clazz    class or interface of the element to be obtained
     * @param state    visibility state of target element
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    <T extends IElement> T findChildElement(By childLoc, String name, Class<T> clazz, ElementState state);

    /**
     * Find an element in the parent element with DISPLAYED state
     *
     * @param childLoc child element locator
     * @param name     output name in logs
     * @param clazz    class or interface of the element to be obtained
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, String name, Class<T> clazz) {
        return findChildElement(childLoc, name, clazz, ElementState.DISPLAYED);
    }

    /**
     * Find an element in the parent element
     *
     * @param childLoc child element locator
     * @param clazz    class or interface of the element to be obtained
     * @param state    visibility state of target element
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, Class<T> clazz, ElementState state) {
        return findChildElement(childLoc, null, clazz, state);
    }

    /**
     * Find an element in the parent element with DISPLAYED state
     *
     * @param childLoc child element locator
     * @param clazz    class or interface of the element to be obtained
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, Class<T> clazz) {
        return findChildElement(childLoc, null, clazz, ElementState.DISPLAYED);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc Child element locator
     * @param name     output name in logs
     * @param supplier required element's supplier
     * @param state    visibility state of target element
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    <T extends IElement> T findChildElement(By childLoc, String name, IElementSupplier<T> supplier, ElementState state);

    /**
     * Find an element in the parent element with DISPLAYED state
     *
     * @param childLoc child element locator
     * @param name     output name in logs
     * @param supplier required element's supplier
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, String name, IElementSupplier<T> supplier) {
        return findChildElement(childLoc, name, supplier, ElementState.DISPLAYED);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc child element locator
     * @param supplier required element's supplier
     * @param state    visibility state of target element
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, IElementSupplier<T> supplier, ElementState state) {
        return findChildElement(childLoc, null, supplier, state);
    }

    /**
     * Find an element in the parent element with DISPLAYED state
     *
     * @param childLoc child element locator
     * @param supplier required element's supplier
     * @param <T>      the type of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, IElementSupplier<T> supplier) {
        return findChildElement(childLoc, null, supplier, ElementState.DISPLAYED);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param clazz         Class or interface of the elements to be obtained.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           Class<T> clazz) {
        return findChildElements(childLoc, clazz, ElementsCount.ANY);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param clazz         Class or interface of the elements to be obtained.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           Class<T> clazz, ElementsCount count) {
        return findChildElements(childLoc, clazz, count, ElementState.DISPLAYED);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param clazz         Class or interface of the elements to be obtained.
     * @param state         Visibility state of child elements.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           Class<T> clazz,
                                                           ElementState state) {
        return findChildElements(childLoc, clazz, ElementsCount.ANY, state);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param clazz         Class or interface of the elements to be obtained.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @param state         Visibility state of child elements.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           Class<T> clazz, ElementsCount count,
                                                           ElementState state) {
        return findChildElements(childLoc, null, clazz, count, state);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param name          Child elements name.
     * @param clazz         Class or interface of the elements to be obtained.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                           Class<T> clazz) {
        return findChildElements(childLoc, name, clazz, ElementsCount.ANY);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param name          Child elements name.
     * @param clazz         Class or interface of the elements to be obtained.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                           Class<T> clazz, ElementsCount count) {
        return findChildElements(childLoc, name, clazz, count, ElementState.DISPLAYED);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param name          Child elements name.
     * @param clazz         Class or interface of the elements to be obtained.
     * @param state         Visibility state of child elements.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                           Class<T> clazz, ElementState state) {
        return findChildElements(childLoc, name, clazz, ElementsCount.ANY, state);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param clazz         Class or interface of the elements to be obtained.
     * @param name          Child elements name.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @param state         Visibility state of target elements.
     * @param <T>           Type of the target elements.
     * @return List of child elements.
     */
    <T extends IElement> List<T> findChildElements(By childLoc, String name, Class<T> clazz,
                                                   ElementsCount count, ElementState state);

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param supplier      Required elements' supplier.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           IElementSupplier<T> supplier) {
        return findChildElements(childLoc, supplier, ElementsCount.ANY);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param supplier      Required elements' supplier.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           IElementSupplier<T> supplier, ElementsCount count) {
        return findChildElements(childLoc, supplier, count, ElementState.DISPLAYED);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param supplier      Required elements' supplier.
     * @param state         Visibility state of child elements.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           IElementSupplier<T> supplier,
                                                           ElementState state) {
        return findChildElements(childLoc, supplier, ElementsCount.ANY, state);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param supplier      Required elements' supplier.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @param state         Visibility state of child elements.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc,
                                                           IElementSupplier<T> supplier, ElementsCount count,
                                                           ElementState state) {
        return findChildElements(childLoc, null, supplier, count, state);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param name          Child elements name.
     * @param supplier      Required elements' supplier.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                           IElementSupplier<T> supplier) {
        return findChildElements(childLoc, name, supplier, ElementsCount.ANY);
    }

    /**
     * Finds displayed child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param name          Child elements name.
     * @param supplier      Required elements' supplier.
     * @param count         Expected number of elements that have to be found (zero, more then zero, any).
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                           IElementSupplier<T> supplier, ElementsCount count) {
        return findChildElements(childLoc, name, supplier, count, ElementState.DISPLAYED);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param name          Child elements name.
     * @param supplier      Required elements' supplier.
     * @param state         Visibility state of child elements.
     * @return List of child elements.
     */
    default <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                           IElementSupplier<T> supplier, ElementState state) {
        return findChildElements(childLoc, name, supplier, ElementsCount.ANY, state);
    }

    /**
     * Finds child elements by their locator relative to parent element.
     *
     * @param childLoc      Locator of child elements relative to its parent.
     * @param supplier      Required elements' supplier.
     * @param name          Child elements name.
     * @param state         Visibility state of child elements.
     * @param <T>           Type of the target elements.
     * @return List of child elements.
     */
    <T extends IElement> List<T> findChildElements(By childLoc, String name,
                                                   IElementSupplier<T> supplier, ElementsCount count, ElementState state);
}

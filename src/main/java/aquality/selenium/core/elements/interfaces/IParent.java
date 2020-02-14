package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;

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
}

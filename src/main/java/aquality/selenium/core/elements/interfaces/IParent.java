package aquality.selenium.core.elements.interfaces;

import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;

public interface IParent {

    /**
     * Find an element in the parent element
     *
     * @param childLoc Child element locator
     * @param clazz class or interface of the element to be obtained
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, Class<? extends IElement> clazz){
        return findChildElement(childLoc, clazz, ElementState.DISPLAYED);
    }

    /**
     * Find an element in the parent element
     *
     * @param childLoc Child element locator
     * @param clazz class or interface of the element to be obtained
     * @param state visibility state of target element
     * @return found child element
     */
    <T extends IElement> T findChildElement(By childLoc, Class<? extends IElement> clazz, ElementState state);

    /**
     * Finds an element in the parent element
     *
     * @param childLoc child element locator
     * @return child element
     */
    default <T extends IElement> T findChildElement(By childLoc) {
        return findChildElement(childLoc, null, null, null);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc child element locator
     * @param name     output name in logs
     * @return child element
     */
    default <T extends IElement> T findChildElement(By childLoc, String name) {
        return findChildElement(childLoc, name, null, null);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc Child element locator
     * @param state    visibility state of target element
     * @return child element
     */
    default <T extends IElement> T findChildElement(By childLoc, ElementState state) {
        return findChildElement(childLoc, null, null, state);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc Child element locator
     * @param supplier required element's supplier
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, IElementSupplier<T> supplier) {
        return findChildElement(childLoc, null, supplier, null);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc child element locator
     * @param name     output name in logs
     * @param supplier required element's supplier
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, String name, IElementSupplier<T> supplier) {
        return findChildElement(childLoc, name, supplier, null);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc child element locator
     * @param name     output name in logs
     * @param state    visibility state of target element
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, String name, ElementState state) {
        return findChildElement(childLoc, name, null, state);
    }

    /**
     * Finds an element in the parent element
     *
     * @param childLoc Child element locator
     * @param supplier required element's supplier
     * @param state    visibility state of target element
     * @return found child element
     */
    default <T extends IElement> T findChildElement(By childLoc, IElementSupplier<T> supplier, ElementState state) {
        return findChildElement(childLoc, null, supplier, state);
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
}

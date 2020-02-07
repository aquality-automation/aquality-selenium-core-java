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
     * Find an element in the parent element
     *
     * @param childLoc Child element locator
     * @param supplier required element's supplier
     * @param state visibility state of target element
     * @return found child element
     */
    <T extends IElement> T findChildElement(By childLoc, IElementSupplier<T> supplier, ElementState state);
}

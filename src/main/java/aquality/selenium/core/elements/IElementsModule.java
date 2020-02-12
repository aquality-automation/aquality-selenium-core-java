package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFactory;
import aquality.selenium.core.elements.interfaces.IElementFinder;

/**
 * Describes implementations of elements services to be registered in DI container.
 */
public interface IElementsModule {
    /**
     * @return class which implements {@link IElementFinder}
     */
    default Class<? extends IElementFinder> getElementFinderImplementation() {
        return ElementFinder.class;
    }

    /**
     * @return class which implements {@link IElementFactory}
     */
    default Class<? extends IElementFactory> getElementFactoryImplementation() {
        return ElementFactory.class;
    }
}

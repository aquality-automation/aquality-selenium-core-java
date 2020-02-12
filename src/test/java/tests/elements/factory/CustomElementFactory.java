package tests.elements.factory;

import aquality.selenium.core.elements.ElementFactory;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.IConditionalWait;

import java.util.HashMap;
import java.util.Map;

public class CustomElementFactory extends ElementFactory {
    CustomElementFactory(IConditionalWait conditionalWait, IElementFinder elementFinder, ILocalizationManager localizationManager) {
        super(conditionalWait, elementFinder, localizationManager);
    }

    @Override
    protected Map<Class<? extends IElement>, Class<? extends IElement>> getElementTypesMap() {
        Map<Class<? extends IElement>, Class<? extends IElement>> typesMap = new HashMap<>();
        typesMap.put(ICustomElement.class, CustomElement.class);
        typesMap.put(IWebCustomElement.class, CustomWebElement.class);
        return typesMap;
    }
}


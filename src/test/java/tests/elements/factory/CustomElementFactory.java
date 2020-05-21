package tests.elements.factory;

import aquality.selenium.core.elements.ElementFactory;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementSupplier;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.InvalidArgumentException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomElementFactory extends ElementFactory {
    private static final int XPATH_SUBSTRING_BEGIN_INDEX = 10;
    CustomElementFactory(IConditionalWait conditionalWait, IElementFinder elementFinder, ILocalizationManager localizationManager) {
        super(conditionalWait, elementFinder, localizationManager);
    }

    @Override
    protected Map<Class<? extends IElement>, Class<? extends IElement>> getElementTypesMap() {
        Map<Class<? extends IElement>, Class<? extends IElement>> typesMap = new HashMap<>();
        typesMap.put(ICustomElement.class, CustomElement.class);
        return typesMap;
    }

    @Override
    public <T extends IElement> List<T> findChildElements(IElement parentElement, By childLoc, String name,
                                                          IElementSupplier<T> supplier, ElementsCount count,
                                                          ElementState state) {
        By fullLocator = By.xpath(
                extractXPathFromLocator(parentElement.getLocator()).concat(extractXPathFromLocator(childLoc)));
        return findElements(fullLocator, name, supplier, count, state);
    }

    private String extractXPathFromLocator(By locator) {
        Class supportedLocatorType = ByXPath.class;
        if (locator.getClass().equals(supportedLocatorType)) {
            return locator.toString().substring(XPATH_SUBSTRING_BEGIN_INDEX);
        }
        throw new InvalidArgumentException(String.format(
                "Cannot define xpath from locator %1$s. Locator type %2$s is not %3$s, and is not supported yet",
                locator.toString(), locator.getClass(), supportedLocatorType));
    }
}


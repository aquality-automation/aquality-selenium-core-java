package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementFactory;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementSupplier;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.waitings.IConditionalWait;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ElementFactory implements IElementFactory {

    private static final int XPATH_SUBSTRING_BEGIN_INDEX = 10;
    private static final long ZERO_TIMEOUT = 0L;

    private final IConditionalWait conditionalWait;
    private final IElementFinder elementFinder;
    private final ILocalizationManager localizationManager;

    @Inject
    public ElementFactory(IConditionalWait conditionalWait, IElementFinder elementFinder, ILocalizationManager localizationManager) {
        this.conditionalWait = conditionalWait;
        this.elementFinder = elementFinder;
        this.localizationManager = localizationManager;
    }

    @Override
    public <T extends IElement> T getCustomElement(IElementSupplier<T> elementSupplier, By locator, String name, ElementState state) {
        return elementSupplier.get(locator, name, state);
    }

    @Override
    public <T extends IElement> T findChildElement(IElement parentElement, By childLoc, String name, Class<? extends IElement> clazz, ElementState state) {
        IElementSupplier<T> elementSupplier = getDefaultElementSupplier(clazz);
        return findChildElement(parentElement, childLoc, name, elementSupplier, state);
    }

    @Override
    public <T extends IElement> T findChildElement(IElement parentElement, By childLoc, String name, IElementSupplier<T> supplier, ElementState state) {
        String childName = name == null ? "Child element of ".concat(parentElement.getName()) : name;
        By fullLocator = new ByChained(parentElement.getLocator(), childLoc);
        return supplier.get(fullLocator, childName, state);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier,
                                                     ElementsCount count, ElementState state) {
        try {
            waitForElementsCount(locator, count, state);
        } catch (TimeoutException e) {
            throw new org.openqa.selenium.TimeoutException(e.getMessage());
        }
        List<WebElement> webElements = elementFinder.findElements(locator, state, ZERO_TIMEOUT);
        String namePrefix = name == null ? "element" : name;
        List<T> list = new ArrayList<>();
        for (int index = 1; index <= webElements.size(); index++) {
            WebElement webElement = webElements.get(index);
            T element = supplier.get(generateXpathLocator(locator, webElement, index), namePrefix, state);
            list.add(element);
        }
        return list;
    }

    protected void waitForElementsCount(By locator, ElementsCount count, ElementState state) throws TimeoutException {
        switch (count) {
            case ZERO:
                conditionalWait.waitForTrue(() -> elementFinder.findElements(locator, state, ZERO_TIMEOUT).isEmpty(),
                        localizationManager.getLocalizedMessage("loc.elements.found.but.should.not",
                                locator.toString(), state.toString()));
                break;
            case MORE_THEN_ZERO:
                conditionalWait.waitForTrue(() -> !elementFinder.findElements(locator, state, ZERO_TIMEOUT).isEmpty(),
                        localizationManager.getLocalizedMessage("loc.no.elements.found.by.locator",
                                locator.toString(), state.toString()));
                break;
            case ANY:
                conditionalWait.waitFor(() -> elementFinder.findElements(locator, state, ZERO_TIMEOUT) != null);
                break;
            default:
                throw new IllegalArgumentException("No such expected value: ".concat(count.toString()));
        }
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, String name, Class<? extends IElement> clazz,
                                                     ElementsCount count, ElementState state) {
        IElementSupplier<T> elementSupplier = getDefaultElementSupplier(clazz);
        return findElements(locator, name, elementSupplier, count, state);
    }

    /**
     * Generates xpath locator for target element.
     *
     * @param multipleElementsLocator locator used to find elements. Currently only {@link ByXPath} locators are supported.
     * @param webElement              target element.
     * @param elementIndex            index of target element.
     * @return target element's locator
     */
    protected By generateXpathLocator(By multipleElementsLocator, WebElement webElement, int elementIndex) {
        Class supportedLocatorType = ByXPath.class;
        if (multipleElementsLocator.getClass().equals(supportedLocatorType)) {
            return By.xpath(
                    String.format("(%1$s)[%2$s]", multipleElementsLocator.toString().substring(XPATH_SUBSTRING_BEGIN_INDEX), elementIndex));
        }
        throw new InvalidArgumentException(String.format(
                "Cannot define unique baseLocator for element %1$s. Multiple elements' baseLocator %2$s is not %3$s, and is not supported yet",
                webElement.toString(), multipleElementsLocator, supportedLocatorType));
    }

    /**
     * Gets map between elements interfaces and their implementations.
     * Can be extended for custom elements with custom interfaces.
     *
     * @return Map where key is interface and value is its implementation.
     */
    protected Map<Class<? extends IElement>, Class<? extends IElement>> getElementTypesMap() {
        return new HashMap<>();
    }

    protected Class<? extends IElement> resolveElementClass(Class<? extends IElement> clazz) {
        if (clazz.isInterface() && !getElementTypesMap().containsKey(clazz)) {
            throw new IllegalArgumentException(
                    String.format("Interface %1$s is not found in getElementTypesMap()", clazz));
        }

        return clazz.isInterface() ? getElementTypesMap().get(clazz) : clazz;
    }

    protected <T extends IElement> IElementSupplier<T> getDefaultElementSupplier(Class<? extends IElement> clazz) {
        return (locator, name, state) -> {
            try {
                Class<? extends IElement> type = resolveElementClass(clazz);
                Constructor ctor = type.getDeclaredConstructor(By.class, String.class, ElementState.class);
                return (T) ctor.newInstance(locator, name, state);
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Logger.getInstance().debug(e.getMessage());
                throw new IllegalArgumentException("Something went wrong during element casting");
            }
        };
    }
}

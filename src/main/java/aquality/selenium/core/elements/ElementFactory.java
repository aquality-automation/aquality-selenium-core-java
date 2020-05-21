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
import org.openqa.selenium.By.ByTagName;
import org.openqa.selenium.By.ByXPath;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ElementFactory implements IElementFactory {

    private static final int XPATH_SUBSTRING_BEGIN_INDEX = 10;
    private static final int TAGNAME_SUBSTRING_BEGIN_INDEX = 12;
    private static final String TAGNAME_XPATH_PREFIX = "//";
    private static final Duration ZERO_TIMEOUT = Duration.ZERO;

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
    public <T extends IElement> T getCustomElement(Class<T> clazz, By locator, String name, ElementState state) {
        IElementSupplier<T> elementSupplier = getDefaultElementSupplier(clazz);
        return getCustomElement(elementSupplier, locator, name, state);
    }

    @Override
    public <T extends IElement> T findChildElement(IElement parentElement, By childLoc, String name, Class<T> clazz, ElementState state) {
        IElementSupplier<T> elementSupplier = getDefaultElementSupplier(clazz);
        return findChildElement(parentElement, childLoc, name, elementSupplier, state);
    }

    @Override
    public <T extends IElement> T findChildElement(IElement parentElement, By childLoc, String name, IElementSupplier<T> supplier, ElementState state) {
        String childName = name == null ? "Child element of ".concat(parentElement.getName()) : name;
        By fullLocator = generateAbsoluteChildLocator(parentElement.getLocator(), childLoc);
        return supplier.get(fullLocator, childName, state);
    }

    @Override
    public <T extends IElement> List<T> findChildElements(IElement parentElement, By childLoc, String name, Class<T> clazz, ElementsCount count, ElementState state) {
        IElementSupplier<T> elementSupplier = getDefaultElementSupplier(clazz);
        return findChildElements(parentElement, childLoc, name, elementSupplier, count, state);
    }

    @Override
    public <T extends IElement> List<T> findChildElements(IElement parentElement, By childLoc, String name, IElementSupplier<T> supplier, ElementsCount count, ElementState state) {
        String childName = name == null ? "Child element of ".concat(parentElement.getName()) : name;
        By fullLocator = generateAbsoluteChildLocator(parentElement.getLocator(), childLoc);
        return findElements(fullLocator, childName, supplier, count, state);
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
            WebElement webElement = webElements.get(index - 1);
            String currentName = String.format("%1$s %2$s", namePrefix, index);
            T element = supplier.get(generateXpathLocator(locator, webElement, index), currentName, state);
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
    public <T extends IElement> List<T> findElements(By locator, String name, Class<T> clazz,
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
        if (isLocatorSupportedForXPathGeneration(multipleElementsLocator)) {
            return By.xpath(
                    String.format("(%1$s)[%2$s]", extractXPathFromLocator(multipleElementsLocator), elementIndex));
        }
        throw new InvalidArgumentException(String.format(
                "Cannot define unique baseLocator for element %1$s. Multiple elements' baseLocator type %2$s is not supported yet",
                webElement.toString(), multipleElementsLocator.getClass()));
    }

    /**
     * Extracts XPath from passed locator.
     * Current implementation works only with ByXPath.class and ByTagName locator types,
     * but you can implement your own for the specific WebDriver type.
     *
     * @param locator locator to get xpath from.
     * @return extracted XPath.
     */
    protected String extractXPathFromLocator(By locator) {
        Class supportedLocatorType = ByXPath.class;
        if (locator.getClass().equals(supportedLocatorType)) {
            return locator.toString().substring(XPATH_SUBSTRING_BEGIN_INDEX);
        }
        if (locator.getClass().equals(ByTagName.class)){
            return TAGNAME_XPATH_PREFIX + locator.toString().substring(TAGNAME_SUBSTRING_BEGIN_INDEX);
        }
        throw new InvalidArgumentException(String.format(
                "Cannot define xpath from locator %1$s. Locator type %2$s is not %3$s, and is not supported yet",
                locator.toString(), locator.getClass(), supportedLocatorType));
    }

    /**
     * Generates absolute child locator for target element.
     *
     * @param parentLoc parent locator
     * @param childLoc child locator relative to parent
     * @return absolute locator of the child
     */
    protected By generateAbsoluteChildLocator(By parentLoc, By childLoc) {
        return isLocatorSupportedForXPathGeneration(parentLoc)
                && isLocatorSupportedForXPathGeneration(childLoc)
                && !extractXPathFromLocator(childLoc).startsWith(".")
                ? By.xpath(extractXPathFromLocator(parentLoc).concat(extractXPathFromLocator(childLoc)))
                : new ByChained(parentLoc, childLoc);
    }

    /**
     * Defines is the locator can be transformed to xpath or not.
     * Current implementation works only with ByXPath.class and ByTagName locator types,
     * but you can implement your own for the specific WebDriver type.
     *
     * @param locator locator to transform
     * @return true if the locator can be transformed to xpath, false otherwise.
     */
    protected boolean isLocatorSupportedForXPathGeneration(By locator) {
        return locator.getClass().equals(ByXPath.class) || locator.getClass().equals(ByTagName.class);
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

    protected <T extends IElement> Class<T> resolveElementClass(Class<T> clazz) {
        if (clazz.isInterface() && !getElementTypesMap().containsKey(clazz)) {
            throw new IllegalArgumentException(
                    String.format("Interface %1$s is not found in getElementTypesMap()", clazz));
        }

        return clazz.isInterface() ? (Class<T>) getElementTypesMap().get(clazz) : clazz;
    }

    protected <T extends IElement> IElementSupplier<T> getDefaultElementSupplier(Class<T> clazz) {
        return (locator, name, state) -> {
            try {
                Constructor<T> ctor = resolveElementClass(clazz)
                        .getDeclaredConstructor(By.class, String.class, ElementState.class);
                ctor.setAccessible(true);
                T instance = ctor.newInstance(locator, name, state);
                ctor.setAccessible(false);
                return instance;
            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                Logger.getInstance().debug(e.getMessage());
                throw new IllegalArgumentException("Something went wrong during element casting");
            }
        };
    }
}

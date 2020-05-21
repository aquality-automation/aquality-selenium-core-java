package tests.elements.factory;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.ElementsCount;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementSupplier;
import org.openqa.selenium.By;
import tests.ITestWithApplication;
import tests.applications.windowsApp.AqualityServices;
import tests.applications.windowsApp.CalculatorWindow;

import java.util.List;

public class FindChildElementsTests implements ITestWithApplication, IFindElementsTests  {

    @Override
    public IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    @Override
    public boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    private IElement getParent() {
        return CalculatorWindow.getWindowByXPathLabel();
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, Class<T> clazz, ElementsCount count) {
        return customFactory().findChildElements(getParent(), locator, clazz, count);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, Class<T> clazz) {
        return customFactory().findChildElements(getParent(), locator, clazz);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, String name, Class<T> clazz) {
        return customFactory().findChildElements(getParent(), locator, name, clazz);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, String name, IElementSupplier<T> supplier, ElementsCount count, ElementState state) {
        return customFactory().findChildElements(getParent(), locator, name, supplier, count, state);
    }

    @Override
    public <T extends IElement> List<T> findElements(By locator, IElementSupplier<T> supplier, ElementsCount count, ElementState state) {
        return customFactory().findChildElements(getParent(), locator, supplier, count, state);
    }
}

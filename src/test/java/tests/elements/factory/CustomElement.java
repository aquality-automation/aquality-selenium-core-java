package tests.elements.factory;

import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.elements.interfaces.IElementSupplier;
import org.apache.commons.lang3.NotImplementedException;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebElement;

public class CustomElement implements ICustomElement {

    private final By locator;
    private final String name;
    private final ElementState state;

    protected CustomElement(By locator, String name, ElementState state) {
        this.locator = locator;
        this.name = name;
        this.state = state;
    }

    @Override
    public RemoteWebElement getElement(Long timeout) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public By getLocator() {
        return locator;
    }

    @Override
    public <T extends IElement> T findChildElement(By childLoc, Class<? extends IElement> clazz, ElementState state) {
        throw new NotImplementedException("not implemented in tests");
    }

    @Override
    public <T extends IElement> T findChildElement(By childLoc, IElementSupplier<T> supplier, ElementState state) {
        throw new NotImplementedException("not implemented in tests");
    }

    public ElementState getState() {
        return state;
    }
}

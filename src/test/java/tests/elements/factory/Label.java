package tests.elements.factory;

import aquality.selenium.core.elements.ElementState;
import org.openqa.selenium.By;

public class Label extends CustomWebElement {
    public Label(By locator, String name, ElementState state) {
        super(locator, name, state);
    }

    @Override
    protected String getElementType() {
        return "Label";
    }
}

package tests.elements;

import aquality.selenium.core.elements.ElementStateProvider;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.application.browser.AqualityServices;

public class ElementStateProviderTests implements IWebElementStateProviderTests {

    @Override
    public IElementStateProvider state(By locator) {
        return new ElementStateProvider(locator,
                AqualityServices.get(IConditionalWait.class),
                AqualityServices.get(IElementFinder.class));
    }
}

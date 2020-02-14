package tests.elements;

import aquality.selenium.core.elements.DefaultElementStateProvider;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.applications.browser.AqualityServices;

public class DefaultElementStateProviderTests implements IWebElementStateProviderTests {

    @Override
    public IElementStateProvider state(By locator) {
        return new DefaultElementStateProvider(locator,
                AqualityServices.get(IConditionalWait.class),
                AqualityServices.get(IElementFinder.class));
    }
}

package tests.elements;

import aquality.selenium.core.elements.RelativeElementFinder;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.application.windowsApp.AqualityServices;
import tests.application.windowsApp.CalculatorWindow;

public class RelativeElementFinderTests implements IElementFinderTests {

    @Override
    public IElementFinder getElementFinder() {
        return new RelativeElementFinder(
                AqualityServices.get(ILocalizedLogger.class),
                AqualityServices.get(IConditionalWait.class),
                () -> getApplication().getDriver().findElement(CalculatorWindow.getWindowLocator()));
    }

    @Test
    public void shouldFindChildElements() {
        Assert.assertNotNull(getElementFinder().findElement(CalculatorWindow.getOneButton()));
    }
}

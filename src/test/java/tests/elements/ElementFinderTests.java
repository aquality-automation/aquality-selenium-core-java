package tests.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.waitings.IConditionalWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.applications.windowsApp.AqualityServices;
import tests.applications.windowsApp.CalculatorWindow;

import java.util.function.BooleanSupplier;

public class ElementFinderTests implements IElementFinderTests {

    @Override
    public IElementFinder getElementFinder() {
        return AqualityServices.get(IElementFinder.class);
    }

    @Test
    public void shouldBePossibleToUseConditionalWaitWithElementFinder() {
        BooleanSupplier elementFinderCondition = () ->
                getElementFinder().findElement(CalculatorWindow.getResultsLabelLoc()).getText().contains("3");
        Assert.assertFalse(elementFinderCondition.getAsBoolean(), "condition should not match before actions");
        Assert.assertTrue(AqualityServices.get(IConditionalWait.class)
                .waitFor(driver -> {
                    driver.findElement(CalculatorWindow.getTwoButtonLoc()).click();
                    driver.findElement(CalculatorWindow.getPlusButtonLoc()).click();
                    driver.findElement(CalculatorWindow.getOneButtonLoc()).click();
                    driver.findElement(CalculatorWindow.getEqualsButtonLoc()).click();
                    return elementFinderCondition.getAsBoolean();
                }));
    }
}

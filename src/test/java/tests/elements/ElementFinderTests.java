package tests.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.waitings.IConditionalWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.application.windowsApp.AqualityServices;
import tests.application.windowsApp.CalculatorWindow;

import java.util.function.BooleanSupplier;

public class ElementFinderTests implements IElementFinderTests {

    @Override
    public IElementFinder getElementFinder() {
        return AqualityServices.get(IElementFinder.class);
    }

    @Test
    public void shouldBePossibleToUseConditionalWaitWithElementFinder() {
        BooleanSupplier elementFinderCondition = () ->
                getElementFinder().findElement(CalculatorWindow.getResultsLabel()).getText().contains("3");
        Assert.assertFalse(elementFinderCondition.getAsBoolean(), "condition should not match before actions");
        Assert.assertTrue(AqualityServices.get(IConditionalWait.class)
                .waitFor(driver -> {
                    driver.findElement(CalculatorWindow.getTwoButton()).click();
                    driver.findElement(CalculatorWindow.getPlusButton()).click();
                    driver.findElement(CalculatorWindow.getOneButton()).click();
                    driver.findElement(CalculatorWindow.getEqualsButton()).click();
                    return elementFinderCondition.getAsBoolean();
                }));
    }
}

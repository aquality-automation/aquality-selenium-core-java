package tests.applications.windowsApp;

import aquality.selenium.core.elements.ElementState;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import tests.elements.factory.CustomElement;

public class CalculatorWindow {
    private CalculatorWindow(){
    }

    public static By getWindowLocator() {
        return By.tagName("Window");
    }

    public static CustomElement getWindowLabel() {
        return new CustomElement(getWindowLocator(), "Window", ElementState.DISPLAYED);
    }

    public static By getOneButtonLoc() {
        return By.name("1");
    }

    public static CustomElement getOneButton() {
        return new CustomElement(getOneButtonLoc(), "1", ElementState.DISPLAYED);
    }

    public static By getTwoButtonLoc() {
        return By.name("2");
    }

    public static By getPlusButtonLoc() {
        return By.name("+");
    }

    public static By getEqualsButtonLoc() {
        return By.name("=");
    }

    public static By getEqualsButtonByXPath() {
        return By.xpath("//*[@Name='=']");
    }

    public static By getResultsLabelLoc() {
        return MobileBy.AccessibilityId("48");
    }

    public static CustomElement getLeftValueTextBox() {
        return new CustomElement(MobileBy.xpath("//*[@AutomationId='50']"), "Left value", ElementState.DISPLAYED);
    }
}

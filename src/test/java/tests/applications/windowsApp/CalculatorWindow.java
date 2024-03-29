package tests.applications.windowsApp;

import aquality.selenium.core.elements.ElementState;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import tests.elements.factory.CustomElement;

public class CalculatorWindow {
    private CalculatorWindow(){
    }

    public static By getWindowLocator() {
        return By.xpath("//Window");
    }

    public static CustomElement getWindowLabel() {
        return new CustomElement(getWindowLocator(), "Window", ElementState.DISPLAYED);
    }

    public static CustomElement getWindowByXPathLabel() {
        return new CustomElement(By.xpath("//Window"), "Window", ElementState.DISPLAYED);
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

    public static By getRelativeXPathLocator() {
        return By.xpath("//*[@Name='=']");
    }

    public static By getDottedXPathLocator() {
        return By.xpath("//*[@Name='=']/parent::Window");
    }

    public static By getTagXpathLocator() {
        return By.xpath("//Button");
    }

    public static By getResultsLabelLoc() {
        return AppiumBy.accessibilityId("48");
    }

    public static CustomElement getLeftValueTextBox() {
        return new CustomElement(AppiumBy.xpath("//*[@AutomationId='50']"), "Left value", ElementState.DISPLAYED);
    }
}

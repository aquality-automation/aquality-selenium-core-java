package tests.application.windowsApp;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public class CalculatorWindow {
    private CalculatorWindow(){
    }

    public static By getWindowLocator() {
        return By.tagName("Window");
    }

    public static By getOneButton() {
        return By.name("1");
    }

    public static By getTwoButton() {
        return By.name("2");
    }

    public static By getPlusButton() {
        return By.name("+");
    }

    public static By getEqualsButton() {
        return By.name("=");
    }

    public static By getResultsLabel() {
        return MobileBy.AccessibilityId("48");
    }
}

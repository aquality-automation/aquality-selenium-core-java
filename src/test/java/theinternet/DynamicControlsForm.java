package theinternet;

import org.openqa.selenium.By;

public class DynamicControlsForm {

    private DynamicControlsForm(){
    }

    public static By getEnableButtonLocator() {
        return By.xpath("//button[contains(@onclick, 'swapInput()')][contains(.,'Enable')]");
    }

    public static By getInputTextboxLocator() {
        return By.xpath("//input[@type='text']");
    }

    public static By getCheckboxLocator() {
        return By.xpath("//div[@id='checkbox']");
    }

    public static By getRemoveButtonLocator() {
        return By.xpath("//button[contains(@onclick, 'swapCheckbox()')]");
    }
}

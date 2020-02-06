package theinternet;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.By;

import java.util.function.Function;
import java.util.function.Supplier;

public class DynamicControlsForm extends BaseForm {
    private static final By ENABLE_BUTTON_LOCATOR = By.xpath("//button[contains(@onclick, 'swapInput()')][contains(.,'Enable')]");
    private static final By REMOVE_BUTTON_LOCATOR = By.xpath("//button[contains(@onclick, 'swapCheckbox()')]");
    private static final By INPUT_TEXTBOX_LOCATOR = By.xpath("//input[@type='text']");
    private static final By CHECKBOX_LOCATOR = By.xpath("//div[@id='checkbox']");

    public DynamicControlsForm(Supplier<IApplication> appSupplier, Function<By, IElementStateProvider> stateProviderFunction) {
        super(appSupplier, stateProviderFunction);
    }

    public void clickEnable() {
        click(ENABLE_BUTTON_LOCATOR);
    }

    public void clickRemove() {
        click(REMOVE_BUTTON_LOCATOR);
    }

    public IElementStateProvider inputState() {
        return state(INPUT_TEXTBOX_LOCATOR);
    }

    public IElementStateProvider checkboxState() {
        return state(CHECKBOX_LOCATOR);
    }
}

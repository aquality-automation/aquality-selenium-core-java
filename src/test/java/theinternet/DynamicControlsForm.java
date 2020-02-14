package theinternet;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.By;
import tests.applications.browser.CachedLabel;
import tests.elements.factory.CustomWebElement;

import java.util.function.Function;
import java.util.function.Supplier;

public class DynamicControlsForm extends BaseForm {
    private static final By ENABLE_BUTTON_LOCATOR = By.xpath("//button[contains(@onclick, 'swapInput()')][contains(.,'Enable')]");
    private static final By LOADING_LABEL_LOCATOR = By.id("loading");
    private static final By REMOVE_BUTTON_LOCATOR = By.xpath("//button[contains(@onclick, 'swapCheckbox()')]");
    private static final By INPUT_TEXTBOX_LOCATOR = By.xpath("//input[@type='text']");
    private static final By CHECKBOX_LOCATOR = By.xpath("//div[@id='checkbox']");
    private static final By CONTENT_LOCATOR = By.id("content");

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

    public static CachedLabel getContentCachedLabel() {
        return new CachedLabel(CONTENT_LOCATOR, ElementState.DISPLAYED);
    }

    public static By getCheckboxLocator() {
        return CHECKBOX_LOCATOR;
    }

    public static CachedLabel getCheckboxLabel() {
        return new CachedLabel(CHECKBOX_LOCATOR, ElementState.EXISTS_IN_ANY_STATE);
    }

    public static CachedLabel getRemoveLabel() {
        return new CachedLabel(REMOVE_BUTTON_LOCATOR, ElementState.EXISTS_IN_ANY_STATE);
    }

    public static CustomWebElement getEnableButton() {
        return new CustomWebElement(ENABLE_BUTTON_LOCATOR, "Enable", ElementState.EXISTS_IN_ANY_STATE);
    }

    public static CustomWebElement getRemoveButton() {
        return new CustomWebElement(REMOVE_BUTTON_LOCATOR, "Remove", ElementState.EXISTS_IN_ANY_STATE);
    }

    public static CustomWebElement getLoadingLabel() {
        return new CustomWebElement(LOADING_LABEL_LOCATOR, "Loading", ElementState.DISPLAYED);
    }

    public static CustomWebElement getContentLabel() {
        return new CustomWebElement(CONTENT_LOCATOR, "Content", ElementState.DISPLAYED);
    }
}

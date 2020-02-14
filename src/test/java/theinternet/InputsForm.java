package theinternet;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.By;
import tests.elements.factory.CustomWebElement;

import java.util.function.Function;
import java.util.function.Supplier;

public class InputsForm extends BaseForm {

    private static final CustomWebElement INPUT_TEXT_BOX = new CustomWebElement(By.xpath("//input[@type='number']"), "Input", ElementState.EXISTS_IN_ANY_STATE);

    public InputsForm(Supplier<IApplication> appSupplier, Function<By, IElementStateProvider> stateProviderFunction) {
        super(appSupplier, stateProviderFunction);
    }

    public static CustomWebElement getInputTextBox(){
        return INPUT_TEXT_BOX;
    }
}

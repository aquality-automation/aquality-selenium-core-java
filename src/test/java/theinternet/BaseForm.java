package theinternet;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.By;
import java.util.function.Function;
import java.util.function.Supplier;

abstract class BaseForm {

    private final Supplier<IApplication> appSupplier;
    private final Function<By, IElementStateProvider> stateProviderFunction;

    BaseForm(Supplier<IApplication> appSupplier, Function<By, IElementStateProvider> stateProviderFunction) {
        this.appSupplier = appSupplier;
        this.stateProviderFunction = stateProviderFunction;
    }

    void click(By locator) {
        locator.findElement(appSupplier.get().getDriver()).click();
    }

    IElementStateProvider state(By locator) {
        return stateProviderFunction.apply(locator);
    }
}

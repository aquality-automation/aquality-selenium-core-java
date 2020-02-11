package theinternet;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.By;
import tests.application.browser.CachedLabel;

import java.util.function.Function;
import java.util.function.Supplier;

public class DynamicLoadingForm extends BaseForm {

    private static final long DEFAULT_LOADING_TIMEOUT = 5L;
    private static final long SMALL_LOADING_TIMEOUT = 2L;
    private static final By LOADING_LABEL_LOCATOR = By.id("loading");
    private static final By START_BUTTON_LOCATOR = By.xpath("//div[@id='start']/button");

    public DynamicLoadingForm(Supplier<IApplication> appSupplier, Function<By, IElementStateProvider> stateProviderFunction) {
        super(appSupplier, stateProviderFunction);
    }

    public static CachedLabel getLoadingLabel() {
        return new CachedLabel(LOADING_LABEL_LOCATOR, ElementState.DISPLAYED);
    }

    public static CachedLabel getStartLabel() {
        return new CachedLabel(START_BUTTON_LOCATOR, ElementState.DISPLAYED);
    }

    public long getTimeout() {
        return DEFAULT_LOADING_TIMEOUT;
    }

    public long getSmallTimeout() {
        return SMALL_LOADING_TIMEOUT;
    }

    public void clickStart() {
        click(START_BUTTON_LOCATOR);
    }

    public IElementStateProvider loaderState() {
        return state(LOADING_LABEL_LOCATOR);
    }

    public IElementStateProvider startButtonState() {
        return state(START_BUTTON_LOCATOR);
    }
}

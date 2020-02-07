package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class ElementStateProvider implements IElementStateProvider {
    static final long ZERO_TIMEOUT = 0L;
    protected final By locator;
    protected final IConditionalWait conditionalWait;

    protected ElementStateProvider(By locator, IConditionalWait conditionalWait) {
        this.locator = locator;
        this.conditionalWait = conditionalWait;
    }

    protected boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    protected DesiredState elementEnabled() {
        return new DesiredState(this::isElementEnabled, "ENABLED")
                .withCatchingTimeoutException()
                .withThrowingNoSuchElementException();
    }

    protected DesiredState elementNotEnabled() {
        return new DesiredState(element -> !isElementEnabled(element), "NOT ENABLED")
                .withCatchingTimeoutException()
                .withThrowingNoSuchElementException();
    }

    protected DesiredState elementClickable() {
        return new DesiredState(webElement -> webElement.isDisplayed() && webElement.isEnabled(), "CLICKABLE");
    }
}

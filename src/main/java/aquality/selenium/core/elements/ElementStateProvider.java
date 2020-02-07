package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.openqa.selenium.WebElement;

public abstract class ElementStateProvider implements IElementStateProvider {
    private static final long ZERO_TIMEOUT = 0L;

    protected Long getZeroTImeout() {
        return ZERO_TIMEOUT;
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

package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.elements.interfaces.ILogElementState;
import org.openqa.selenium.WebElement;

public abstract class ElementStateProvider implements IElementStateProvider {

    private final ILogElementState logger;

    protected ElementStateProvider(ILogElementState logger) {
        this.logger = logger;
    }

    protected void logElementState(String messageKey, String conditionKeyPart) {
        String conditionKey = "loc.el.state.".concat(conditionKeyPart);
        logger.logElementState(messageKey, conditionKey);
    }

    protected boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    protected DesiredState elementEnabled() {
        return new DesiredState(this::isElementEnabled, "enabled")
                .withCatchingTimeoutException()
                .withThrowingNoSuchElementException();
    }

    protected DesiredState elementNotEnabled() {
        return new DesiredState(element -> !isElementEnabled(element), "not.enabled")
                .withCatchingTimeoutException()
                .withThrowingNoSuchElementException();
    }

    protected DesiredState elementClickable() {
        return new DesiredState(webElement -> webElement.isDisplayed() && webElement.isEnabled(), "clickable");
    }
}

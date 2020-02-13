package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import com.google.inject.Inject;
import org.openqa.selenium.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Implementation of IElementFinder.
 */
public class ElementFinder implements IElementFinder {
    private final ILocalizedLogger localizedLogger;
    private final IConditionalWait conditionalWait;

    @Inject
    public ElementFinder(ILocalizedLogger localizedLogger, IConditionalWait conditionalWait) {
        this.localizedLogger = localizedLogger;
        this.conditionalWait = conditionalWait;
    }

    @Override
    public List<WebElement> findElements(By locator, DesiredState desiredState, Long timeoutInSeconds) {
        AtomicBoolean wasAnyElementFound = new AtomicBoolean(false);
        List<WebElement> resultElements = new ArrayList<>();
        try {
            conditionalWait.waitFor(driver ->
                            tryToFindElements(locator, desiredState, wasAnyElementFound, resultElements, driver),
                    timeoutInSeconds == null ? null : Duration.ofSeconds(timeoutInSeconds));
        } catch (TimeoutException e) {
            handleTimeoutException(e, locator, desiredState, wasAnyElementFound.get());
        }

        return resultElements;
    }

    protected boolean tryToFindElements(By locator, DesiredState desiredState, AtomicBoolean wasAnyElementFound,
                                        List<WebElement> resultElements, SearchContext context) {
        List<WebElement> currentFoundElements = context.findElements(locator);
        wasAnyElementFound.set(!currentFoundElements.isEmpty());
        currentFoundElements
                .stream()
                .filter(desiredState.getElementStateCondition())
                .forEachOrdered(resultElements::add);
        return !resultElements.isEmpty();
    }

    /**
     * depends on configuration of DesiredState object it can be required to throw or not NoSuchElementException
     *
     * @param exception          TimeoutException to handle
     * @param locator            locator that is using to find elements
     * @param desiredState       DesiredState object
     * @param wasAnyElementFound was any element found by locator or not.
     */
    protected void handleTimeoutException(TimeoutException exception, By locator, DesiredState desiredState, boolean wasAnyElementFound) {
        String message = String.format("No elements with locator '%1$s' were found in %2$s state", locator, desiredState.getStateName());
        if (desiredState.isCatchingInTimeoutException()) {
            if (!wasAnyElementFound) {
                if (desiredState.isThrowingNoSuchElementException()) {
                    throw new NoSuchElementException(message);
                }
                localizedLogger.debug("loc.no.elements.found.in.state", locator, desiredState.getStateName());
            } else {
                localizedLogger.debug("loc.elements.were.found.but.not.in.state", locator, desiredState.getStateName());
            }
        } else {
            String combinedMessage = String.format("%1$s: %2$s", message, exception.getMessage());
            if (desiredState.isThrowingNoSuchElementException() && !wasAnyElementFound) {
                throw new NoSuchElementException(combinedMessage);
            }
            throw new TimeoutException(combinedMessage);
        }
    }
}

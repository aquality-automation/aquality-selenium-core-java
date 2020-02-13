package aquality.selenium.core.elements;

import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

/**
 * Implementation of IElementFinder for a relative SearchContext.
 */
public class RelativeElementFinder extends ElementFinder {
    private final IConditionalWait conditionalWait;
    private final Supplier<SearchContext> searchContextSupplier;

    public RelativeElementFinder(ILocalizedLogger localizedLogger, IConditionalWait conditionalWait,
                                 Supplier<SearchContext> searchContextSupplier) {
        super(localizedLogger, conditionalWait);
        this.conditionalWait = conditionalWait;
        this.searchContextSupplier = searchContextSupplier;
    }

    @Override
    public List<WebElement> findElements(By locator, DesiredState desiredState, Long timeoutInSeconds) {
        AtomicBoolean wasAnyElementFound = new AtomicBoolean(false);
        List<WebElement> resultElements = new ArrayList<>();
        try {
            conditionalWait.waitForTrue(() ->
                            tryToFindElements(locator, desiredState, wasAnyElementFound, resultElements,
                                    searchContextSupplier.get()),
                    timeoutInSeconds == null ? null : Duration.ofSeconds(timeoutInSeconds));
        } catch (TimeoutException e) {
            handleTimeoutException(new org.openqa.selenium.TimeoutException(e.getMessage(), e), locator, desiredState,
                    wasAnyElementFound.get());
        } catch (org.openqa.selenium.TimeoutException e) {
            handleTimeoutException(e, locator, desiredState, wasAnyElementFound.get());
        }

        return resultElements;
    }
}

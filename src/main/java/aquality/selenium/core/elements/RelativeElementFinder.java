package aquality.selenium.core.elements;

import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
        List<WebElement> foundElements = new ArrayList<>();
        List<WebElement> resultElements = new ArrayList<>();
        try {
            conditionalWait.waitForTrue(() -> {
                List<WebElement> currentFoundElements = searchContextSupplier.get().findElements(locator);
                foundElements.addAll(currentFoundElements);
                List<WebElement> filteredElements = currentFoundElements
                        .stream()
                        .filter(desiredState.getElementStateCondition())
                        .collect(Collectors.toList());
                resultElements.addAll(filteredElements);
                return !filteredElements.isEmpty();
            }, timeoutInSeconds, null);
        } catch (TimeoutException e) {
            handleTimeoutException(new org.openqa.selenium.TimeoutException(e.getMessage(), e), locator, desiredState,
                    foundElements);
        }

        return resultElements;
    }
}

package aquality.selenium.core.elements;

import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import com.google.inject.Inject;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<WebElement> foundElements = new ArrayList<>();
        List<WebElement> resultElements = new ArrayList<>();
        try {
            conditionalWait.waitFor(driver -> {
                List<WebElement> currentFoundElements = driver == null ? new ArrayList<>() : driver.findElements(locator);
                foundElements.addAll(currentFoundElements);
                List<WebElement> filteredElements = currentFoundElements
                        .stream()
                        .filter(desiredState.getElementStateCondition())
                        .collect(Collectors.toList());
                resultElements.addAll(filteredElements);
                return !filteredElements.isEmpty();
            }, timeoutInSeconds, 100, null, null); //todo: rebase and use proper method
        }
        catch (TimeoutException e) {
            applyResult(locator, desiredState, foundElements);
        }

        return resultElements;
    }

    /**
     * depends on configuration of DesiredState object it can be required to throw or not NoSuchElementException
     * @param locator locator that is using to find elements
     * @param desiredState DesiredState object
     * @param foundElements list of all found elements by locator.
     */
    private void applyResult(By locator, DesiredState desiredState, List<WebElement> foundElements){
        String message = String.format("No elements with locator '%1$s' were found in %2$s state", locator, desiredState.getStateName());
        if (desiredState.isCatchingInTimeoutException()){
            if(foundElements.isEmpty()){
                if(desiredState.isThrowingNoSuchElementException()){
                    throw new NoSuchElementException(message);
                }
                localizedLogger.debug("loc.no.elements.found.in.state", locator, desiredState.getStateName());
            }else {
                localizedLogger.debug("loc.elements.were.found.but.not.in.state", locator, desiredState.getStateName());
            }
        }else {
            throw new TimeoutException(desiredState.getStateName());
        }
    }
}

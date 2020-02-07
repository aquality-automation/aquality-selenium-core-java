package aquality.selenium.core.elements;

import org.openqa.selenium.WebElement;

import java.util.function.Predicate;

/**
 * Defines desired state for element with ability to handle exceptions.
 */
public class DesiredState {

    private final Predicate<WebElement> desiredStatePredicate;
    private final String stateName;
    private boolean isCatchingTimeoutException;
    private boolean isThrowingNoSuchElementException;

    public DesiredState(Predicate<WebElement> desiredStatePredicate, String stateName){
        this.desiredStatePredicate = desiredStatePredicate;
        this.stateName = stateName;
    }

    public Predicate<WebElement> getElementStateCondition() {
        return desiredStatePredicate;
    }

    public String getStateName() {
        return stateName;
    }

    public DesiredState withCatchingTimeoutException(){
        this.isCatchingTimeoutException = true;
        return this;
    }

    public DesiredState withThrowingNoSuchElementException(){
        this.isThrowingNoSuchElementException = true;
        return this;
    }

    public boolean isCatchingInTimeoutException() {
        return isCatchingTimeoutException;
    }

    public boolean isThrowingNoSuchElementException() {
        return isThrowingNoSuchElementException;
    }
}

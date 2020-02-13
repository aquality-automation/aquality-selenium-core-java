package tests.elements;

import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import org.testng.annotations.DataProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface ICachedElementTests {
    Duration ZERO_TIMEOUT = Duration.ZERO;

    @DataProvider
    default Object[] stateFunctionsFalseWhenElementStale() {
        List<Predicate<IElementStateProvider>> stateFunctions = new ArrayList<>();
        stateFunctions.add(IElementStateProvider::isDisplayed);
        stateFunctions.add(IElementStateProvider::isExist);
        stateFunctions.add(state -> !state.waitForNotDisplayed(ZERO_TIMEOUT));
        stateFunctions.add(state -> !state.waitForNotExist(ZERO_TIMEOUT));
        return stateFunctions.toArray();
    }

    @DataProvider
    default Object[] stateFunctionsTrueWhenElementStaleWhichRetriveElement() {
        List<Predicate<IElementStateProvider>> stateFunctions = new ArrayList<>();
        stateFunctions.add(IElementStateProvider::isEnabled);
        stateFunctions.add(IElementStateProvider::isClickable);
        stateFunctions.add(state -> state.waitForDisplayed(ZERO_TIMEOUT));
        stateFunctions.add(state -> state.waitForExist(ZERO_TIMEOUT));
        stateFunctions.add(state -> state.waitForEnabled(ZERO_TIMEOUT));
        stateFunctions.add(state -> !state.waitForNotEnabled(ZERO_TIMEOUT));
        return stateFunctions.toArray();
    }
}

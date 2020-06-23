package tests.elements;

import aquality.selenium.core.elements.CachedElementStateProvider;
import aquality.selenium.core.elements.ElementCacheHandler;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementFinder;
import aquality.selenium.core.elements.interfaces.IElementStateProvider;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.waitings.IConditionalWait;
import org.openqa.selenium.By;
import tests.applications.browser.AqualityServices;

public class CachedElementStateProviderTests implements IWebElementStateProviderTests {
    
    @Override
    public IElementStateProvider state(By locator) {
        return new CachedElementStateProvider(locator,
                AqualityServices.get(IConditionalWait.class),
                new ElementCacheHandler(locator, ElementState.EXISTS_IN_ANY_STATE, AqualityServices.get(IElementFinder.class)),
                (messageKey, stateKey) ->
                        AqualityServices.get(ILocalizedLogger.class)
                                .infoElementAction("Element", locator.toString(), messageKey,
                                        AqualityServices.get(ILocalizationManager.class).getLocalizedMessage(stateKey)));
    }

}

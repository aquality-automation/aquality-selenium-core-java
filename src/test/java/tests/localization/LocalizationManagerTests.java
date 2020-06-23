package tests.localization;

import aquality.selenium.core.configurations.ILoggerConfiguration;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.localization.LocalizationManager;
import aquality.selenium.core.logging.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.applications.CustomAqualityServices;
import tests.applications.browser.AqualityServices;

import java.util.MissingFormatArgumentException;

import static org.testng.Assert.assertEquals;

public class LocalizationManagerTests {
    private static final String[] SUPPORTED_LANGUAGES = new String[]{"be", "en", "ru"};
    private static final String CLICKING_MESSAGE_KEY = "loc.clicking";
    private static final String CLICKING_VALUE_BE = "Націскаем";
    private static final String CLICKING_VALUE_EN = "Clicking";

    @DataProvider
    private Object[] keysWithParams() {
        return new String[]{
                "loc.el.getattr",
                "loc.el.attr.value",
                "loc.text.value",
                "loc.text.sending.keys",
                "loc.no.elements.found.in.state",
                "loc.no.elements.found.by.locator",
                "loc.elements.were.found.but.not.in.state",
                "loc.elements.found.but.should.not",
                "loc.search.of.elements.failed",
                "loc.wait.for.state",
                "loc.wait.for.state.failed"};
    }

    @DataProvider
    private Object[] keysWithoutParams() {
        return new String[]{
                CLICKING_MESSAGE_KEY,
                "loc.get.text",
                "loc.el.state.displayed",
                "loc.el.state.not.displayed",
                "loc.el.state.exist",
                "loc.el.state.not.exist",
                "loc.el.state.enabled",
                "loc.el.state.not.enabled",
                "loc.el.state.clickable"};
    }


    private LocalizationManager getLocalizationManager() {
        return new LocalizationManager(
                AqualityServices.getServiceProvider().getInstance(ILoggerConfiguration.class),
                Logger.getInstance());
    }

    private LocalizationManager getLocalizationManager(String language) {
        ILoggerConfiguration configuration = new ILoggerConfiguration() {
            @Override
            public String getLanguage() {
                return language;
            }

            @Override
            public boolean logPageSource() {
                return true;
            }
        };
        return new LocalizationManager(configuration, Logger.getInstance());
    }

    @Test
    public void testShouldReturnUnknownKey() {
        String unknownKey = "loc.unknown.fake.key";
        Assert.assertEquals(unknownKey, getLocalizationManager().getLocalizedMessage(unknownKey));
    }

    @Test
    public void testShouldBeRegisteredAsSingleton() {
        assertEquals(CustomAqualityServices.getServiceProvider().getInstance(ILocalizationManager.class),
                CustomAqualityServices.getServiceProvider().getInstance(ILocalizationManager.class));
    }

    @Test
    public void testShouldBePossibleToUseForClicking() {
        assertEquals(getLocalizationManager().getLocalizedMessage(CLICKING_MESSAGE_KEY), CLICKING_VALUE_EN,
                "Logger should be configured in English by default and return valid value");
    }

    @Test
    public void testShouldBePossibleToUseForClickingWithCustomLanguage() {
        assertEquals(getLocalizationManager("be").getLocalizedMessage(CLICKING_MESSAGE_KEY), CLICKING_VALUE_BE,
                "Logger should be configured in custom language when use custom profile, and return valid value");
    }

    @Test(dataProvider = "keysWithParams")
    public void testShouldThrowFormatExceptionWhenKeysRequireParams(String keyWithParams) {
        for (String language : SUPPORTED_LANGUAGES) {
            Assert.assertThrows(MissingFormatArgumentException.class,
                    () -> getLocalizationManager(language).getLocalizedMessage(keyWithParams));
        }
    }

    @Test(dataProvider = "keysWithoutParams")
    public void testShouldReturnNonKeyAndNonEmptyValuesForKeysWithoutParams(String keyWithoutParams) {
        for (String language : SUPPORTED_LANGUAGES) {
            String value = getLocalizationManager(language).getLocalizedMessage(keyWithoutParams);
            Assert.assertFalse(value.isEmpty(),
                    String.format("value of key %1$s in language %2$s should not be empty", keyWithoutParams, language));
            Assert.assertNotEquals(value, keyWithoutParams,
                    String.format("value of key %1$s in language %2$s should be defined", keyWithoutParams, language));
        }
    }

    @Test(dataProvider = "keysWithParams")
    public void testShouldReturnNonKeyAndNonEmptyValuesForKeysWithParams(String keyWithParams) {
        for (String language : SUPPORTED_LANGUAGES) {
            Object[] params = new String[]{"a", "b", "c"};
            String value = getLocalizationManager(language).getLocalizedMessage(keyWithParams, params);
            Assert.assertFalse(value.isEmpty(),
                    String.format("value of key %1$s in language %2$s should not be empty", keyWithParams, language));
            Assert.assertNotEquals(value, keyWithParams,
                    String.format("value of key %1$s in language %2$s should be defined", keyWithParams, language));
            Assert.assertTrue(value.contains(params[0].toString()),
                    String.format("value of key %1$s in language %2$s should contain at least first parameter",
                            keyWithParams, language));
        }
    }

    @Test
    public void testShouldReturnNonKeyValueForKeysPresentInCoreIfLanguageMissedInSiblingAssembly() {
        String localizedValue = getLocalizationManager("en").getLocalizedMessage(CLICKING_MESSAGE_KEY);

        Assert.assertEquals(localizedValue, CLICKING_VALUE_EN, "Value should match to expected");
    }

    @Test
    public void testShouldReturnNonKeyValueForKeysPresentInCoreIfKeyMissedInSiblingAssembly() {
        String localizedValue = getLocalizationManager("be").getLocalizedMessage(CLICKING_MESSAGE_KEY);

        Assert.assertEquals(localizedValue, CLICKING_VALUE_BE, "Value should match to expected");
    }


    @Test
    public void testShouldThrowWhenInvalidLanguageSupplied() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                getLocalizationManager("invalid").getLocalizedMessage(CLICKING_MESSAGE_KEY));
    }
}

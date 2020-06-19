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

    @DataProvider
    private Object[] keysWithParams() {
        return new String[]{
                "loc.el.getattr",
                "loc.text.sending.keys",
                "loc.no.elements.found.in.state",
                "loc.no.elements.found.by.locator",
                "loc.elements.were.found.but.not.in.state",
                "loc.elements.found.but.should.not",
                "loc.search.of.elements.failed",
                "loc.element.not.in.state"};
    }

    @DataProvider
    private Object[] keysWithoutParams() {
        return new String[]{
                CLICKING_MESSAGE_KEY,
                "loc.get.text"};
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
        assertEquals(getLocalizationManager().getLocalizedMessage(CLICKING_MESSAGE_KEY), "Clicking",
                "Logger should be configured in English by default and return valid value");
    }

    @Test
    public void testShouldBePossibleToUseForClickingWithCustomLanguage() {
        assertEquals(getLocalizationManager("be").getLocalizedMessage(CLICKING_MESSAGE_KEY), "Націскаем",
                "Logger should be configured in custom language when use custom profile, and return valid value");
    }

    @Test(dataProvider = "keysWithParams")
    public void testShouldThrowFormatExceptionWhenKeysRequireParams(String keyWithParams) {
        for (String language: SUPPORTED_LANGUAGES) {
            Assert.assertThrows(MissingFormatArgumentException.class,
                    () -> getLocalizationManager(language).getLocalizedMessage(keyWithParams));
        }
    }

    @Test(dataProvider = "keysWithoutParams")
    public void testShouldReturnNonKeyAndNonEmptyValuesForKeysWithoutParams(String keyWithoutParams) {
        for (String language: SUPPORTED_LANGUAGES) {
            String value = getLocalizationManager(language).getLocalizedMessage(keyWithoutParams);
            Assert.assertFalse(value.isEmpty(),
                    String.format("value of key %1$s in language %2$s should not be empty", keyWithoutParams, language));
            Assert.assertNotEquals(value, keyWithoutParams,
                    String.format("value of key %1$s in language %2$s should be defined", keyWithoutParams, language));
        }
    }

    @Test(dataProvider = "keysWithParams")
    public void testShouldReturnNonKeyAndNonEmptyValuesForKeysWithParams(String keyWithParams) {
        for (String language: SUPPORTED_LANGUAGES) {
            Object[] params = new String[] { "a", "b", "c" };
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
    public void testShouldThrowWhenInvalidLanguageSupplied() {
        Assert.assertThrows(IllegalArgumentException.class, () ->
                getLocalizationManager("invalid").getLocalizedMessage(CLICKING_MESSAGE_KEY));
    }
}

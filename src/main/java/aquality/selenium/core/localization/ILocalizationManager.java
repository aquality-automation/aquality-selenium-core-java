package aquality.selenium.core.localization;

/**
 * This interface is using for translation messages to different languages
 */
public interface ILocalizationManager {

    /**
     * Get localized message from resources by its key.
     *
     * @param messageKey Key in resource file.
     * @param args Arguments, which will be provided to template of localized message.
     * @return Localized message.
     */
    String getLocalizedMessage(String messageKey, Object... args);
}

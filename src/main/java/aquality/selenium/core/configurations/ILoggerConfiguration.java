package aquality.selenium.core.configurations;

import aquality.selenium.core.localization.SupportedLanguage;

/**
 * Describes logger configuration.
 */
public interface ILoggerConfiguration {

    /**
     * Gets language of framework.
     *
     * @return Supported language.
     */
    SupportedLanguage getLanguage();
}

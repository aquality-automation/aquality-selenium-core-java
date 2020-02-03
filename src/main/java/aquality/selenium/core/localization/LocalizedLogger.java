package aquality.selenium.core.localization;

import aquality.selenium.core.logging.Logger;
import com.google.inject.Inject;

public class LocalizedLogger implements ILocalizedLogger {

    private final ILocalizationManager localizationManager;
    private final Logger logger;

    @Inject
    public LocalizedLogger(ILocalizationManager localizationManager, Logger logger) {
        this.localizationManager = localizationManager;
        this.logger = logger;
    }

    private String localizeMessage(String messageKey, Object... args) {
        return localizationManager.getLocalizedMessage(messageKey, args);
    }

    @Override
    public void infoElementAction(String elementType, String elementName, String messageKey, Object... args) {
        String message = String.format("%1$s '%2$s' :: %3$s", elementType, elementName, localizeMessage(messageKey, args));
        logger.info(message);
    }

    @Override
    public void info(String messageKey, Object... args) {
        logger.info(localizeMessage(messageKey, args));
    }

    @Override
    public void debug(String messageKey, Object... args) {
        logger.debug(localizeMessage(messageKey, args));
    }

    @Override
    public void debug(String messageKey, Throwable throwable, Object... args) {
        logger.debug(localizeMessage(messageKey, args), throwable);
    }

    @Override
    public void warn(String messageKey, Object... args) {
        logger.warn(localizeMessage(messageKey, args));
    }

    @Override
    public void error(String messageKey, Object... args) {
        logger.error(localizeMessage(messageKey, args));
    }

    @Override
    public void fatal(String messageKey, Throwable throwable, Object... args) {
        logger.fatal(localizeMessage(messageKey, args), throwable);
    }
}

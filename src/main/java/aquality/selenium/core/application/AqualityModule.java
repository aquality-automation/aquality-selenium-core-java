package aquality.selenium.core.application;

import aquality.selenium.core.configurations.*;
import aquality.selenium.core.localization.*;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.IElementActionRetrier;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.IUtilitiesModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Describes all dependencies which is registered for the project.
 */
public class AqualityModule<T extends IApplication> extends AbstractModule
        implements ILocalizationModule, IUtilitiesModule {

    private final Provider<T> applicationProvider;

    public AqualityModule (Provider<T> applicationProvider) {
        this.applicationProvider = applicationProvider;
    }

    /**
     * Registers dependencies to a container.
     */
    @Override
    protected void configure() {
        bind(IApplication.class).toProvider(applicationProvider);
        bind(ISettingsFile.class).toInstance(getInstanceOfSettingsFile());
        bind(Logger.class).toInstance(Logger.getInstance());
        bind(ILoggerConfiguration.class).to(LoggerConfiguration.class).in(Singleton.class);
        bind(ITimeoutConfiguration.class).to(TimeoutConfiguration.class).in(Singleton.class);
        bind(IRetryConfiguration.class).to(RetryConfiguration.class).in(Singleton.class);
        bind(IElementCacheConfiguration.class).to(ElementCacheConfiguration.class).in(Singleton.class);
        bind(IElementActionRetrier.class).to(getElementActionRetrierImplementation()).in(Singleton.class);
        bind(ILocalizationManager.class).to(getLocalizationManagerImplementation()).in(Singleton.class);
        bind(ILocalizedLogger.class).to(getLocalizedLoggerImplementation()).in(Singleton.class);
    }
}

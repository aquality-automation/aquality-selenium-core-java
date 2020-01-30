package aquality.selenium.core.application;

import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.utilities.JsonSettingsFile;
import aquality.selenium.core.configurations.*;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Describes all dependencies which is registered for the project.
 */
public class AqualityModule<T extends IApplication> extends AbstractModule {

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
        bind(ISettingsFile.class).toInstance(getSettings());
        bind(Logger.class).toInstance(Logger.getInstance());
        bind(ILoggerConfiguration.class).to(LoggerConfiguration.class).in(Singleton.class);
        bind(ITimeoutConfiguration.class).to(TimeoutConfiguration.class).in(Singleton.class);
        bind(IRetryConfiguration.class).to(RetryConfiguration.class).in(Singleton.class);
        bind(IElementCacheConfiguration.class).to(ElementCacheConfiguration.class).in(Singleton.class);
    }

    /**
     * Provides default {@link ISettingsFile}. with settings.
     * Default value is settings.json.
     * You are able to override this path, by setting environment variable 'profile'.
     * In this case, settings file will be settings.{profile}.json.
     *
     * @return An instance of settings.
     */
    protected ISettingsFile getSettings() {
        String settingsProfile = System.getProperty("profile") == null ? "settings.json" : "settings." + System.getProperty("profile") + ".json";
        return new JsonSettingsFile(settingsProfile);
    }
}

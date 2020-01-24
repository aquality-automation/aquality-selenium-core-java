package aquality.selenium.core.application;

import aquality.selenium.core.logging.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;

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
        bind(Logger.class).toInstance(Logger.getInstance());
    }
}
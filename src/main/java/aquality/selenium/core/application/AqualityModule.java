package aquality.selenium.core.application;

import com.google.inject.AbstractModule;
import aquality.selenium.core.logging.Logger;

/**
 * Describes all dependencies which is registered for the project.
 */
public class AqualityModule extends AbstractModule {

    /**
     * Registers dependencies to a container.
     */
    @Override
    protected void configure() {
        bind(Logger.class).toInstance(Logger.getInstance());
    }
}
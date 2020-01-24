package aquality.selenium.core.application;

import com.google.inject.AbstractModule;
import aquality.selenium.core.logging.Logger;

public class AqualityModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Logger.class).toInstance(Logger.getInstance());
    }
}
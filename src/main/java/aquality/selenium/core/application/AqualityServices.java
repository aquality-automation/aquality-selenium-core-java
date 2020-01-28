package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Describes static methods which help work with Application and dependency injector.
 */
public abstract class AqualityServices <T extends IApplication> {

    private T app;
    private final Injector injector;

    protected <V extends AqualityModule<T>> AqualityServices(Provider<T> applicationProvider,
                                                             Supplier<V> servicesModuleSupplier) {
        AqualityModule<T> module = servicesModuleSupplier == null
                ? new AqualityModule<>(applicationProvider)
                : servicesModuleSupplier.get();
        injector = Guice.createInjector(module);
    }

    /**
     * @return true if the application is already started, false otherwise.
     */
    protected boolean isAppStarted() {
        return app != null && app.isStarted();
    }

    /**
     * Sets the application instance, saving it to DI container.
     * @param application instance to set into container.
     */
    protected void setApp(T application) {
        this.app = application;
    }

    /**
     * Returns an existing application or initializes a new one based on passed parameter.
     * @param startApplicationFunction function to start the application, where the injector could be used.
     * @return started application.
     */
    protected T getApp(Function<Injector, T> startApplicationFunction) {
        if (!isAppStarted()) {
            setApp(startApplicationFunction.apply(injector));
        }

        return app;
    }

    /**
     * Gets existing injector based on {@link AqualityModule} supplier passed to constructor.
     *
     * @return existing injector.
     */
    protected Injector getInjector() {
        return injector;
    }
}
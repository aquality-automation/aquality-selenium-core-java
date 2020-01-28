package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provider;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Describes static methods which help work with Application and dependency injector.
 * Injector is thread safe.
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

    protected boolean isAppStarted() {
        return app != null && app.isStarted();
    }

    protected void setApp(T application) {
        this.app = application;
    }

    protected T getApp(Function<Injector, T> startApplicationFunction) {
        if (!isAppStarted()) {
            setApp(startApplicationFunction.apply(injector));
        }

        return app;
    }

    protected Injector getInjector() {
        return injector;
    }
}
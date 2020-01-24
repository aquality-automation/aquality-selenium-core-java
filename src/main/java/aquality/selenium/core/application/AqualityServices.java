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
public class AqualityServices {

    private static final ThreadLocal<IApplication> appContainer = new ThreadLocal<>();
    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    protected AqualityServices() {
    }

    protected static boolean isApplicationStarted() {
        return appContainer.get() != null && appContainer.get().isStarted();
    }

    protected static <T extends IApplication> void setApplication(T application) {
        if (appContainer.get() != null){
            appContainer.remove();
        }
        appContainer.set(application);
    }

    protected static <T extends IApplication, V extends AqualityModule> T getApplication (
            Function<Injector, T> startApplicationFunction, Supplier<V> servicesModuleSupplier){
        if (!isApplicationStarted()) {
            setApplication(startApplicationFunction.apply(
                    getInjector(() -> getApplication(startApplicationFunction, servicesModuleSupplier),
                            servicesModuleSupplier)));
        }

        return (T) appContainer.get();
    }

    /**
     * Gets existing injector or initializes new with based on {@link AqualityModule}.
     *
     * @return existing or new injector.
     */
    protected static <T extends IApplication, V extends AqualityModule> Injector getInjector(Provider<T> applicationProvider,
                                                                     Supplier<V> servicesModuleSupplier) {
        if (injectorContainer.get() == null) {
            if (servicesModuleSupplier == null) {
                initInjector(new AqualityModule<>(applicationProvider));
            }
            else {
                initInjector(servicesModuleSupplier.get());
            }
        }

        return injectorContainer.get();
    }

    /**
     * Initializes custom injector.
     *
     * @param module - custom module with dependencies.
     * @param <T> is type of custom module. Custom module should be inherited from {@link AqualityModule}.
     */
    protected static <T extends AqualityModule> void initInjector(T module) {
        injectorContainer.remove();
        injectorContainer.set(Guice.createInjector(module));
    }
}
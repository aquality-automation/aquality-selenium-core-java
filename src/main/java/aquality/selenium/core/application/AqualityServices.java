package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Describes static methods which help work with Application and dependency injector.
 * Injector is thread safe.
 */
public class AqualityServices {

    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    protected AqualityServices() {
    }

    /**
     * Gets existing injector or initializes new with based on {@link AqualityModule}.
     *
     * @return existing or new injector.
     */
    protected static Injector getInjector() {
        if (injectorContainer.get() == null) {
            initInjector();
        }

        return injectorContainer.get();
    }

    private static void initInjector() {
        initInjector(new AqualityModule());
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

package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Describes static methods which help work with Application and dependency injector.
 * Injector is thread safe.
 */
public abstract class AqualityServices {

    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    protected AqualityServices() {
    }


    protected static Injector getInjector() {
        if (injectorContainer.get() == null) {
            injectorContainer.remove();
            initInjector();
        }

        return injectorContainer.get();
    }

    /**
     * Sets default injector with {@link AqualityModule}.
     */
    private static void initInjector() {
        initInjector(new AqualityModule());
    }

    /**
     * Sets custom injector.
     *
     * @param module - custom module with dependencies.
     * @param <T>    is type of custom module.Custom module should be inherited from {@link AqualityModule}.
     */
    protected static <T extends AqualityModule> void initInjector(T module) {
        injectorContainer.remove();
        injectorContainer.set(Guice.createInjector(module));
    }

    public void unload() {
        injectorContainer.remove();
    }
}
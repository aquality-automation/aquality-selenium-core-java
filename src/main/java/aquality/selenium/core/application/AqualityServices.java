package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Describes static methods which help work with Application and dependency injector.
 * Injector is thread safe.
 */
public abstract class AqualityServices implements AutoCloseable {

    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    protected AqualityServices() {
    }

    /**
     * Gets default injector with {@link AqualityModule}.
     *
     * @return injector.
     */
    protected static Injector getInjector() {
        return getInjector(new AqualityModule());
    }

    /**
     * Gets custom injector.
     *
     * @param module is custom module with dependencies.
     * @param <T>    type of custom module. Should be inherited from {@link AqualityModule}.
     * @return custom injector.
     */
    protected static <T extends AqualityModule> Injector getInjector(T module) {
        if (injectorContainer.get() == null) {
            setInjector(module);
        }

        return injectorContainer.get();
    }

    /**
     * Sets default injector with {@link AqualityModule}.
     */
    protected static void setInjector() {
        setInjector(new AqualityModule());
    }

    /**
     * Sets custom injector.
     *
     * @param module - custom module with dependencies.
     * @param <T>    is type of custom module.Custom module should be inherited from {@link AqualityModule}.
     */
    protected static <T extends AqualityModule> void setInjector(T module) {
        injectorContainer.remove();
        injectorContainer.set(Guice.createInjector(module));
    }

    /**
     * Closes thread safe container.
     */
    @Override
    public void close() {
        injectorContainer.remove();
    }
}
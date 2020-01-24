package aquality.selenium.core.application;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AqualityServices {

    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    protected AqualityServices() {
    }

    protected static Injector getInjector() {
        return getInjector(new AqualityModule());
    }

    protected static <TModule extends AbstractModule> Injector getInjector(TModule module) {
        if (injectorContainer.get() == null) {
            setInjector(module);
        }

        return injectorContainer.get();
    }

    protected static void setInjector() {
        setInjector(new AqualityModule());
    }

    protected static <TModule extends AbstractModule> void setInjector(TModule module) {
        remove(injectorContainer);
        injectorContainer.set(Guice.createInjector(module));
    }

    private static void remove(ThreadLocal<?> container) {
        if (container.get() != null) {
            container.remove();
        }
    }

    private void unload() {
        injectorContainer.remove();
    }
}
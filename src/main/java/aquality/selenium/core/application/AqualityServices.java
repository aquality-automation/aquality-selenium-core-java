package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AqualityServices {

    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    protected AqualityServices() {
    }

    protected static Injector getInjector() {
        return getInjector(new AqualityModule());
    }

    protected static <T extends AqualityModule> Injector getInjector(T module) {
        if (injectorContainer.get() == null) {
            setInjector(module);
        }

        return injectorContainer.get();
    }

    protected static void setInjector() {
        setInjector(new AqualityModule());
    }

    protected static <T extends AqualityModule> void setInjector(T module) {
        remove(injectorContainer);
        injectorContainer.set(Guice.createInjector(module));
    }

    private static void remove(ThreadLocal<?> container) {
        if (container.get() != null) {
            container.remove();
        }
    }

    public void unload() {
        injectorContainer.remove();
    }
}
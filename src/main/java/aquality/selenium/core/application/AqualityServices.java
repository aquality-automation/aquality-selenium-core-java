package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

public abstract class AqualityServices{

    private static final ThreadLocal<Injector> injectorContainer = new ThreadLocal<>();

    private AqualityServices(){
    }

    public static Injector getInjector(){
        if(injectorContainer.get() == null){
            setDefaultInjector();
        }

        return injectorContainer.get();
    }

    public static void setDefaultInjector(){
        remove(injectorContainer);
        injectorContainer.set(Guice.createInjector(new AqualityModule()));
    }

    private static void remove(ThreadLocal<?> container){
        if(container.get() != null){
            container.remove();
        }
    }
}

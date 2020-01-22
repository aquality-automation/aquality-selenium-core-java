package aquality.selenium.core.application;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AqualityServices {

    private static Injector injector;

    public static Injector getInjector(){
        if(injector == null){
            injector = Guice.createInjector(new AqualityModule());
        }

        return injector;
    }
}

package tests.application;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.application.AqualityServices;
import com.google.inject.Injector;

public abstract class CustomAqualityServices extends AqualityServices {

    private CustomAqualityServices() {
    }

    public static Injector getInjector() {
        return AqualityServices.getInjector();
    }

    public static <T extends AqualityModule> Injector getInjector(T module) {
        return AqualityServices.getInjector(module);
    }

    public static void setInjector() {
        AqualityServices.setInjector();
    }

    public static <T extends AqualityModule> void setInjector(T module) {
        AqualityServices.setInjector(module);
    }
}
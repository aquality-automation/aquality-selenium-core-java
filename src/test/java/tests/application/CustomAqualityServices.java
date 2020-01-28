package tests.application;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.application.AqualityServices;
import com.google.inject.Injector;
import tests.application.browser.ChromeApplication;

public abstract class CustomAqualityServices extends AqualityServices {

    private CustomAqualityServices() {
    }

    public static ChromeApplication getApplication() {
        return getApplication(injector -> tests.application.browser.AqualityServices.getApplication(), null);
    }

    public static Injector getInjector() {
        return getInjector(CustomAqualityServices::getApplication, null);
    }

    public static <T extends AqualityModule> void initInjector(T module) {
        AqualityServices.initInjector(module);
    }
}
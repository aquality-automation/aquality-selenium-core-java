package tests.application;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.application.AqualityServices;
import com.google.inject.Injector;
import io.github.bonigarcia.wdm.WebDriverManager;
import tests.application.browser.ChromeApplication;

public abstract class CustomAqualityServices extends AqualityServices {

    private CustomAqualityServices() {
    }

    public static boolean isBrowserStarted() {
        return isApplicationStarted();
    }

    public static ChromeApplication getApplication() {
        return getApplication(CustomAqualityServices::startChrome, null);
    }

    private static ChromeApplication startChrome(Injector injector) {
        WebDriverManager.chromedriver().version("Latest").setup();
        if (injector == null) {
            throw new RuntimeException("cannot start Chrome without DI injector");
        }
        long implicitWaitSeconds = 0; // todo: get from TimeoutConfiguration from injector
        return new ChromeApplication(implicitWaitSeconds);
    }

    public static Injector getInjector() {
        return getInjector(CustomAqualityServices::getApplication, null);
    }

    public static <T extends AqualityModule> void initInjector(T module) {
        AqualityServices.initInjector(module);
    }
}
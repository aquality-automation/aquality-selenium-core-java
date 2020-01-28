package tests.application.browser;

import com.google.inject.Injector;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AqualityServices  extends aquality.selenium.core.application.AqualityServices {

    private AqualityServices() {
    }

    public static boolean isApplicationStarted() {
        return aquality.selenium.core.application.AqualityServices.isApplicationStarted();
    }

    public static ChromeApplication getApplication() {
        return getApplication(AqualityServices::startChrome, null);
    }

    private static ChromeApplication startChrome(Injector injector) {
        WebDriverManager.chromedriver().setup();
        if (injector == null) {
            throw new RuntimeException("cannot start Chrome without DI injector");
        }
        long implicitWaitSeconds = 0;
        return new ChromeApplication(implicitWaitSeconds);
    }

    public static Injector getInjector() {
        return getInjector(AqualityServices::getApplication, null);
    }
}

package tests.application.browser;

import com.google.inject.Injector;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AqualityServices  extends aquality.selenium.core.application.AqualityServices<ChromeApplication> {
    private static final ThreadLocal<AqualityServices> INSTANCE_CONTAINER = ThreadLocal.withInitial(AqualityServices::new);

    private AqualityServices() {
        super(AqualityServices::getApplication, null);
    }

    private static AqualityServices getInstance() {
        return INSTANCE_CONTAINER.get();
    }

    public static boolean isApplicationStarted() {
        return getInstance().isAppStarted();
    }

    public static ChromeApplication getApplication() {
        return getInstance().getApp(AqualityServices::startChrome);
    }

    private static ChromeApplication startChrome(Injector injector) {
        WebDriverManager.chromedriver().setup();
        if (injector == null) {
            throw new RuntimeException("cannot start Chrome without DI injector");
        }
        long implicitWaitSeconds = 0;
        return new ChromeApplication(implicitWaitSeconds);
    }

    public static Injector getServiceProvider() {
        return getInstance().getInjector();
    }
}

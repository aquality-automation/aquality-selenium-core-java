package tests.applications.browser;

import aquality.selenium.core.applications.AqualityModule;
import com.google.inject.Injector;
import io.github.bonigarcia.wdm.WebDriverManager;

public class AqualityServices  extends aquality.selenium.core.applications.AqualityServices<ChromeApplication> {
    private static final ThreadLocal<AqualityServices> INSTANCE_CONTAINER = ThreadLocal.withInitial(AqualityServices::new);

    private AqualityServices() {
        super(AqualityServices::getApplication, null);
    }

    private <T extends AqualityModule<ChromeApplication>>  AqualityServices(T module) {
        super(AqualityServices::getApplication, () -> module);
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

    public static <T extends AqualityModule<ChromeApplication>> void initInjector(T module) {
        if (INSTANCE_CONTAINER.get() != null){
            INSTANCE_CONTAINER.remove();
        }
        INSTANCE_CONTAINER.set(new AqualityServices(module));
    }

    public static <T> T get(Class<T> type) {
        return getServiceProvider().getInstance(type);
    }
}

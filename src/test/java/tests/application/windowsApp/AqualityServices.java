package tests.application.windowsApp;

import aquality.selenium.core.logging.Logger;
import com.google.inject.Injector;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class AqualityServices extends aquality.selenium.core.application.AqualityServices<WindowsApplication> {
    private static final ThreadLocal<AqualityServices> INSTANCE_CONTAINER = ThreadLocal.withInitial(AqualityServices::new);
    private static final String APP_PATH = "./src/test/resources/apps/Day Maxi Calc.exe";
    private static final String DEFAULT_SERVICE_URL =  "http://127.0.0.1:4723/";

    private AqualityServices() {
        super(AqualityServices::getApplication, null);
    }

    private static AqualityServices getInstance() {
        return INSTANCE_CONTAINER.get();
    }

    public static boolean isApplicationStarted() {
        return getInstance().isAppStarted();
    }

    public static WindowsApplication getApplication() {
        return getInstance().getApp(AqualityServices::startWindowsApplication);
    }

    private static WindowsApplication startWindowsApplication(Injector injector) {
        if (injector == null) {
            throw new RuntimeException("cannot start WindowsApplication without DI injector");
        }
        long implicitWaitSeconds = 0;
        try {
            return new WindowsApplication(implicitWaitSeconds,
                    new File(APP_PATH).getCanonicalPath(),
                    new URL(DEFAULT_SERVICE_URL));
        } catch (IOException e) {
            Logger.getInstance().fatal(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public static <T> T get(Class<T> type) {
        return getServiceProvider().getInstance(type);
    }

    public static void setApplication(WindowsApplication application) {
        getInstance().setApp(application);
    }

    public static Injector getServiceProvider() {
        return getInstance().getInjector();
    }
}

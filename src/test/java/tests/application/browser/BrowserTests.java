package tests.application.browser;

import aquality.selenium.core.applications.IApplication;
import com.google.inject.Injector;
import tests.application.IApplicationTests;

public class BrowserTests implements IApplicationTests {
    @Override
    public IApplication getApplication() {
        return AqualityServices.getApplication();
    }

    @Override
    public Injector getServiceProvider() {
        return AqualityServices.getServiceProvider();
    }

    @Override
    public boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }
}

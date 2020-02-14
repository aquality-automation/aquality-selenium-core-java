package tests.applications.windowsApp;

import aquality.selenium.core.applications.IApplication;
import com.google.inject.Injector;
import tests.applications.IApplicationTests;

public class ApplicationTests implements IApplicationTests {

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

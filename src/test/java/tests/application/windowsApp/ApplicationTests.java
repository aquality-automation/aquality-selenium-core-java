package tests.application.windowsApp;

import aquality.selenium.core.application.IApplication;
import com.google.inject.Injector;
import tests.application.IApplicationTests;

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

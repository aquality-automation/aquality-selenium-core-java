package tests.application;

import aquality.selenium.core.application.AqualityServices;
import com.google.inject.Injector;

public abstract class TestAqualityServices extends AqualityServices {

    private TestAqualityServices(){
    }

    public static Injector getInjector() {
        return AqualityServices.getInjector();
    }
}

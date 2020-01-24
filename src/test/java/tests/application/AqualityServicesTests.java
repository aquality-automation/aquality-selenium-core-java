package tests.application;

import aquality.selenium.core.logging.Logger;
import com.google.inject.ConfigurationException;
import com.google.inject.Injector;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class AqualityServicesTests{

    @Test
    public void testShouldBePossibleToGetDefaultInjector() {
        Injector injector = CustomAqualityServices.getInjector();
        assertNotNull(injector, "Default injector should not be null");

        Logger logger = injector.getInstance(Logger.class);
        assertNotNull(logger, "Logger should not be null");
        assertThrows(ConfigurationException.class, () -> injector.getInstance(ICustomDependency.class));

        Injector newInjector = CustomAqualityServices.getInjector();
        assertEquals(injector, newInjector, "AqualityServices should return the same instance of injector");
    }

    @Test
    public void testShouldBePossibleToGetCustomModule() {
        CustomAqualityServices.initInjector(new TestModule(() -> CustomAqualityServices.getApplication()));
        Injector injector = CustomAqualityServices.getInjector();
        assertNotNull(injector, "Custom injector should not be null");

        ICustomDependency customDependency = injector.getInstance(ICustomDependency.class);
        assertNotNull(customDependency, "ICustomDependency should be injected in custom module");
        assertEquals(customDependency.getClass(), CustomDependency.class, "ICustomDependency should be injected as CustomDependency");
        assertNotNull(injector.getInstance(Logger.class), "Logger should be null");

        Injector newInjector = CustomAqualityServices.getInjector();
        assertEquals(injector, newInjector, "AqualityServices should return the same instance of injector");
    }

    @Test
    public void testShouldBePossibleToSetDefaultInjector() {
        Logger logger = CustomAqualityServices.getInjector().getInstance(Logger.class);
        assertNotNull(logger, "Logger should be injected in default module");
    }

    @Test
    public void testShouldBePossibleToSetCustomInjector() {
        CustomAqualityServices.initInjector(new TestModule(() -> CustomAqualityServices.getApplication()));
        ICustomDependency customDependency = CustomAqualityServices.getInjector().getInstance(ICustomDependency.class);
        assertNotNull(customDependency, "ICustomDependency should be injected in custom module");
    }
}
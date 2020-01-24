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
        assertEquals(injector, newInjector, "AqualityServices should return instance of injector");
    }

    @Test
    public void testShouldBePossibleToGetCustomModule() {
        Injector injector = CustomAqualityServices.getInjector(new TestModule());
        assertNotNull(injector, "Custom injector should not be null");

        ICustomDependency customDependency = injector.getInstance(ICustomDependency.class);
        assertNotNull(customDependency, "ICustomDependency should not be injected in custom module");
        assertEquals(customDependency.getClass(), CustomDependency.class, "ICustomDependency should not be injected as CustomDependency");
        assertNotNull(injector.getInstance(Logger.class), "Logger should not be null");

        Injector newInjector = CustomAqualityServices.getInjector();
        assertEquals(injector, newInjector, "AqualityServices should return instance of injector");
    }

    @Test
    public void testShouldBePossibleToSetDefaultInjector() {
        CustomAqualityServices.setInjector();
        Logger logger = CustomAqualityServices.getInjector().getInstance(Logger.class);
        assertNotNull(logger, "Logger should not be injected in default module");
    }

    @Test
    public void testShouldBePossibleToSetCustomInjector() {
        CustomAqualityServices.setInjector(new TestModule());
        ICustomDependency customDependency = CustomAqualityServices.getInjector().getInstance(ICustomDependency.class);
        assertNotNull(customDependency, "ICustomDependency should not be injected in custom module");
    }
}
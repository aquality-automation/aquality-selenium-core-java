package tests.configurations;

import aquality.selenium.core.configurations.IElementCacheConfiguration;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.assertFalse;

public class ElementCacheConfigurationTests {

    @Test
    public void testShouldBePossibleCheckIsEnableElementCache() {
        boolean isEnable = CustomAqualityServices.getServiceProvider().getInstance(IElementCacheConfiguration.class).isEnabled();
        assertFalse(isEnable, "Element cache is disabled by default");
    }
}

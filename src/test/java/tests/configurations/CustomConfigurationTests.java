package tests.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.applications.CustomAqualityServices;
import tests.applications.CustomConfiguration;
import tests.applications.TestModule;

import static org.testng.Assert.assertEquals;

public class CustomConfigurationTests extends BaseProfileTest {

    private static final String PROFILE = "customconfiguration";
    private static final String DOUBLE_VALUE_KEY = "custom.doubleValue";

    @BeforeMethod
    public void before() {
        System.setProperty(PROFILE_KEY, PROFILE);
        CustomAqualityServices.initInjector(new TestModule(CustomAqualityServices::getApplication));
    }

    @Test
    public void testShouldBePossibleToGetDoubleValueFromCustomConfiguration() {
        checkDoubleValue(0.2);
    }

    @Test
    public void testShouldBePossibleToGetDoubleValueFromEnvironment() {
        System.setProperty(DOUBLE_VALUE_KEY, "0.3");
        checkDoubleValue(0.3);
    }

    @Test
    public void testShouldBePossibleToGetValueFromCustomConfiguration() {
        String value = CustomAqualityServices.getServiceProvider().getInstance(CustomConfiguration.class).getStringValue();
        assertEquals(value, "customString", "String value should be got from custom configuration");
    }

    @AfterMethod
    public void after() {
        System.clearProperty(DOUBLE_VALUE_KEY);
    }

    private void checkDoubleValue(double expectedValue){
        double value = new CustomConfiguration(CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class)).getDoubleValue();
        assertEquals(value, expectedValue, "Double value should be got correctly");
    }
}

package tests.application.windowsApp;

import aquality.selenium.core.application.IApplication;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class ApplicationTests {

    @BeforeMethod
    public void setUp() {
        AqualityServices.setApplication(null);
    }

    @AfterMethod
    public void cleanUp() {
        if (AqualityServices.isApplicationStarted()) {
            AqualityServices.getApplication().getDriver().quit();
        }
    }

    @Test
    public void testShouldBePossibleToGetApplication() {
        IApplication application = AqualityServices.get(IApplication.class);
        Assert.assertNotNull(application, "should be possible to resolve app instance from aquality services");
        Assert.assertEquals(AqualityServices.getApplication(), application,
                "should return the same instance of app");
    }

    @Test
    public void testShouldBePossibleToGetDriver() {
        Assert.assertNotNull(AqualityServices.getApplication().getDriver(),
                "should be possible get driver from the application");
    }

    @Test
    public void testShouldBePossibleToSetImplicitWait() {
        AqualityServices.getApplication().setImplicitWaitTimeout(1, TimeUnit.SECONDS);
        AqualityServices.getApplication().setImplicitWaitTimeout(0, TimeUnit.SECONDS);
    }

    @Test
    public void testShouldBePossibleToDefineIsStarted() {
        WindowsApplication application = AqualityServices.getApplication();
        Assert.assertTrue(application.isStarted(),
                "application should be started when got it from the aquality services");
        Assert.assertTrue(AqualityServices.isApplicationStarted(),
                "application should be started when check it's state from the aquality services");
        application.getDriver().quit();
        Assert.assertFalse(application.isStarted(),
                "application should not be started after quit");
        Assert.assertFalse(AqualityServices.isApplicationStarted(),
                "application should not be started when check it's state from the aquality services after quit");
    }
}

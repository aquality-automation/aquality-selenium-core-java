package tests.applications;

import aquality.selenium.core.applications.IApplication;
import com.google.inject.Injector;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.ITestWithApplication;

import java.util.concurrent.TimeUnit;

public interface IApplicationTests extends ITestWithApplication {
    Injector getServiceProvider();

    @Test
    default void testShouldBePossibleToGetApplication() {
        IApplication application = getServiceProvider().getInstance(IApplication.class);
        Assert.assertNotNull(application, "should be possible to resolve app instance from aquality services");
        Assert.assertEquals(getApplication(), application,
                "should return the same instance of app");
    }

    @Test
    default void testShouldBePossibleToGetDriver() {
        Assert.assertNotNull(getApplication().getDriver(), "should be possible get driver from the applications");
    }

    @Test
    default void testShouldBePossibleToSetImplicitWait() {
        checkImplicitWaitSetting(1);
        checkImplicitWaitSetting(0);
    }

    default void checkImplicitWaitSetting(int valueInSeconds) {
        try {
            getApplication().setImplicitWaitTimeout(1, TimeUnit.SECONDS);
        } catch (Throwable e){
            Assert.fail("An error occured when tried to set implicit wait", e);
        }
    }

    @Test
    default void testShouldBePossibleToDefineIsStarted() {
        Assert.assertFalse(isApplicationStarted(),
                "applications should not be started before getting");
        IApplication application = getApplication();
        Assert.assertTrue(application.isStarted(),
                "applications should be started when got it from the aquality services");
        Assert.assertTrue(isApplicationStarted(),
                "applications should be started when check it's state from the aquality services");
        application.getDriver().quit();
        Assert.assertFalse(application.isStarted(),
                "applications should not be started after quit");
        Assert.assertFalse(isApplicationStarted(),
                "applications should not be started when check it's state from the aquality services after quit");
    }
}

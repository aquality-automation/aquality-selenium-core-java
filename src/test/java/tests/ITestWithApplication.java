package tests;

import aquality.selenium.core.applications.IApplication;
import org.testng.annotations.AfterMethod;

public interface ITestWithApplication {
    IApplication getApplication();
    boolean isApplicationStarted();

    @AfterMethod
    default void cleanUp () {
        if (isApplicationStarted()) {
            getApplication().getDriver().quit();
        }
    }
}

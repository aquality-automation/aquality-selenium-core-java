package tests.applications.browser;

import aquality.selenium.core.applications.IApplication;
import org.openqa.selenium.Dimension;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import theinternet.TheInternetPage;

public interface ITheInternetPageTest {
    Dimension DEFAULT_SIZE = new Dimension(1024, 768);

    default IApplication getBrowser() {
        return AqualityServices.getApplication();
    }

    default boolean isApplicationStarted() {
        return AqualityServices.isApplicationStarted();
    }

    default void navigate(TheInternetPage page) {
        getBrowser().getDriver().navigate().to(page.getAddress());
    }

    @BeforeMethod
    default void beforeMethod() {
        navigate(TheInternetPage.DYNAMIC_CONTROLS);
        getBrowser().getDriver().manage().window().setSize(DEFAULT_SIZE);
    }

    @AfterMethod
    default void cleanUp () {
        if (isApplicationStarted()) {
            getBrowser().getDriver().quit();
        }
    }
}

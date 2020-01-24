package tests.application;

import org.testng.Assert;
import org.testng.annotations.Test;
import tests.application.browser.ChromeApplication;

public class BrowserTests {
    @Test
    public void testShouldBePossibleToGetBrowser() {
        ChromeApplication browser = CustomAqualityServices.getApplication();
        Assert.assertTrue(browser.isStarted());
        browser.getDriver().quit();
    }
}

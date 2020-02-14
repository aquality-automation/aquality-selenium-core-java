package tests.applications.browser;

import aquality.selenium.core.applications.IApplication;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

public class ChromeApplication implements IApplication {
    private long implicitWaitSeconds;
    private final RemoteWebDriver driver;

    public ChromeApplication(long implicitWaitSeconds) {
        driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
        setImplicitWaitTimeout(implicitWaitSeconds, TimeUnit.SECONDS);
    }

    @Override
    public RemoteWebDriver getDriver() {
        return driver;
    }

    @Override
    public boolean isStarted() {
        return driver.getSessionId() != null;
    }

    @Override
    public void setImplicitWaitTimeout(long value, TimeUnit timeUnit) {
        long valueInSeconds = TimeUnit.SECONDS.convert(value, timeUnit);
        if (implicitWaitSeconds != valueInSeconds){
            driver.manage().timeouts().implicitlyWait(value, timeUnit);
            implicitWaitSeconds = valueInSeconds;
        }
    }
}

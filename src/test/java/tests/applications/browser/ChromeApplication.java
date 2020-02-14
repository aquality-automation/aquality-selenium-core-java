package tests.applications.browser;

import aquality.selenium.core.applications.IApplication;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ChromeApplication implements IApplication {
    private Duration implicitWait;
    private final RemoteWebDriver driver;

    ChromeApplication(long implicitWaitSeconds) {
        driver = new ChromeDriver(new ChromeOptions().setHeadless(true));
        setImplicitWaitTimeout(Duration.ofSeconds(implicitWaitSeconds));
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
    public void setImplicitWaitTimeout(Duration value) {
        if (implicitWait != value){
            driver.manage().timeouts().implicitlyWait(value.getSeconds(), TimeUnit.SECONDS);
            implicitWait = value;
        }
    }
}

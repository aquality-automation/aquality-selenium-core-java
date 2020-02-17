package tests.applications.windowsApp;

import aquality.selenium.core.applications.IApplication;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WindowsApplication implements IApplication {
    private long implicitWaitSeconds;
    private final RemoteWebDriver driver;

    WindowsApplication(long implicitWaitSeconds, String appPath, URL serviceUrl) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", appPath);
        driver = new WindowsDriver<WindowsElement>(serviceUrl, capabilities);
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
        long valueInSeconds = value.getSeconds();
        if (implicitWaitSeconds != valueInSeconds){
            driver.manage().timeouts().implicitlyWait(value.toMillis(), TimeUnit.MILLISECONDS);
            implicitWaitSeconds = valueInSeconds;
        }
    }
}

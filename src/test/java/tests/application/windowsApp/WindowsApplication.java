package tests.application.windowsApp;

import aquality.selenium.core.application.IApplication;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.WindowsElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

public class WindowsApplication implements IApplication {
    private long implicitWaitSeconds;
    private final RemoteWebDriver driver;

    public WindowsApplication(long implicitWaitSeconds, String appPath, URL serviceUrl) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", appPath);
        driver = new WindowsDriver<WindowsElement>(serviceUrl, capabilities);
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

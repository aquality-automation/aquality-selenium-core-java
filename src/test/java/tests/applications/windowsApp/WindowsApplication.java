package tests.applications.windowsApp;

import aquality.selenium.core.applications.IApplication;
import io.appium.java_client.windows.WindowsDriver;
import io.appium.java_client.windows.options.WindowsOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WindowsApplication implements IApplication {
    private long implicitWaitSeconds;
    private final WindowsDriver driver;

    WindowsApplication(long implicitWaitSeconds, String appPath, URL serviceUrl) {
        WindowsOptions options = new WindowsOptions();
        options.setApp(appPath);
        driver = new WindowsDriver(serviceUrl, options);
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

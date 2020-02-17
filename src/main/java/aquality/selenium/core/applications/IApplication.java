package aquality.selenium.core.applications;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.time.Duration;

/**
 * Interface of any application controlled by Selenium WebDriver API
 */
public interface IApplication {

    /**
     * @return Current instance of driver.
     */
    RemoteWebDriver getDriver();

    /**
     * @return Is the application already running or not.
     */
    boolean isStarted();

    /**
     * Sets implicit wait timeout to Selenium WebDriver.
     * @param value timeout value to set.
     */
    void setImplicitWaitTimeout(Duration value);
}

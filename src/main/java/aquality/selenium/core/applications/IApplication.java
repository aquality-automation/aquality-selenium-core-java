package aquality.selenium.core.applications;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.concurrent.TimeUnit;

/**
 * Interface of any applications controlled by Selenium WebDriver API
 */
public interface IApplication {

    /**
     * @return Current instance of driver.
     */
    RemoteWebDriver getDriver();

    /**
     * @return Is the applications already running or not.
     */
    boolean isStarted();

    /**
     * Sets implicit wait timeout to Selenium WebDriver.
     * @param value timeout value to set.
     * @param timeUnit timeUnit of timeout value.
     */
    void setImplicitWaitTimeout(long value, TimeUnit timeUnit);
}

package tests.waitings;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.localization.ILocalizationManager;
import aquality.selenium.core.waitings.ConditionalWait;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.openqa.selenium.StaleElementReferenceException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import utils.Timer;

import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

class BaseConditionalWaitTest {

    static final long waitForTimeoutCondition = 10;
    static final long waitForTimeoutPolling = 150;
    static final long ACCURANCY = 1;
    ThreadLocal<Timer> timer = ThreadLocal.withInitial(Timer::new);
    ITimeoutConfiguration timeoutConfiguration;
    ConditionalWait conditionalWait;
    private Provider<IApplication> application;

    @BeforeMethod
    public void init() {
        Injector serviceProvider = CustomAqualityServices.getServiceProvider();
        ILocalizationManager localizationManager = serviceProvider.getInstance(ILocalizationManager.class);
        application = serviceProvider.getProvider(IApplication.class);
        timeoutConfiguration = serviceProvider.getInstance(ITimeoutConfiguration.class);
        conditionalWait = new ConditionalWait(application, timeoutConfiguration, localizationManager);
    }

    @AfterMethod
    public void quitApplication() {
        application.get().getDriver().quit();
    }
}

package aquality.selenium.core.waitings;

import aquality.selenium.core.application.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.localization.ILocalizationManager;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

public class ConditionalWait implements IConditionalWait {

    private IApplication application;
    private ITimeoutConfiguration timeoutConfiguration;
    private ILocalizationManager localizationManager;

    @Inject
    private ConditionalWait(Provider<IApplication> applicationProvider, ITimeoutConfiguration timeoutConfiguration, ILocalizationManager localizationManager) {
        application = applicationProvider.get();
        this.timeoutConfiguration = timeoutConfiguration;
        this.localizationManager = localizationManager;
    }

    @Override
    public boolean waitForTrue(BooleanSupplier condition, String message) {
        try {
            waitForTrue(condition, timeoutConfiguration.getCondition(), timeoutConfiguration.getPollingInterval(), message);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public void waitForTrue(BooleanSupplier condition, long timeoutInSeconds, long pollingIntervalInMilliseconds, String message) throws TimeoutException {
        if (condition == null) {
            throw new IllegalArgumentException(localizationManager.getLocalizedMessage("loc.wait.condition.cant.be.null"));
        }

        double startTime = getCurrentTime();
        while (true) {
            if (condition.getAsBoolean()) {
                return;
            }

            double currentTime = getCurrentTime();
            if ((currentTime - startTime) > timeoutInSeconds) {
                String exceptionMessage = String.format(localizationManager.getLocalizedMessage("loc.wait.timeout.condition"), timeoutInSeconds, message);
                throw new TimeoutException(exceptionMessage);
            }

            try {
                Thread.sleep(pollingIntervalInMilliseconds);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public <T> T waitFor(ExpectedCondition<T> condition, String message) {
        return waitFor(condition,
                timeoutConfiguration.getCondition(),
                timeoutConfiguration.getPollingInterval(),
                message,
                Collections.singleton(StaleElementReferenceException.class));
    }

    @Override
    public <T> T waitFor(ExpectedCondition<T> condition, long timeOutInSeconds, long pollingIntervalInMilliseconds, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        application.setImplicitWaitTimeout(0, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(application.getDriver(), timeOutInSeconds);
        wait.pollingEvery(Duration.ofMillis(pollingIntervalInMilliseconds));
        wait.withMessage(message);
        wait.ignoreAll(exceptionsToIgnore);
        try {
            return wait.until(condition);
        } finally {
            application.setImplicitWaitTimeout(timeoutConfiguration.getImplicit(), TimeUnit.SECONDS);
        }
    }

    private double getCurrentTime() {
        return System.nanoTime() / Math.pow(10, 9);
    }
}

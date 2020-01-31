package aquality.selenium.core.waitings;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.localization.ILocalizationManager;
import com.google.common.base.Strings;
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
    public ConditionalWait(Provider<IApplication> applicationProvider, ITimeoutConfiguration timeoutConfiguration, ILocalizationManager localizationManager) {
        application = applicationProvider.get();
        this.timeoutConfiguration = timeoutConfiguration;
        this.localizationManager = localizationManager;
    }

    @Override
    public boolean waitFor(BooleanSupplier condition, Long timeoutInSeconds, Long pollingIntervalInMilliseconds, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        try {
            waitForTrue(condition, timeoutInSeconds, pollingIntervalInMilliseconds, message, exceptionsToIgnore);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public void waitForTrue(BooleanSupplier condition, Long timeoutInSeconds, Long pollingIntervalInMilliseconds, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) throws TimeoutException {
        if (condition == null) {
            throw new IllegalArgumentException(localizationManager.getLocalizedMessage("loc.wait.condition.cant.be.null"));
        }

        Long timeout = resolveConditionTimeout(timeoutInSeconds);
        Long pollingInterval = resolvePollingInterval(pollingIntervalInMilliseconds);
        String exMessage = resolveMessage(message);
        double startTime = getCurrentTime();
        while (true) {
            if (isConditionSatisfied(condition, exceptionsToIgnore)) {
                return;
            }

            double currentTime = getCurrentTime();
            if ((currentTime - startTime) > timeout) {
                String exceptionMessage = localizationManager.getLocalizedMessage("loc.wait.timeout.condition", timeout, exMessage);
                throw new TimeoutException(exceptionMessage);
            }

            try {
                Thread.sleep(pollingInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public <T> T waitFor(ExpectedCondition<T> condition, Long timeoutInSeconds, Long pollingIntervalInMilliseconds, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        application.setImplicitWaitTimeout(0, TimeUnit.SECONDS);
        Long timeout = resolveConditionTimeout(timeoutInSeconds);
        Long pollingInterval = resolvePollingInterval(pollingIntervalInMilliseconds);
        String exMessage = resolveMessage(message);
        WebDriverWait wait = new WebDriverWait(application.getDriver(), timeout);
        wait.pollingEvery(Duration.ofMillis(pollingInterval));
        wait.withMessage(exMessage);
        wait.ignoreAll(exceptionsToIgnore == null ? Collections.singleton(StaleElementReferenceException.class) : exceptionsToIgnore);
        try {
            return wait.until(condition);
        } finally {
            application.setImplicitWaitTimeout(timeoutConfiguration.getImplicit(), TimeUnit.SECONDS);
        }
    }

    private double getCurrentTime() {
        return System.nanoTime() / Math.pow(10, 9);
    }

    private boolean isConditionSatisfied(BooleanSupplier condition, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        try {
            return condition.getAsBoolean();
        } catch (Exception e) {
            if (exceptionsToIgnore == null || !exceptionsToIgnore.contains(e.getClass())) {
                throw e;
            }

            return false;
        }
    }

    private Long resolveConditionTimeout(Long timeout) {
        return timeout == null ? timeoutConfiguration.getCommand() : timeout;
    }

    private Long resolvePollingInterval(Long timeout) {
        return timeout == null ? timeoutConfiguration.getPollingInterval() : timeout;
    }

    private String resolveMessage(String message) {
        return Strings.isNullOrEmpty(message) ? "" : message;
    }
}

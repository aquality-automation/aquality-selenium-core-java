package aquality.selenium.core.waitings;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import java.util.function.BooleanSupplier;

public class ConditionalWait implements IConditionalWait {

    private final Provider<IApplication> applicationProvider;
    private final ITimeoutConfiguration timeoutConfiguration;

    @Inject
    public ConditionalWait(Provider<IApplication> applicationProvider, ITimeoutConfiguration timeoutConfiguration) {
        this.applicationProvider = applicationProvider;
        this.timeoutConfiguration = timeoutConfiguration;
    }

    @Override
    public boolean waitFor(BooleanSupplier condition, Duration timeout, Duration pollingInterval, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        try {
            waitForTrue(condition, timeout, pollingInterval, message, exceptionsToIgnore);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    @Override
    public void waitForTrue(BooleanSupplier condition, Duration timeout, Duration pollingInterval, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) throws TimeoutException {
        BooleanSupplier supplier = Optional.ofNullable(condition).orElseThrow(() -> new IllegalArgumentException("Condition cannot be null"));
        long timeoutInSeconds = resolveConditionTimeoutInSeconds(timeout);
        long pollingIntervalInMilliseconds = resolvePollingInterval(pollingInterval).toMillis();
        String exMessage = resolveMessage(message);
        double startTime = getCurrentTime();
        while (true) {
            if (isConditionSatisfied(supplier, exceptionsToIgnore)) {
                return;
            }

            double currentTime = getCurrentTime();
            if ((currentTime - startTime) > timeoutInSeconds) {
                String exceptionMessage = String.format("Timed out after %1$s seconds during wait for condition '%2$s'", timeout, exMessage);
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
    public <T> T waitFor(ExpectedCondition<T> condition, Duration timeout, Duration pollingInterval, String message, Collection<Class<? extends Throwable>> exceptionsToIgnore) {
        IApplication app = applicationProvider.get();
        app.setImplicitWaitTimeout(Duration.ZERO);
        long timeoutInSeconds = resolveConditionTimeoutInSeconds(timeout);
        Duration actualPollingInterval = resolvePollingInterval(pollingInterval);
        String exMessage = resolveMessage(message);
        WebDriverWait wait = new WebDriverWait(app.getDriver(), timeoutInSeconds);
        wait.pollingEvery(actualPollingInterval);
        wait.withMessage(exMessage);
        wait.ignoreAll(exceptionsToIgnore == null ? Collections.singleton(StaleElementReferenceException.class) : exceptionsToIgnore);
        try {
            return wait.until(condition);
        } finally {
            app.setImplicitWaitTimeout(timeoutConfiguration.getImplicit());
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

    private long resolveConditionTimeoutInSeconds(Duration timeout) {
        return Optional.ofNullable(timeout).orElse(timeoutConfiguration.getCondition()).getSeconds();
    }

    private Duration resolvePollingInterval(Duration pollingInterval) {
        return Optional.ofNullable(pollingInterval).orElse(timeoutConfiguration.getPollingInterval());
    }

    private String resolveMessage(String message) {
        return Strings.isNullOrEmpty(message) ? "" : message;
    }
}

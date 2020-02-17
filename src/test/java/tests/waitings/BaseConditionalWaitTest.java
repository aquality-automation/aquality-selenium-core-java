package tests.waitings;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.waitings.ConditionalWait;
import com.google.inject.Provider;
import org.testng.annotations.AfterMethod;
import tests.applications.browser.AqualityServices;
import utils.Timer;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

abstract class BaseConditionalWaitTest {
    static final Duration waitForTimeoutCondition = Duration.ofSeconds(10);
    static final Duration waitForTimeoutPolling = Duration.ofMillis(150);
    static final double accuracy = 2;
    static final Collection<Class<? extends Throwable>> ignoredExceptions = Collections.singleton(IllegalStateException.class);
    ThreadLocal<Timer> timer = ThreadLocal.withInitial(Timer::new);
    protected Provider<IApplication> application = AqualityServices.getServiceProvider().getProvider(IApplication.class);
    ITimeoutConfiguration timeoutConfiguration = AqualityServices.getServiceProvider().getInstance(ITimeoutConfiguration.class);
    ConditionalWait conditionalWait = new ConditionalWait(application, timeoutConfiguration);

    @AfterMethod
    public void stopTimer(){
        timer.get().stop();
    }

    BooleanSupplier throwNewException(AtomicBoolean atomicBoolean) {
        return () -> {
            if (atomicBoolean.get()) {
                atomicBoolean.set(false);
                throw new IllegalStateException("");
            }

            return true;
        };
    }
}

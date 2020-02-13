package tests.waitings;

import aquality.selenium.core.applications.IApplication;
import aquality.selenium.core.configurations.ITimeoutConfiguration;
import aquality.selenium.core.waitings.ConditionalWait;
import com.google.inject.Injector;
import com.google.inject.Provider;
import org.testng.annotations.AfterMethod;
import tests.application.browser.AqualityServices;
import utils.Timer;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BooleanSupplier;

class BaseConditionalWaitTest {
    static final long waitForTimeoutCondition = 10;
    static final long waitForTimeoutPolling = 150;
    static final double accuracy = 0.5;
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

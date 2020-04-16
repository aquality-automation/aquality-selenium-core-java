package tests.applications;

import aquality.selenium.core.applications.AqualityModule;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import tests.applications.browser.ChromeApplication;

public class TestModule extends AqualityModule<ChromeApplication> {
    public TestModule(Provider<ChromeApplication> applicationProvider) {
        super(applicationProvider);
    }

    @Override
    protected void configure() {
        super.configure();
        bind(ICustomDependency.class).to(CustomDependency.class);
        bind(ICustomConfiguration.class).to(CustomConfiguration.class).in(Singleton.class);
    }
}

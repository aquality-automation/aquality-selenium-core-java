package tests.applications;

import aquality.selenium.core.applications.AqualityModule;
import com.google.inject.Provider;
import tests.applications.browser.ChromeApplication;

public class TestModule extends AqualityModule<ChromeApplication> {
    public TestModule(Provider<ChromeApplication> applicationProvider) {
        super(applicationProvider);
    }

    @Override
    protected void configure() {
        super.configure();
        bind(ICustomDependency.class).to(CustomDependency.class);
    }
}

package tests.application;

import aquality.selenium.core.application.AqualityModule;
import com.google.inject.Provider;
import tests.application.browser.ChromeApplication;

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
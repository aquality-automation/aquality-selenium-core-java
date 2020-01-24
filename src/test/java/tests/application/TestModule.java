package tests.application;

import aquality.selenium.core.application.AqualityModule;

public class TestModule extends AqualityModule {

    @Override
    protected void configure() {
        super.configure();
        bind(ICustomDependency.class).to(CustomDependency.class);
    }
}
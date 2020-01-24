package tests.application;

import com.google.inject.AbstractModule;

public class TestModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ICustomDependency.class).to(CustomDependency.class);
    }
}
package tests.utilities;

import aquality.selenium.core.application.AqualityModule;
import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Provider;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;
import tests.application.browser.ChromeApplication;

import java.util.List;
import java.util.Map;

public class CustomSettingsFileTests {

    @Test
    public void testShouldBePossibleToOverrideSettingsFile() {
        CustomAqualityServices.initInjector(new CustomModule(CustomAqualityServices::getApplication));
        ISettingsFile jsonSettingsFile = CustomAqualityServices.getServiceProvider().getInstance(ISettingsFile.class);
        Object value = jsonSettingsFile.getValue("/timeouts/timeoutPollingInterval");
        Assert.assertNull(value, "Value should be got from CustomSettingsFile");
    }

    @AfterMethod
    public void after(){
        CustomAqualityServices.initInjector(new AqualityModule<>(CustomAqualityServices::getApplication));
    }

    private class CustomModule extends AqualityModule<ChromeApplication> {

        CustomModule(Provider<ChromeApplication> applicationProvider) {
            super(applicationProvider);
        }

        @Override
        protected ISettingsFile getSettings() {
            return new CustomSettingsFile();
        }
    }

    private class CustomSettingsFile implements ISettingsFile {

        @Override
        public Object getValue(String path) {
            return null;
        }

        @Override
        public List<String> getList(String path) {
            return null;
        }

        @Override
        public Map<String, Object> getMap(String path) {
            return null;
        }

        @Override
        public boolean isValuePresent(String path) {
            return false;
        }
    }
}

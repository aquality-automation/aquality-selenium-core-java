package tests.configurations;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseProfileTest {

    protected static final String PROFILE_KEY = "profile";
    private String previousProfile;

    @BeforeMethod
    public void before() {
        previousProfile = System.getProperty(PROFILE_KEY);
    }

    @AfterMethod
    public void after() {
        if (previousProfile == null) {
            System.clearProperty(PROFILE_KEY);
        } else {
            System.setProperty(PROFILE_KEY, previousProfile);
        }
    }
}

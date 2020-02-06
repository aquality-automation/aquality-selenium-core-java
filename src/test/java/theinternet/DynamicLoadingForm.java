package theinternet;

import org.openqa.selenium.By;

public class DynamicLoadingForm {

    private static final long DEFAULT_LOADING_TIMEOUT = 5L;
    private static final long SMALL_LOADING_TIMEOUT = 2L;

    private DynamicLoadingForm(){
    }

    public static By getLoadingLabelLocator() {
        return By.id("loading");
    }

    public static By getStartButtonLocator() {
        return By.xpath("//div[@id='start']/button");
    }

    public static long getTimeout() {
        return DEFAULT_LOADING_TIMEOUT;
    }

    public static long getSmallTimeout() {
        return SMALL_LOADING_TIMEOUT;
    }

    public static By getLblFinish() {
        return By.id("finish");
    }
}

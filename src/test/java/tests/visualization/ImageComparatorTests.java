package tests.visualization;

import aquality.selenium.core.visualization.IImageComparator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.applications.browser.AqualityServices;
import tests.applications.browser.ITheInternetPageTest;
import theinternet.DynamicLoadingForm;
import theinternet.TheInternetPage;

import java.awt.*;

public class ImageComparatorTests implements ITheInternetPageTest {
    private final IImageComparator imageComparator = AqualityServices.get(IImageComparator.class);

    private void startLoading() {
        DynamicLoadingForm.getStartLabel().click();
    }

    @Override
    @BeforeMethod
    public void beforeMethod() {
        navigate(TheInternetPage.DYNAMIC_LOADING);
    }

    @Test
    public void testGetPercentageDifferenceForSameElement() {
        Image firstImage = DynamicLoadingForm.getStartLabel().visual().getImage();
        Image secondImage = DynamicLoadingForm.getStartLabel().visual().getImage();

        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage), 0);
    }

    @Test
    public void testGetPercentageDifferenceForSameElementWithZeroThreshold() {
        Image firstImage = DynamicLoadingForm.getStartLabel().visual().getImage();
        Image secondImage = DynamicLoadingForm.getStartLabel().visual().getImage();

        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage, 0), 0);
    }

    @Test
    public void testGetPercentageDifferenceForDifferentElements() {
        Image firstImage = DynamicLoadingForm.getStartLabel().visual().getImage();
        startLoading();
        Image secondImage = DynamicLoadingForm.getLoadingLabel().visual().getImage();

        Assert.assertNotEquals(imageComparator.percentageDifference(firstImage, secondImage), 0);
    }

    @Test
    public void testGetPercentageDifferenceForDifferentElementsWithFullThreshold() {
        final int threshold = 1;
        Image firstImage = DynamicLoadingForm.getStartLabel().visual().getImage();
        startLoading();
        Image secondImage = DynamicLoadingForm.getLoadingLabel().visual().getImage();

        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage, threshold), 0);
    }

    @Test
    public void testGetPercentageDifferenceForSimilarElements() throws InterruptedException {
        startLoading();
        Image firstImage = DynamicLoadingForm.getLoadingLabel().visual().getImage();
        Thread.sleep(300);
        Image secondImage = DynamicLoadingForm.getLoadingLabel().visual().getImage();

        Assert.assertTrue(imageComparator.percentageDifference(firstImage, secondImage, 0) != 0,
                "With zero threshold, there should be some difference");
        Assert.assertTrue(imageComparator.percentageDifference(firstImage, secondImage, 0.2f) <= 0.3,
                "With 0.2f threshold, the difference should be less or equal than 0.3");
        Assert.assertTrue(imageComparator.percentageDifference(firstImage, secondImage, 0.4f) <= 0.2,
                "With 0.4f threshold, the difference should be less or equal than 0.2");
        Assert.assertTrue(imageComparator.percentageDifference(firstImage, secondImage, 0.6f) <= 0.1,
                "With 0.6f threshold, the difference should be less or equal than 0.1");
        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage, 0.6f), 0,
                "With 0.8f threshold, the difference should be 0");
    }
}

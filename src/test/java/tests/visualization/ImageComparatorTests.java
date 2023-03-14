package tests.visualization;

import aquality.selenium.core.visualization.IImageComparator;
import aquality.selenium.core.visualization.ImageFunctions;
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

    private Image getStartImage() {
        return ImageFunctions.getScreenshotAsImage(DynamicLoadingForm.getStartLabel().getElement());
    }

    private Image getLoadingImage() {
        return ImageFunctions.getScreenshotAsImage(DynamicLoadingForm.getLoadingLabel().getElement());
    }

    @Override
    @BeforeMethod
    public void beforeMethod() {
        navigate(TheInternetPage.DYNAMIC_LOADING);
    }

    @Test
    public void testGetPercentageDifferenceForSameElement() {
        Image firstImage = getStartImage();
        Image secondImage = getStartImage();

        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage), 0);
    }

    @Test
    public void testGetPercentageDifferenceForSameElementWithZeroThreshold() {
        Image firstImage = getStartImage();
        Image secondImage = getStartImage();

        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage, 0), 0);
    }

    @Test
    public void testGetPercentageDifferenceForDifferentElements() {
        Image firstImage = getStartImage();
        startLoading();
        Image secondImage = getLoadingImage();

        Assert.assertNotEquals(imageComparator.percentageDifference(firstImage, secondImage), 0);
    }

    @Test
    public void testGetPercentageDifferenceForDifferentElementsWithFullThreshold() {
        final int threshold = 1;
        Image firstImage = getStartImage();
        startLoading();
        Image secondImage = getLoadingImage();

        Assert.assertEquals(imageComparator.percentageDifference(firstImage, secondImage, threshold), 0);
    }

    @Test
    public void testGetPercentageDifferenceForSimilarElements() {
        startLoading();
        Image firstImage = getLoadingImage();
        DynamicLoadingForm.waitUntilLoaderChanged(firstImage.getHeight(null));
        Image secondImage = getLoadingImage();

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

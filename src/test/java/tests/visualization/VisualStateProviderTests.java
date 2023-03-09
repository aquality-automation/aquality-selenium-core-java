package tests.visualization;

import aquality.selenium.core.visualization.IVisualStateProvider;
import aquality.selenium.core.visualization.ImageFunctions;
import aquality.selenium.core.waitings.IConditionalWait;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.applications.browser.AqualityServices;
import tests.applications.browser.ITheInternetPageTest;
import theinternet.DynamicLoadingForm;
import theinternet.TheInternetPage;

import java.awt.*;

public class VisualStateProviderTests implements ITheInternetPageTest {
    private void startLoading() {
        DynamicLoadingForm.getStartLabel().click();
    }

    private IVisualStateProvider getStartVisual() {
        return DynamicLoadingForm.getStartLabel().visual();
    }

    private IVisualStateProvider getLoadingVisual() {
        return DynamicLoadingForm.getLoadingLabel().visual();
    }

    @Override
    @BeforeMethod
    public void beforeMethod() {
        navigate(TheInternetPage.DYNAMIC_LOADING);
    }

    @Test
    public void testGetElementSize() {
        Dimension actualSize = getStartVisual().getSize();
        Assert.assertNotNull(actualSize);
        Assert.assertNotEquals(actualSize, new Dimension());
    }

    @Test
    public void testGetElementLocation() {
        Point actualLocation = getStartVisual().getLocation();
        Assert.assertNotNull(actualLocation);
        Assert.assertNotEquals(actualLocation, new Point());
    }

    @Test
    public void testGetElementImage() {
        Image actualImage = getStartVisual().getImage();
        Assert.assertNotNull(actualImage);
        Assert.assertNotEquals(ImageFunctions.getSize(actualImage), new Dimension());
    }

    @Test
    public void testGetPercentageDifferenceForSameElement() {
        Image firstImage = getStartVisual().getImage();
        Assert.assertEquals(getStartVisual().getDifference(firstImage), 0);
    }

    @Test
    public void testGetPercentageDifferenceForSameElementWithZeroThreshold() {
        Image firstImage = getStartVisual().getImage();
        Assert.assertEquals(getStartVisual().getDifference(firstImage, 0), 0);
    }

    @Test
    public void testGetPercentageDifferenceForDifferentElements() {
        Image firstImage = getStartVisual().getImage();
        startLoading();
        Assert.assertNotEquals(getLoadingVisual().getDifference(firstImage), 0);
    }

    @Test
    public void testGetPercentageDifferenceForDifferentElementsWithFullThreshold() {
        Image firstImage = getStartVisual().getImage();
        startLoading();
        Assert.assertEquals(getLoadingVisual().getDifference(firstImage, 1), 0);
    }

    @Test
    public void testGetPercentageDifferenceForSimilarElements() throws InterruptedException {
        startLoading();
        Image firstImage = getLoadingVisual().getImage();
        AqualityServices.get(IConditionalWait.class).waitFor(
                () -> firstImage.getHeight(null) < getLoadingVisual().getSize().getHeight());
        Assert.assertNotEquals(getLoadingVisual().getDifference(firstImage, 0), 0);
        Assert.assertTrue(getLoadingVisual().getDifference(firstImage, 0.2f) <= 0.3);
        Assert.assertTrue(getLoadingVisual().getDifference(firstImage, 0.4f) <= 0.2);
        Assert.assertTrue(getLoadingVisual().getDifference(firstImage, 0.6f) <= 0.1);
        Assert.assertEquals(getLoadingVisual().getDifference(firstImage, 0.8f), 0);
    }
}

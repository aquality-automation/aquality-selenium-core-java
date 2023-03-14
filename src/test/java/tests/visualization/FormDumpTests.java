package tests.visualization;

import aquality.selenium.core.configurations.IVisualizationConfiguration;
import aquality.selenium.core.configurations.VisualizationConfiguration;
import aquality.selenium.core.utilities.ISettingsFile;
import aquality.selenium.core.visualization.DumpManager;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.applications.browser.AqualityServices;
import tests.applications.browser.ITheInternetPageTest;
import theinternet.HoversForm;
import theinternet.TheInternetPage;

import javax.imageio.ImageIO;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.Arrays;

public class FormDumpTests implements ITheInternetPageTest {
    private String getPathToDumps() {
        return AqualityServices.get(IVisualizationConfiguration.class).getPathToDumps();
    }

    @Override
    @BeforeMethod
    public void beforeMethod() {
        navigate(TheInternetPage.HOVERS);
    }

    @Test
    public void shouldBePossibleToSaveFormDumpWithDefaultName()
    {
        HoversForm form = new HoversForm();
        File pathToDump = cleanUpAndGetPathToDump(form.getName().replace("/", " "));
        form.dump().save();
        Assert.assertTrue(pathToDump.exists() && pathToDump.isDirectory());
        File[] files = pathToDump.listFiles();
        Assert.assertTrue(files != null && files.length > 0, "Dump should contain some files");
    }

    @Test
    public void shouldBePossibleToSaveFormDumpWithSubFoldersInName()
    {
        HoversForm form = new HoversForm();
        String dumpName = String.format("SubFolder1%sSubFolder2", File.separatorChar);
        File pathToDump = cleanUpAndGetPathToDump(dumpName);
        form.dump().save(dumpName);
        File[] files = pathToDump.listFiles();
        Assert.assertTrue(files != null && files.length > 0, "Dump should contain some files");
    }

    @Test
    public void shouldBePossibleToCompareWithDumpWithCustomNameWhenDifferenceIsZero()
    {
        HoversForm form = new HoversForm();
        form.dump().save("Zero diff");
        Assert.assertEquals(form.dump().compare("Zero diff"), 0, "Difference with current page should be around zero");
    }

    @Test
    public void shouldBePossibleToCompareWithDumpWithCustomNameWhenDifferenceIsNotZero()
    {
        HoversForm form = new HoversForm();
        form.hoverAvatar();
        form.dump().save("Non-zero diff");
        AqualityServices.getApplication().getDriver().navigate().refresh();
        form.waitUntilPresent();
        Assert.assertTrue(form.dump().compare("Non-zero diff") > 0, "Difference with current page should be greater than zero");
    }

    @Test
    public void shouldBePossibleToCompareWithDumpWithCustomNameWhenElementSetDiffers()
    {
        HoversForm customForm = new HoversForm();
        customForm.dump().save("Set differs");
        customForm.setElementsForDump(HoversForm.ElementsFilter.INITIALIZED_AS_DISPLAYED);
        Assert.assertTrue(customForm.dump().compare("Set differs") > 0, "Difference with current page should be greater than zero if element set differs");
    }

    @Test
    public void shouldBePossibleToSaveAndCompareWithDumpWithCustomNameWhenAllElementsSelected()
    {
        HoversForm customForm = new HoversForm();
        customForm.setElementsForDump(HoversForm.ElementsFilter.ALL_ELEMENTS);
        customForm.dump().save("All elements");
        Assert.assertEquals(customForm.dump().compare("All elements"), 0, "Some elements should be failed to take image, but difference should be around zero");
    }

    @Test
    public void shouldBePossibleToSaveAndCompareWithDumpWithOverLengthDumpNameWhenAllElementsSelected() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        HoversForm customForm = new HoversForm();
        customForm.setElementsForDump(HoversForm.ElementsFilter.ALL_ELEMENTS);

        final Method getMaxNameLengthOfDumpElements = DumpManager.class.getDeclaredMethod("getMaxNameLengthOfDumpElements");
        getMaxNameLengthOfDumpElements.setAccessible(true);
        int maxElementNameLength = (int) getMaxNameLengthOfDumpElements.invoke(customForm.dump());
        getMaxNameLengthOfDumpElements.setAccessible(false);
        final Field imageExtension = DumpManager.class.getDeclaredField("imageExtension");
        imageExtension.setAccessible(true);
        int imageExtensionLength =  ((String) imageExtension.get(customForm.dump())).length();
        imageExtension.setAccessible(false);
        final Field maxFullFileNameLength = DumpManager.class.getDeclaredField("maxFullFileNameLength");
        maxFullFileNameLength.setAccessible(true);
        int maxLength = (int) maxFullFileNameLength.get(customForm.dump());
        maxFullFileNameLength.setAccessible(false);
        int pathToDumpLength = getPathToDumps().length();

        String dumpName = StringUtils.repeat('A', maxLength - pathToDumpLength - maxElementNameLength - imageExtensionLength);
        String overLengthDumpName = dumpName + "_BCDE";

        File overLengthPathToDump = cleanUpAndGetPathToDump(overLengthDumpName);
        File pathToDump = cleanUpAndGetPathToDump(dumpName);

        customForm.dump().save(overLengthDumpName);
        Assert.assertFalse(overLengthPathToDump.exists());
        Assert.assertTrue(pathToDump.exists());

        Assert.assertEquals(customForm.dump().compare(dumpName), 0, "Some elements should be failed to take image, but difference should be around zero");
    }

    @DataProvider
    private Object[] validFormats() {
        return ImageIO.getWriterFormatNames();
    }

    @Test (dataProvider = "validFormats")
    public void shouldBePossibleToSaveFormDumpWithValidExtension(String imageFormat) {
        LiteWebForm form = new LiteWebForm(imageFormat);
        String dumpName = String.format("Test .%s extension", imageFormat);
        File pathToDump = cleanUpAndGetPathToDump(dumpName);
        form.dump().save(dumpName);
        Assert.assertTrue(pathToDump.exists());
        File[] files = pathToDump.listFiles();
        Assert.assertTrue(files != null && files.length > 0, "Dump should contain some files");

        for (File file : files) {
            String name = file.getName();
            Assert.assertEquals(name.substring(name.lastIndexOf(".") + 1), imageFormat);
        }
    }

    @Test
    public void shouldBeImpossibleToSaveFormDumpWithInvalidExtension()
    {
        LiteWebForm form = new LiteWebForm("abc");
        String dumpName = "Test .abc extension";
        File pathToDump = cleanUpAndGetPathToDump(dumpName);
        Assert.assertThrows(IllegalArgumentException.class, () -> form.dump().save(dumpName));
        File[] files = pathToDump.listFiles();
        Assert.assertTrue(files == null || files.length == 0, "No dump files should be saved");
    }

    private File cleanUpAndGetPathToDump(String dumpName) {
        File pathToDump = Paths.get(getPathToDumps(), dumpName).toFile();
        File[] files = pathToDump.listFiles();
        if (pathToDump.exists() && files != null) {
            Arrays.stream(files).forEach(File::delete);
        }
        files = pathToDump.listFiles();
        Assert.assertEquals(files == null ? 0 : files.length, 0, "Dump directory should not contain any files before saving");
        return pathToDump;
    }

    private static class LiteWebForm extends HoversForm
    {
        private final String imageFormat;

        @Override
        protected IVisualizationConfiguration getVisualizationConfiguration() {
            return new CustomVisualizationConfiguration(imageFormat);
        }

        public LiteWebForm(String imageFormat)
        {
            this.imageFormat = imageFormat;
        }

        private class CustomVisualizationConfiguration extends VisualizationConfiguration
        {
            private final String imageFormat;

            @Override
            public String getImageFormat() {
                return imageFormat;
            }

            public CustomVisualizationConfiguration(String format)
            {
                super(AqualityServices.get(ISettingsFile.class));
                imageFormat = format;
            }
        }
    }
}

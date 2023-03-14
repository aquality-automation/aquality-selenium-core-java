package aquality.selenium.core.visualization;

import aquality.selenium.core.configurations.IVisualizationConfiguration;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.localization.ILocalizedLogger;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DumpManager<T extends IElement> implements IDumpManager {
    private static final String INVALID_CHARS_REGEX = "[\\\\/|*:\"'<>{}?%,]";

    private final Map<String, T> elementsForVisualization;
    private final String formName;
    private final ILocalizedLogger localizedLogger;
    private final String imageFormat;
    private final String imageExtension;
    private final int maxFullFileNameLength;
    private final String dumpsDirectory;

    public DumpManager(Map<String, T> elementsForVisualization, String formName, IVisualizationConfiguration visualConfiguration, ILocalizedLogger localizedLogger) {
        this.elementsForVisualization = elementsForVisualization;
        this.formName = formName;
        this.localizedLogger = localizedLogger;
        this.imageFormat = visualConfiguration.getImageFormat();
        ImageFunctions.validateImageFormat(this.imageFormat);
        this.imageExtension = String.format(".%s", imageFormat);
        this.maxFullFileNameLength = visualConfiguration.getMaxFullFileNameLength();
        this.dumpsDirectory = visualConfiguration.getPathToDumps();
    }

    @Override
    public float compare(String dumpName) {
        File directory = getDumpDirectory(dumpName);
        localizedLogger.info("loc.form.dump.compare", directory.getName());
        File[] imageFiles = getImageFiles(directory);
        Map<String, T> existingElements = filterElementsForVisualization().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        int countOfUnprocessedElements = existingElements.size();
        int countOfProceededElements = 0;
        float comparisonResult = 0f;
        List<String> absentOnFormElementNames = new ArrayList<>();
        for (File imageFile : imageFiles) {
            String key = imageFile.getName().replace(imageExtension, "");
            if (!existingElements.containsKey(key)) {
                localizedLogger.warn("loc.form.dump.elementnotfound", key);
                countOfUnprocessedElements++;
                absentOnFormElementNames.add(key);
            }
            else {
                comparisonResult += existingElements.get(key).visual().getDifference(ImageFunctions.readImage(imageFile));
                countOfUnprocessedElements--;
                countOfProceededElements++;
                existingElements.remove(key);
            }
        }
        logUnprocessedElements(countOfUnprocessedElements, existingElements, absentOnFormElementNames);
        // adding of countOfUnprocessedElements means 100% difference for each element absent in dump or on page
        float result = (comparisonResult + countOfUnprocessedElements) / (countOfProceededElements + countOfUnprocessedElements);
        localizedLogger.info("loc.form.dump.compare.result", result * 100);
        return result;
    }

    private void logUnprocessedElements(int countOfUnprocessedElements, Map<String, T> existingElements, List<String> absentOnFormElementNames) {
        if (countOfUnprocessedElements > 0)
        {
            if (!existingElements.isEmpty())
            {
                localizedLogger.warn("loc.form.dump.elementsmissedindump", String.join(", ", existingElements.keySet()));
            }
            if (!absentOnFormElementNames.isEmpty())
            {
                localizedLogger.warn("loc.form.dump.elementsmissedonform", String.join(", ", absentOnFormElementNames));
            }
            localizedLogger.warn("loc.form.dump.unprocessedelements", countOfUnprocessedElements);
        }
    }

    private File[] getImageFiles(File directory) {
        if (!directory.exists()) {
            throw new IllegalArgumentException(String.format("Dump directory [%s] does not exist.",
                    directory.getAbsolutePath()));
        }
        File[] imageFiles = directory.listFiles((dir, name) -> name.endsWith(imageExtension));
        if (imageFiles == null || imageFiles.length == 0) {
            throw new IllegalArgumentException(String.format("Dump directory [%s] does not contain any [*%s] files..",
                    directory.getAbsolutePath(), imageExtension));
        }
        return imageFiles;
    }

    @Override
    public void save(String dumpName) {
        File directory = cleanUpAndGetDumpDirectory(dumpName);
        localizedLogger.info("loc.form.dump.save", directory.getName());
        filterElementsForVisualization()
                .forEach(element -> {
                    try {
                        File file = Paths.get(directory.getAbsolutePath(), element.getKey() + imageExtension).toFile();
                        ImageFunctions.save(element.getValue().visual().getImage(), file, imageFormat);
                    } catch (Exception e) {
                        localizedLogger.fatal("loc.form.dump.imagenotsaved", e, element.getKey(), e.getMessage());
                    }
                });
    }

    protected List<Map.Entry<String, T>> filterElementsForVisualization() {
        return elementsForVisualization.entrySet().stream()
                .filter(element -> element.getValue().state().isDisplayed())
                .collect(Collectors.toList());
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected File cleanUpAndGetDumpDirectory(String dumpName) {
        File directory = getDumpDirectory(dumpName);
        if (directory.exists()) {
            File[] dirFiles = directory.listFiles();
            if (dirFiles != null) {
                Arrays.stream(dirFiles).filter(file -> !file.isDirectory()).forEach(File::delete);
            }
        }
        else {
            File dumpsDir = new File(dumpsDirectory);
            if (!dumpsDir.exists()) {
                dumpsDir.mkdir();
            }
            directory.mkdirs();
        }
        return directory;
    }

    protected int getMaxNameLengthOfDumpElements() {
        return elementsForVisualization.keySet().stream()
                .mapToInt(elementName -> elementName == null ? 0 : elementName.length())
                .max().orElse(0);
    }

    protected File getDumpDirectory(String dumpName) {
        // get the maximum length of the name among the form elements for the dump
        int maxNameLengthOfDumpElements = getMaxNameLengthOfDumpElements() + imageExtension.length();
        // get array of sub-folders in dump name
        String[] dumpSubFoldersNames = (dumpName == null ? formName : dumpName).split(Pattern.quote(File.separator));
        // create new dump name without invalid chars for each subfolder
        StringBuilder validDumpNameBuilder = new StringBuilder();
        for (String folderName : dumpSubFoldersNames) {
            String folderNameCopy = folderName.trim();
            folderNameCopy = folderNameCopy.replaceAll(INVALID_CHARS_REGEX, " ");
            if (folderNameCopy.endsWith(".")) {
                folderNameCopy = folderNameCopy.substring(0, folderNameCopy.length() - 1);
            }
            validDumpNameBuilder.append(folderNameCopy).append(File.separator);
        }
        String validDumpNameString = validDumpNameBuilder.toString();
        // create full dump path
        File fullDumpPath = Paths.get(dumpsDirectory, validDumpNameString).toFile();
        // cut off the excess length and log warn message
        if (fullDumpPath.getPath().length() + maxNameLengthOfDumpElements > maxFullFileNameLength)
        {
            validDumpNameString = validDumpNameString.substring(0,
                    maxFullFileNameLength - new File(dumpsDirectory).getAbsolutePath().length() - maxNameLengthOfDumpElements);
            fullDumpPath = Paths.get(dumpsDirectory, validDumpNameString).toFile();
            localizedLogger.warn("loc.form.dump.exceededdumpname", fullDumpPath);
        }

        return fullDumpPath;
    }
}

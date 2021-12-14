package tests.logger;

import aquality.selenium.core.applications.AqualityModule;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.logging.Logger;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.applications.browser.AqualityServices;
import tests.elements.factory.CustomWebElement;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.UUID;

import static org.testng.Assert.*;

public class LoggerTests {
    private static final String APPENDER_LOG_FILE_PATTERN = "target/log/appender-%s.log";
    private static final String TEST_MESSAGE = "test message";
    private static final String TEST_EXCEPTION_TEXT = "test exception";
    private static final String LOG_4_J_FIELD_NAME = "log4J";
    private static final String LOG_PAGE_SOURCE_ENVIRONMENT_VARIABLE = "logger.logPageSource";
    private static final String PAGE_SOURCE_MESSAGE = "Page source:";
    private Logger logger = Logger.getInstance();
    private org.apache.logging.log4j.Logger log4j;
    private Appender appender;
    private File appenderFile;

    @BeforeMethod
    private void addMessagesAppender() throws IOException {
        appenderFile = getRandomAppenderFile();
        appender = getFileAppender(appenderFile);
        logger.addAppender(appender);
    }

    @BeforeGroups("messages")
    private void initializeLog4jField() throws NoSuchFieldException, IllegalAccessException {
        Field log4jField = Logger.class.getDeclaredField(LOG_4_J_FIELD_NAME);
        log4jField.setAccessible(true);
        log4j = ((ThreadLocal<org.apache.logging.log4j.Logger>) log4jField.get(logger)).get();
    }

    @AfterMethod
    private void cleanUpLogPageSourceAndBrowser() {
        System.clearProperty(LOG_PAGE_SOURCE_ENVIRONMENT_VARIABLE);
        if (AqualityServices.isApplicationStarted()) {
            AqualityServices.getApplication().getDriver().quit();
        }
        if (log4j != null) {
            Configurator.setRootLevel(Level.DEBUG);
        }
    }

    @BeforeMethod
    public void cleanUpInjector() {
        AqualityServices.initInjector(new AqualityModule<>(AqualityServices::getApplication));
    }

    @Test(enabled = false)
    public void testShouldBePossibleLogPageSourceWhenIsEnabledAndElementAbsent() throws IOException {
        System.setProperty(LOG_PAGE_SOURCE_ENVIRONMENT_VARIABLE, "true");
        CustomWebElement label = new CustomWebElement(By.name("Absent element"), "Absent element",
                ElementState.EXISTS_IN_ANY_STATE);
        Assert.assertThrows(NoSuchElementException.class, () -> label.getElement(Duration.ZERO));
        assertTrue(isFileContainsText(appenderFile, PAGE_SOURCE_MESSAGE),
                String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), PAGE_SOURCE_MESSAGE));
    }

    @Test(enabled = false)
    public void testShouldBePossibleNotLogPageSourceWhenIsDisabledAndElementAbsent() throws IOException {
        System.setProperty(LOG_PAGE_SOURCE_ENVIRONMENT_VARIABLE, "false");
        CustomWebElement label = new CustomWebElement(By.name("Absent element"), "Absent element",
                ElementState.EXISTS_IN_ANY_STATE);
        Assert.assertThrows(NoSuchElementException.class, () -> label.getElement(Duration.ZERO));
        assertFalse(isFileContainsText(appenderFile, PAGE_SOURCE_MESSAGE),
                String.format("Log '%s' should not contain message '%s'.", appenderFile.getPath(), PAGE_SOURCE_MESSAGE));
    }

    @Test
    public void testAqualityServicesShouldReturnInstanceOfLogger() {
        assertEquals(logger, Logger.getInstance());
    }

    @Test
    public void testShouldBePossibleToAddAppender() throws IOException {
        logger.addAppender(appender).info(TEST_MESSAGE);
        assertTrue(appenderFile.exists(), String.format("New appender should be added to log4j. File '%s' should be created.", appenderFile.getPath()));
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test
    public void testShouldBePossibleToRemoveAppender() throws IOException {
        logger.addAppender(appender).removeAppender(appender).info(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("New appender should be removed from log4j. File '%s' should be empty.", appenderFile.getPath()));
    }

    @Test(groups = "messages")
    public void testInfoMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.FATAL);
        logger.info(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        Configurator.setRootLevel(Level.INFO);
        logger.info(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testInfoMessageWithParametersShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.FATAL);
        logger.info("%s", TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        Configurator.setRootLevel(Level.INFO);
        logger.info("%s", TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testDebugMessageWithParametersShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.WARN);
        logger.debug("%s", TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        Configurator.setRootLevel(Level.DEBUG);
        logger.debug("%s", TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testDebugMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.WARN);
        logger.debug(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertFalse(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));

        Configurator.setRootLevel(Level.DEBUG);
        logger.debug(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testDebugMessageWithThrowableShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.WARN);
        logger.debug(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertFalse(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));

        Configurator.setRootLevel(Level.DEBUG);
        logger.debug(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertTrue(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));
    }

    @Test(groups = "messages")
    public void testWarnMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.ERROR);
        logger.warn(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        Configurator.setRootLevel(Level.WARN);
        logger.warn(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testFatalMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.OFF);
        logger.fatal(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertFalse(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));

        Configurator.setRootLevel(Level.FATAL);
        logger.fatal(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertTrue(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));
    }

    @Test(groups = "messages")
    public void testErrorMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        Configurator.setRootLevel(Level.FATAL);
        logger.error(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        Configurator.setRootLevel(Level.ERROR);
        logger.error(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    private Appender getFileAppender(File file) throws IOException {
        Layout layout = PatternLayout.newBuilder().withPattern("%m%n").build();
        FileAppender fileAppender = FileAppender.newBuilder().setName("test")
                .setLayout(layout)
                .withFileName(file.getPath())
                .withAppend(true)
                .build();
        return fileAppender;
    }

    private boolean isFileContainsText(File file, String line) throws IOException {
        if (!file.exists()) {
            return false;
        }
        return String.join("", Files.readAllLines(file.toPath(), StandardCharsets.UTF_8)).contains(line);
    }

    private File getRandomAppenderFile() {
        return new File(String.format(APPENDER_LOG_FILE_PATTERN, UUID.randomUUID()));
    }

    @AfterMethod
    private void removeFileAppenderFromLogger() {
        logger.removeAppender(appender);
    }
}

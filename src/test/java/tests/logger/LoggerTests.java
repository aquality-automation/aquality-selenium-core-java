package tests.logger;

import aquality.selenium.core.logging.Logger;
import org.apache.log4j.*;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import tests.application.CustomAqualityServices;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import static org.testng.Assert.*;

public class LoggerTests {
    private static final String APPENDER_LOG_FILE_PATTERN = "target/log/appender-%s.log";
    private static final String TEST_MESSAGE = "test message";
    private static final String TEST_EXCEPTION_TEXT = "test exception";
    private static final String LOG_4_J_FIELD_NAME = "log4J";
    private Logger logger = Logger.getInstance();
    private org.apache.log4j.Logger log4j;
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
        log4j = ((ThreadLocal<org.apache.log4j.Logger>) log4jField.get(logger)).get();
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
        log4j.setLevel(Level.FATAL);
        logger.info(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        log4j.setLevel(Level.INFO);
        logger.info(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testInfoMessageWithParametersShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.FATAL);
        logger.info("%s", TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        log4j.setLevel(Level.INFO);
        logger.info("%s", TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testDebugMessageWithParametersShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.WARN);
        logger.debug("%s", TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        log4j.setLevel(Level.DEBUG);
        logger.debug("%s", TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testDebugMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.WARN);
        logger.debug(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        log4j.setLevel(Level.DEBUG);
        logger.debug(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testDebugMessageWithThrowableShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.WARN);
        logger.debug(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertFalse(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));

        log4j.setLevel(Level.DEBUG);
        logger.debug(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertTrue(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));
    }

    @Test(groups = "messages")
    public void testWarnMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.ERROR);
        logger.warn(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        log4j.setLevel(Level.WARN);
        logger.warn(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    @Test(groups = "messages")
    public void testFatalMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.OFF);
        logger.fatal(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertFalse(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));

        log4j.setLevel(Level.FATAL);
        logger.fatal(TEST_MESSAGE, new Exception(TEST_EXCEPTION_TEXT));
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
        assertTrue(isFileContainsText(appenderFile, TEST_EXCEPTION_TEXT), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_EXCEPTION_TEXT));
    }

    @Test(groups = "messages")
    public void testErrorMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.FATAL);
        logger.error(TEST_MESSAGE);
        assertFalse(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));

        log4j.setLevel(Level.ERROR);
        logger.error(TEST_MESSAGE);
        assertTrue(isFileContainsText(appenderFile, TEST_MESSAGE), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), TEST_MESSAGE));
    }

    private Appender getFileAppender(File file) throws IOException {
        Layout layout = new PatternLayout("%m%n");
        RollingFileAppender fileAppender = new RollingFileAppender(layout, file.getPath());
        fileAppender.setName("test");
        fileAppender.setAppend(true);
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

package tests.logger;

import org.apache.log4j.*;
import org.testng.annotations.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;
import aquality.selenium.core.logging.Logger;
import tests.application.CustomAqualityServices;

import static org.testng.Assert.*;

public class LoggerTests {
    private final static String appenderLogFilePattern = "target/log/appender-%s.log";
    private final static String testMessage = "test message";
    private final static String testExceptionText = "test exception";
    private final static String log4jFieldName = "log4J";
    private Logger logger = CustomAqualityServices.getInjector().getInstance(Logger.class);
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
        Field log4jField = Logger.class.getDeclaredField(log4jFieldName);
        log4jField.setAccessible(true);
        log4j = ((ThreadLocal<org.apache.log4j.Logger>) log4jField.get(logger)).get();
    }

    @Test
    public void testAqualityServicesShouldReturnInstanceOfLogger() {
        assertEquals(logger, Logger.getInstance());
    }

    @Test
    public void testShouldBePossibleToAddAppender() throws IOException {
        logger.addAppender(appender).info(testMessage);
        assertTrue(appenderFile.exists(), String.format("New appender should be added to log4j. File '%s' should be created.", appenderFile.getPath()));
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
    }

    @Test
    public void testShouldBePossibleToRemoveAppender() throws IOException {
        logger.addAppender(appender).removeAppender(appender).info(testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("New appender should be removed from log4j. File '%s' should be empty.", appenderFile.getPath()));
    }

    @Test(groups = "messages")
    public void testInfoMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.FATAL);
        logger.info(testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));

        log4j.setLevel(Level.INFO);
        logger.info(testMessage);
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
    }

    @Test(groups = "messages")
    public void testInfoMessageWithParametersShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.FATAL);
        logger.info("%s", testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));

        log4j.setLevel(Level.INFO);
        logger.info("%s", testMessage);
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
    }

    @Test(groups = "messages")
    public void testDebugMessageWithParametersShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.WARN);
        logger.debug("%s", testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));

        log4j.setLevel(Level.DEBUG);
        logger.debug("%s", testMessage);
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
    }

    @Test(groups = "messages")
    public void testDebugMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.WARN);
        logger.debug(testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));

        log4j.setLevel(Level.DEBUG);
        logger.debug(testMessage);
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
    }

    @Test(groups = "messages")
    public void testDebugMessageWithThrowableShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.WARN);
        logger.debug(testMessage, new Exception(testExceptionText));
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));
        assertFalse(isFileContainsText(appenderFile, testExceptionText), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testExceptionText));

        log4j.setLevel(Level.DEBUG);
        logger.debug(testMessage, new Exception(testExceptionText));
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
        assertTrue(isFileContainsText(appenderFile, testExceptionText), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testExceptionText));
    }

    @Test(groups = "messages")
    public void testWarnMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.ERROR);
        logger.warn(testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));

        log4j.setLevel(Level.WARN);
        logger.warn(testMessage);
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
    }

    @Test(groups = "messages")
    public void testFatalMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.OFF);
        logger.fatal(testMessage, new Exception(testExceptionText));
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));
        assertFalse(isFileContainsText(appenderFile, testExceptionText), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testExceptionText));

        log4j.setLevel(Level.FATAL);
        logger.fatal(testMessage, new Exception(testExceptionText));
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
        assertTrue(isFileContainsText(appenderFile, testExceptionText), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testExceptionText));
    }

    @Test(groups = "messages")
    public void testErrorMessageShouldBeDisplayedAccordingToLogLevel() throws IOException {
        log4j.setLevel(Level.FATAL);
        logger.error(testMessage);
        assertFalse(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' shouldn't contain message '%s'.", appenderFile.getPath(), testMessage));

        log4j.setLevel(Level.ERROR);
        logger.error(testMessage);
        assertTrue(isFileContainsText(appenderFile, testMessage), String.format("Log '%s' should contain message '%s'.", appenderFile.getPath(), testMessage));
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
        return new File(String.format(appenderLogFilePattern, UUID.randomUUID()));
    }

    @AfterMethod
    private void removeFileAppenderFromLogger() {
        logger.removeAppender(appender);
    }
}
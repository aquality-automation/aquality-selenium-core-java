package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

import java.time.Duration;

public class TimeoutConfiguration implements ITimeoutConfiguration {

    private final ISettingsFile settingsFile;
    private final Duration condition;
    private final Duration pollInterval;
    private final Duration implicit;
    private final Duration command;

    @Inject
    public TimeoutConfiguration(ISettingsFile settingsFile) {
        this.settingsFile = settingsFile;
        condition = getDurationFromSeconds(TIMEOUT.CONDITION);
        pollInterval = Duration.ofMillis(getTimeout(TIMEOUT.POLL_INTERVAL));
        implicit = getDurationFromSeconds(TIMEOUT.IMPLICIT);
        command = getDurationFromSeconds(TIMEOUT.COMMAND);
    }

    private long getTimeout(TIMEOUT timeout) {
        return Long.valueOf(settingsFile.getValue(timeout.getKey()).toString());
    }

    private Duration getDurationFromSeconds(TIMEOUT timeout) {
        return Duration.ofSeconds(getTimeout(timeout));
    }

    public Duration getImplicit() {
        return implicit;
    }

    public Duration getCondition() {
        return condition;
    }

    public Duration getPollingInterval() {
        return pollInterval;
    }

    public Duration getCommand() {
        return command;
    }

    private enum TIMEOUT {
        IMPLICIT("/timeouts/timeoutImplicit"),
        CONDITION("/timeouts/timeoutCondition"),
        POLL_INTERVAL("/timeouts/timeoutPollingInterval"),
        COMMAND("/timeouts/timeoutCommand");

        private String key;

        TIMEOUT(String key) {
            this.key = key;
        }

        private String getKey() {
            return key;
        }
    }
}

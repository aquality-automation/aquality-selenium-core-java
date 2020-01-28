package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class TimeoutConfiguration implements ITimeoutConfiguration{

    private final ISettingsFile settingsFile;
    private final long condition;
    private final long pollInterval;
    private final long implicit;
    private final long command;

    @Inject
    public TimeoutConfiguration(ISettingsFile settingsFile) {
        this.settingsFile = settingsFile;
        condition = getTimeout(TIMEOUT.CONDITION);
        pollInterval = getTimeout(TIMEOUT.POLL_INTERVAL);
        implicit = getTimeout(TIMEOUT.IMPLICIT);
        command = getTimeout(TIMEOUT.COMMAND);
    }

    private long getTimeout(TIMEOUT timeout){
        return Long.valueOf(settingsFile.getValue("/timeouts/" + timeout.getKey()).toString());
    }

    public long getImplicit(){
        return implicit;
    }

    public long getCondition(){
        return condition;
    }

    public long getPollingInterval(){
        return pollInterval;
    }

    public long getCommand(){
        return command;
    }

    private enum TIMEOUT {
        IMPLICIT("timeoutImplicit"),
        CONDITION("timeoutCondition"),
        POLL_INTERVAL("timeoutPollingInterval"),
        COMMAND("timeoutCommand");

        private String key;
        TIMEOUT(String key){
            this.key = key;
        }

        private String getKey(){
            return key;
        }
    }
}

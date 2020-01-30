package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

public class RetryConfiguration implements IRetryConfiguration {
    private final int number;
    private final long pollingInterval;

    @Inject
    public RetryConfiguration(ISettingsFile settingsFile) {
        this.number = Integer.parseInt(settingsFile.getValue("/retry/number").toString());
        this.pollingInterval = Long.parseLong(settingsFile.getValue("/retry/pollingInterval").toString());
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public long getPollingInterval() {
        return pollingInterval;
    }
}

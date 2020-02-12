package aquality.selenium.core.configurations;

import aquality.selenium.core.utilities.ISettingsFile;
import com.google.inject.Inject;

import java.time.Duration;

public class RetryConfiguration implements IRetryConfiguration {
    private final int number;
    private final Duration pollingInterval;

    @Inject
    public RetryConfiguration(ISettingsFile settingsFile) {
        this.number = Integer.parseInt(settingsFile.getValue("/retry/number").toString());
        this.pollingInterval = Duration.ofMillis(Long.parseLong(settingsFile.getValue("/retry/pollingInterval").toString()));
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Duration getPollingInterval() {
        return pollingInterval;
    }
}

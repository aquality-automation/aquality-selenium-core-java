package aquality.selenium.core.utilities;

/**
 * Provides implementations for utilities module.
 */
public interface IUtilitiesModule {

    /**
     * @return implementation of {@link IActionRetrier}.
     */
    default Class<? extends IActionRetrier> getActionRetrierImplementation() {
        return ActionRetrier.class;
    }

    /**
     * @return implementation of {@link IElementActionRetrier}.
     */
    default Class<? extends IElementActionRetrier> getElementActionRetrierImplementation() {
        return ElementActionRetrier.class;
    }

    /**
     * Provides default {@link ISettingsFile} with settings.
     * Default value is settings.json.
     * You are able to override this path, by setting environment variable 'profile'.
     * In this case, settings file will be settings.{profile}.json.
     *
     * @return An instance of settings.
     */
    default ISettingsFile getInstanceOfSettingsFile() {
        String settingsProfile = System.getProperty("profile") == null ? "settings.json" : "settings." + System.getProperty("profile") + ".json";
        return new JsonSettingsFile(settingsProfile);
    }
}

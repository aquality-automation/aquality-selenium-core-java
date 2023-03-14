package aquality.selenium.core.logging;

/**
 * Describes interface that can log visual state.
 */
public interface ILogVisualState {
    /**
     * Logs element visual state.
     * @param messageKey key of localized message to log.
     * @param args values to put into localized message (if any).
     */
    void logVisualState(String messageKey, Object... args);
}

package aquality.selenium.core.elements.interfaces;

/**
 * Describes interface that can log element state.
 */
public interface ILogElementState {
    /**
     * Logs element state.
     * @param messageKey key of localized message to log.
     * @param stateKey key of localized state to log.
     */
    void logElementState(String messageKey, String stateKey);
}

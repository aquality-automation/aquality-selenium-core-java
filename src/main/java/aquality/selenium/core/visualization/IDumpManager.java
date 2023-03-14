package aquality.selenium.core.visualization;

/**
 * Describes dump manager for the form that could be used for visualization purposes, such as saving and comparing dumps.
 */
public interface IDumpManager {
    /**
     * Compares current form with the dump saved previously.
     *
     * @param dumpName custom name of the sub-folder where the dump was saved.
     * @return The difference of comparing the page to the dump as a percentage (no difference is 0%).
     * In the default implementation, it is calculated as sum of element differences divided by elements count.
     */
    float compare(String dumpName);

    /**
     * Compares current form with the dump saved previously.
     * Form name is used by default for the name of the sub-folder where the dump was saved.
     *
     * @return The difference of comparing the page to the dump as a percentage (no difference is 0%).
     * In the default implementation, it is calculated as sum of element differences divided by elements count.
     */
    default float compare() {
        return compare(null);
    }

    /**
     * Saves the dump of the current form.
     * In the default implementation, it is a set of screenshots of selected form elements.
     *
     * @param dumpName Name of the sub-folder where to save the dump.
     */
    void save(String dumpName);

    /**
     * Saves the dump of the current form.
     * In the default implementation, it is a set of screenshots of selected form elements.
     * Form name is used by default for the name of the sub-folder where to save the dump.
     */
    default void save() {
        save(null);
    }
}

package aquality.selenium.core.forms;

import aquality.selenium.core.visualization.IDumpManager;

/**
 * Describes form that could be used for visualization purposes, such as saving and comparing dumps.
 * See {@link aquality.selenium.core.visualization.IDumpManager} for more details.
 */
public interface IForm {
    /**
     * Name of the current form.
     *
     * @return form's name.
     */
    String getName();

    /**
     * Gets dump manager for the current form that could be used for visualization purposes,
     * such as saving and comparing dumps.
     *
     * @return form's dump manager.
     */
    IDumpManager dump();
}

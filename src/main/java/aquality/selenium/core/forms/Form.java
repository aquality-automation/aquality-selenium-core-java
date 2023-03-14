package aquality.selenium.core.forms;

import aquality.selenium.core.configurations.IVisualizationConfiguration;
import aquality.selenium.core.elements.Element;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElement;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.logging.Logger;
import aquality.selenium.core.visualization.DumpManager;
import aquality.selenium.core.visualization.IDumpManager;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Describes form that could be used for visualization purposes (see {@link IDumpManager}).
 *
 * @param <T> Base type(class or interface) of elements of this form.
 */
public abstract class Form<T extends IElement> implements IForm {
    private final Class<T> elementClass;

    /**
     * Initializes the form.
     *
     * @param elementClass Base type(class or interface) of elements of this form.
     */
    protected Form(Class<T> elementClass) {
        this.elementClass = elementClass;
    }

    /**
     * Visualization configuration used by {@link IDumpManager}.
     * Could be obtained from AqualityServices.
     *
     * @return visualization configuration.
     */
    protected abstract IVisualizationConfiguration getVisualizationConfiguration();

    /**
     * Localized logger used by {@link IDumpManager}.
     * Could be obtained from AqualityServices.
     *
     * @return localized logger.
     */
    protected abstract ILocalizedLogger getLocalizedLogger();

    /**
     * Name of the current form.
     *
     * @return form's name.
     */
    @Override
    public abstract String getName();

    /**
     * Gets dump manager for the current form that could be used for visualization purposes,
     * such as saving and comparing dumps.
     * Uses getElementsForVisualization() as basis for dump creation and comparison.
     *
     * @return form's dump manager.
     */
    @Override
    public IDumpManager dump() {
        return new DumpManager<>(getElementsForVisualization(), getName(), getVisualizationConfiguration(), getLocalizedLogger());
    }

    /**
     * List of pairs uniqueName-element to be used for dump saving and comparing.
     * By default, only currently displayed elements to be used with getDisplayedElements().
     * You can override this property with defined getAllElements(), getAllCurrentFormElements,
     * getElementsInitializedAsDisplayed(), or your own element set.
     *
     * @return elements for visualization.
     */
    protected Map<String, T> getElementsForVisualization() {
        return getDisplayedElements();
    }

    /**
     * List of pairs uniqueName-element from the current form and it's parent forms, which are currently displayed.
     *
     * @return list of displayed elements.
     */
    protected Map<String, T> getDisplayedElements() {
        return getAllElements().entrySet().stream().filter(element -> element.getValue().state().isDisplayed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * List of pairs uniqueName-element from the current form and it's parent forms, which were initialized as displayed.
     *
     * @return list of displayed elements.
     */
    protected Map<String, T> getElementsInitializedAsDisplayed() {
        return getAllElements().entrySet().stream().filter(element -> {
            try {
                Field stateField = Element.class.getDeclaredField("elementState");
                stateField.setAccessible(true);
                ElementState elementState = (ElementState) stateField.get(element.getValue());
                stateField.setAccessible(false);
                return ElementState.DISPLAYED == elementState;
            } catch (ReflectiveOperationException exception) {
                Logger.getInstance().fatal("Failed to read state the element: " + element.getKey(), exception);
                return false;
            }
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * List of pairs uniqueName-element from the current form and it's parent forms.
     *
     * @return list of elements.
     */
    protected Map<String, T> getAllElements() {
        Map<String, T> map = new HashMap<>();
        addElementsToMap(map, this.getClass().getDeclaredFields());
        Class<?> superClass = this.getClass().getSuperclass();
        while (superClass != null) {
            addElementsToMap(map, superClass.getDeclaredFields());
            superClass = superClass.getSuperclass();
        }
        return map;
    }

    /**
     * List of pairs uniqueName-element from the current form.
     *
     * @return list of elements.
     */
    protected Map<String, T> getAllCurrentFormElements() {
        Map<String, T> map = new HashMap<>();
        addElementsToMap(map, this.getClass().getDeclaredFields());
        return map;
    }

    /**
     * Adds pairs uniqueName-element from the specified fields array to map using the reflection.
     *
     * @param map map to save elements.
     * @param fields any class fields (only assignable from target type will be added to map).
     */
    protected void addElementsToMap(Map<String, T> map, Field[] fields) {
        for (Field field : fields) {
            try {
                if (elementClass.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    //noinspection unchecked
                    map.put(field.getName(), (T) field.get(this));
                    field.setAccessible(false);
                }
            }
            catch (IllegalAccessException exception) {
                Logger.getInstance().fatal("Failed to read the element: " + field.getName(), exception);
            }
        }
    }
}

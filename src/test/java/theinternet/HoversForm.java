package theinternet;

import aquality.selenium.core.configurations.IVisualizationConfiguration;
import aquality.selenium.core.elements.ElementState;
import aquality.selenium.core.elements.interfaces.IElementFactory;
import aquality.selenium.core.forms.Form;
import aquality.selenium.core.localization.ILocalizedLogger;
import aquality.selenium.core.logging.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import tests.applications.browser.AqualityServices;
import tests.elements.factory.CustomWebElement;
import tests.elements.factory.Label;

import java.util.Map;

public class HoversForm extends Form<CustomWebElement> {
    private static final By ContentLoc = By.xpath("//div[contains(@class,'example')]");
    private static final By HiddenElementsLoc = By.xpath("//h5");
    private static final By NotExistingElementsLoc = By.xpath("//h5");
    private static final By DisplayedElementsLoc = By.xpath("//div[contains(@class,'figure')]");

    private final Label displayedLabel = AqualityServices.get(IElementFactory.class).getCustomElement(
            Label::new, DisplayedElementsLoc, "I'm displayed field");
    private final Label displayedButInitializedAsExist = new Label(DisplayedElementsLoc, "I'm displayed but initialized as existing", ElementState.EXISTS_IN_ANY_STATE);
    private final Label notExistingButInitializedAsExist = new Label(HiddenElementsLoc, "I'm notExisting but initialized as existing", ElementState.EXISTS_IN_ANY_STATE);
    protected final Label displayedProtectedLabel = new Label(DisplayedElementsLoc, "I'm displayed protected", ElementState.DISPLAYED);
    private final Label hiddenLabel = new Label(HiddenElementsLoc, "I'm hidden", ElementState.EXISTS_IN_ANY_STATE);
    private final Label hiddenLabelInitializedAsDisplayed = new Label(HiddenElementsLoc, "I'm hidden but mask as displayed", ElementState.DISPLAYED);
    protected final Label contentLabel = new Label(ContentLoc, "Content", ElementState.DISPLAYED);
    private final Label contentDuplicateLabel = new Label(ContentLoc, "Content", ElementState.DISPLAYED);

    private Map<String, CustomWebElement> elementsToCheck;

    public HoversForm() {
        super(CustomWebElement.class);
    }

    public void clickOnContent() {
        contentLabel.click();
    }

    public void waitUntilPresent() {
        displayedLabel.state().waitForClickable();
    }

    public void hoverAvatar() {
        Logger.getInstance().info("Hovering avatar");
        new Actions(AqualityServices.getApplication().getDriver()).moveToElement(displayedLabel.getElement())
                .clickAndHold().build().perform();
        hiddenLabel.state().waitForDisplayed();
    }

    @Override
    protected IVisualizationConfiguration getVisualizationConfiguration() {
        return AqualityServices.get(IVisualizationConfiguration.class);
    }

    @Override
    protected ILocalizedLogger getLocalizedLogger() {
        return AqualityServices.get(ILocalizedLogger.class);
    }

    @Override
    public String getName() {
        return "Hovers Web page/form";
    }

    @Override
    protected Map<String, CustomWebElement> getElementsForVisualization() {
        if (elementsToCheck == null) {
            elementsToCheck = super.getElementsForVisualization();
        }
        return elementsToCheck;
    }

    public void setElementsForDump(ElementsFilter filter) {
        switch (filter) {
            case INITIALIZED_AS_DISPLAYED:
                elementsToCheck = getElementsInitializedAsDisplayed();
                break;
            case DISPLAYED_ELEMENTS:
                elementsToCheck = getDisplayedElements();
                break;
            case CURRENT_FORM_ELEMENTS:
                elementsToCheck = getAllCurrentFormElements();
                break;
            case ALL_ELEMENTS:
            default:
                elementsToCheck = getAllElements();
                break;
        }
    }

    public enum ElementsFilter {
        ALL_ELEMENTS,
        DISPLAYED_ELEMENTS,
        INITIALIZED_AS_DISPLAYED,
        CURRENT_FORM_ELEMENTS
    }
}

package aquality.selenium.core.visualization;

/**
 * Describes implementations of visualization services to be registered in DI container.
 */
public interface IVisualizationModule {
    /**
     * @return class which implements {@link IImageComparator}
     */
    default Class<? extends IImageComparator> getImageComparatorImplementation() {
        return ImageComparator.class;
    }
}

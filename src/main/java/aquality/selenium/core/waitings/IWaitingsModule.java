package aquality.selenium.core.waitings;

/**
 * Provides implementations for waitings module.
 */
public interface IWaitingsModule {

    /**
     * @return implementation of {@link IConditionalWait}.
     */
    default Class<? extends IConditionalWait> getConditionalWaitImplementation() {
        return ConditionalWait.class;
    }
}

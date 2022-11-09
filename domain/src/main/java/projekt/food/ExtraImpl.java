package projekt.food;

import java.util.function.Consumer;

/**
 * Concrete implementation of an extra.
 *
 * @param <C> the type of config this extra is applicable to
 */
class ExtraImpl<C extends Food.Config> implements Extra<C> {

    private final String name;
    private final int priority;
    private final Consumer<? super C> configMutator;

    /**
     * Constructs a new {@link ExtraImpl} object with the specified parameters.
     *
     * @param name          the name for this extra
     * @param priority      the priority for this extra implementation
     * @param configMutator the consumer to use when applying this extra
     */
    ExtraImpl(
        String name,
        int priority,
        Consumer<? super C> configMutator
    ) {
        this.name = name;
        this.priority = priority;
        this.configMutator = configMutator;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void apply(C config) {
        configMutator.accept(config);
    }
}

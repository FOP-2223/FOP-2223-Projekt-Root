package projekt.delivery.event;

@FunctionalInterface
public interface EventListener<E extends Event> {
    /**
     * Handles the provided event
     *
     * @param event The {@link Event} to handle
     */
    void handle(E event);
}

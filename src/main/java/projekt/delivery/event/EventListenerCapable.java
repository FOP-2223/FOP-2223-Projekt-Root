package projekt.delivery.event;

public interface EventListenerCapable<E extends Event> {

    Class<E> getType();

    EventListener<E> getListener();
}

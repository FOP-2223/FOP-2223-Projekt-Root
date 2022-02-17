package projekt.delivery.event;

public interface EventListenerRegistration<E extends Event> {

    Class<E> getType();

    EventListener<? super E> getListener();

    void cancel();
}

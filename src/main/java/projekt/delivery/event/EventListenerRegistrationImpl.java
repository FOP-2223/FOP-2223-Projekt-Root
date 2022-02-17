package projekt.delivery.event;

import java.util.function.Consumer;

class EventListenerRegistrationImpl<E extends Event> implements EventListenerRegistration<E> {

    private final Class<E> type;
    private final EventListener<? super E> listener;
    private final Consumer<? super EventListenerRegistration<E>> cancelCallback;

    EventListenerRegistrationImpl(
        Class<E> type,
        EventListener<? super E> listener,
        Consumer<? super EventListenerRegistration<E>> cancelCallback
    ) {
        this.type = type;
        this.listener = listener;
        this.cancelCallback = cancelCallback;
    }

    @Override
    public Class<E> getType() {
        return type;
    }

    @Override
    public EventListener<? super E> getListener() {
        return listener;
    }

    @Override
    public void cancel() {
        cancelCallback.accept(this);
    }
}

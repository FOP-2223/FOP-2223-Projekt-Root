package projekt.delivery.event;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private final Map<Class<?>, List<EventListenerRegistration<?>>> listeners = new HashMap<>();

    public <E extends Event> EventListenerRegistration<E> register(Class<E> type, EventListener<? super E> listener) {
        final EventListenerRegistration<E> registration = new EventListenerRegistrationImpl<>(type, listener, this::unregister);
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(registration);
        return registration;
    }

    public void unregister(EventListenerRegistration<?> registration) {
        listeners.computeIfPresent(registration.getType(), (t, l) -> {
            l.remove(registration);
            if (l.size() == 0) {
                // remove mapping if list is empty
                return null;
            } else {
                return l;
            }
        });
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void post(Class<? super E> type, E event) {
        final @Nullable List<EventListenerRegistration<? super E>> actualListeners =
            (List<EventListenerRegistration<? super E>>) listeners.get(type);
        if (actualListeners != null) {
            for (EventListenerRegistration<? super E> e : actualListeners) {
                e.getListener().handle(event);
            }
        }
        // recursively call events for supertypes
        for (Class<? super E> superType : (Class<? super E>[]) type.getInterfaces()) {
            // only post event if supertype is still a subtype of Event
            if (Event.class.isAssignableFrom(superType)) {
                post(superType, event);
            }
        }
    }
}

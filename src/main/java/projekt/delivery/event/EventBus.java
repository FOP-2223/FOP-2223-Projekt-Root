package projekt.delivery.event;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private final Map<Class<?>, List<EventListener<?>>> listeners = new HashMap<>();

    public <E extends Event> void addListener(Class<E> type, EventListener<? super E> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    @SuppressWarnings("unchecked")
    public <E extends Event> void post(Class<? super E> type, E event) {
        @Nullable List<EventListener<? super E>> actualListeners = (List<EventListener<? super E>>) listeners.get(type);
        if (actualListeners != null) {
            for (EventListener<? super E> e : actualListeners) {
                e.handle(event);
            }
        }
        // recursively call events for superinterfaces
        for (Class<? super E> superType : (Class<? super E>[]) type.getInterfaces()) {
            // only post event if supertype is still a subtype of Event
            if (Event.class.isAssignableFrom(superType)) {
                post(superType, event);
            }
        }
    }
}

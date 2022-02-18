package projekt.delivery.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {

    private final Map<Class<?>, List<EventListenerRegistration<?>>> listeners = new HashMap<>();
    private final Map<Class<?>, List<Event>> queuedEvents = new HashMap<>();
    private final List<MultiEventListener> multiListeners = new ArrayList<>();

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

    public <E extends Event> void queuePost(Class<? super E> type, E event) {
        queuedEvents.computeIfAbsent(type, k -> new ArrayList<>()).add(event);
    }

    public void sendPostQueue() {
        final List<Event> events = queuedEvents.values().stream()
            .flatMap(Collection::stream)
            .sorted(Comparator.comparing(Event::getVehicle))
            .toList();
        for (final MultiEventListener listener : multiListeners) {
            listener.handle(events);
        }
        for (final Map.Entry<Class<?>, List<Event>> entry : queuedEvents.entrySet()) {
            post(entry);
        }
        multiListeners.clear();
    }

    @SuppressWarnings("unchecked")
    private <E extends Event> void post(Map.Entry<Class<?>, List<Event>> entry) {
        final Class<? super E> type = (Class<? super E>) entry.getKey();
        final List<? extends E> events = (List<? extends E>) entry.getValue().stream()
            .sorted(Comparator.comparing(Event::getVehicle))
            .toList();
        for (final E event : events) {
            post(type, event);
        }
    }

    /**
     * Posts the provided {@link Event} to all listeners registered under the provided type.
     *
     * <p>
     * This method does not post to any {@link MultiEventListener MultiEventListeners}, as it only receives single events.
     * </p>
     *
     * @param type  The post event type
     * @param event The {@link Event} to post
     * @param <E>   The post event type
     * @param <A>   The actual event type
     */
    @SuppressWarnings("unchecked")
    public <E extends Event, A extends E> void post(Class<? super E> type, A event) {
        final List<EventListenerRegistration<?>> actualListeners = listeners.get(type);
        if (actualListeners != null) {
            for (EventListenerRegistration<?> e : actualListeners) {
                ((EventListenerRegistration<? super A>) e).getListener().handle(event);
            }
        }
        // recursively call events for supertypes
        for (final Class<? super E> superType : (Class<? super E>[]) type.getInterfaces()) {
            // only post event if supertype is still a subtype of Event
            if (Event.class.isAssignableFrom(superType)) {
                post(superType, event);
            }
        }
    }
}

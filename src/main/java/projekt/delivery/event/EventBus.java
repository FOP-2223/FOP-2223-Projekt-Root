package projekt.delivery.event;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EventBus {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Event> queuedEvents = new ArrayList<>();
    private final Map<LocalDateTime, List<Event>> log = new HashMap<>();
    private final Map<LocalDateTime, List<Event>> unmodifiableLog = Collections.unmodifiableMap(log);

    public void queuePost(Event event) {
        lock.readLock().lock();
        try {
            queuedEvents.add(event);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void queuePost(Collection<Event> events) {
        lock.readLock().lock();
        try {
            queuedEvents.addAll(events);
        } finally {
            lock.readLock().unlock();
        }
    }

    public List<Event> popEvents(LocalDateTime time) {
        // is not a read lock because the queue has to be cleared too
        lock.writeLock().lock();
        try {
            final List<Event> events = queuedEvents.stream()
                .sorted(Comparator.comparing(Event::getVehicle))
                .toList();
            log.put(time, events);
            queuedEvents.clear();
            return events;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<LocalDateTime, List<Event>> getLog() {
        return unmodifiableLog;
    }
}

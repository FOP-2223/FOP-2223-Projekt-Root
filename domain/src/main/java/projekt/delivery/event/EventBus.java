package projekt.delivery.event;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class EventBus {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final List<Event> queuedEvents = new ArrayList<>();
    private final Map<Long, List<Event>> log = new HashMap<>();
    private final Map<Long, List<Event>> unmodifiableLog = Collections.unmodifiableMap(log);

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

    public List<Event> popEvents(long tick) {
        // is not a read lock because the queue has to be cleared too
        lock.writeLock().lock();
        try {
            final List<Event> events = queuedEvents.stream()
                .sorted(Comparator.comparing(Event::getVehicle))
                .toList();
            log.put(tick, events);
            System.out.printf("Tick: %s - %s\n", tick, events);
            queuedEvents.clear();
            return events;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Map<Long, List<Event>> getLog() {
        return unmodifiableLog;
    }
}

package projekt.delivery.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LogCapturer implements EventListenerCapable<Event> {

    private final EventListener<Event> listener = this::handleEvent;
    private final List<Event> log = new ArrayList<>();
    private final List<Event> unmodifiableLog = Collections.unmodifiableList(log);

    private void handleEvent(Event event) {
        log.add(event);
    }

    @Override
    public Class<Event> getType() {
        return Event.class;
    }

    @Override
    public EventListener<? super Event> getListener() {
        return listener;
    }

    public List<Event> getLog() {
        return unmodifiableLog;
    }
}

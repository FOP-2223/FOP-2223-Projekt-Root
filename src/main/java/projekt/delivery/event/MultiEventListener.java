package projekt.delivery.event;

import java.util.List;

@FunctionalInterface
public interface MultiEventListener {

    void handle(List<Event> events);
}

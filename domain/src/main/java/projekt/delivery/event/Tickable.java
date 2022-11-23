package projekt.delivery.event;

import java.util.List;

public interface Tickable {

    List<Event> tick(long currentTick);
}

package projekt.delivery.simulation;

import projekt.delivery.event.Event;

import java.util.List;

public interface SimulationListener {

    void onTick(List<Event> events, long tick);
}

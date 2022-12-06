package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

public interface Event {

    static Event of(long tick, Vehicle vehicle) {
        return new VehicleEventImpl(tick, vehicle);
    }

    long getTick();

}

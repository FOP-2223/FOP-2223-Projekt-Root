package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface Event {

    LocalDateTime getTime();

    Vehicle getVehicle();

    static Event of(LocalDateTime time, Vehicle vehicle) {
        return new EventImpl(time, vehicle);
    }
}

package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface Event {

    static Event of(LocalDateTime time, Vehicle vehicle) {
        return new EventImpl(time, vehicle);
    }

    LocalDateTime getTime();

    Vehicle getVehicle();
}

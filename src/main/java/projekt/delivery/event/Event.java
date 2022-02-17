package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

public interface Event {

    LocalDateTime getTime();

    Vehicle getVehicle();
}

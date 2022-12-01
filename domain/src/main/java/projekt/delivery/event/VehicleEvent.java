package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

public interface VehicleEvent extends Event {

    Vehicle getVehicle();
}

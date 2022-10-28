package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

import java.time.LocalDateTime;

class EventImpl implements Event {

    private final LocalDateTime time;
    private final Vehicle vehicle;

    EventImpl(LocalDateTime time, Vehicle vehicle) {
        this.time = time;
        this.vehicle = vehicle;
    }

    @Override
    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public String toString() {
        return "Event("
            + "time=" + getTime()
            + ", vehicle=" + getVehicle().getId()
            + ')';
    }
}

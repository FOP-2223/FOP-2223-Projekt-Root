package projekt.delivery.event;

import projekt.delivery.routing.Vehicle;

class EventImpl implements Event {

    private final long tick;
    private final Vehicle vehicle;

    EventImpl(long tick, Vehicle vehicle) {
        this.tick = tick;
        this.vehicle = vehicle;
    }

    @Override
    public long getTick() {
        return tick;
    }

    @Override
    public Vehicle getVehicle() {
        return vehicle;
    }

    @Override
    public String toString() {
        return "Event("
            + "time=" + getTick()
            + ", vehicle=" + getVehicle().getId()
            + ')';
    }
}

package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

class LoadOrderEventImpl extends VehicleEventImpl implements LoadOrderEvent {

    private final ConfirmedOrder order;
    private final VehicleManager.OccupiedRestaurant restaurant;

    LoadOrderEventImpl(long tick, Vehicle vehicle, ConfirmedOrder order, VehicleManager.OccupiedRestaurant restaurant) {
        super(tick, vehicle);
        this.order = order;
        this.restaurant = restaurant;
    }

    public ConfirmedOrder getOrder() {
        return order;
    }

    @Override
    public VehicleManager.OccupiedRestaurant getRestaurant() {
        return restaurant;
    }

    @Override
    public String toString() {
        return "LoadOrderEvent("
            + "time=" + getTick()
            + ", vehicle=" + getVehicle()
            + ", order=" + getOrder()
            + ", warehouse=" + getRestaurant()
            + ')';
    }
}

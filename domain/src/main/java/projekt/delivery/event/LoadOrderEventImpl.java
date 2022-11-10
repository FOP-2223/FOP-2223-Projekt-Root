package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

class LoadOrderEventImpl extends EventImpl implements LoadOrderEvent {

    private final ConfirmedOrder order;
    private final VehicleManager.Warehouse warehouse;

    LoadOrderEventImpl(long tick, Vehicle vehicle, ConfirmedOrder order, VehicleManager.Warehouse warehouse) {
        super(tick, vehicle);
        this.order = order;
        this.warehouse = warehouse;
    }

    public ConfirmedOrder getOrder() {
        return order;
    }

    @Override
    public VehicleManager.Warehouse getWarehouse() {
        return warehouse;
    }

    @Override
    public String toString() {
        return "LoadOrderEvent("
            + "time=" + getTick()
            + ", vehicle=" + getVehicle()
            + ", order=" + getOrder()
            + ", warehouse=" + getWarehouse()
            + ')';
    }
}

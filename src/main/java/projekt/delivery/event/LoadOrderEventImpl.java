package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.time.LocalDateTime;

class LoadOrderEventImpl extends EventImpl implements LoadOrderEvent {

    private final ConfirmedOrder order;
    private final VehicleManager.Warehouse warehouse;

    LoadOrderEventImpl(LocalDateTime time, Vehicle vehicle, ConfirmedOrder order, VehicleManager.Warehouse warehouse) {
        super(time, vehicle);
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
}

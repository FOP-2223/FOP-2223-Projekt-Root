package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

public interface LoadOrderEvent extends Event {

    static LoadOrderEvent of(
        long tick,
        Vehicle vehicle,
        ConfirmedOrder order,
        VehicleManager.Warehouse warehouse
    ) {
        return new LoadOrderEventImpl(tick, vehicle, order, warehouse);
    }

    ConfirmedOrder getOrder();

    VehicleManager.Warehouse getWarehouse();
}

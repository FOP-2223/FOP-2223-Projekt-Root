package projekt.delivery.event;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.time.LocalDateTime;

public interface LoadOrderEvent extends Event {

    static LoadOrderEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        ConfirmedOrder order,
        VehicleManager.Warehouse warehouse
    ) {
        return new LoadOrderEventImpl(time, vehicle, order, warehouse);
    }

    ConfirmedOrder getOrder();

    VehicleManager.Warehouse getWarehouse();
}

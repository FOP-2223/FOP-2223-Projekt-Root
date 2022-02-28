package projekt.delivery.event;

import projekt.delivery.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.time.LocalDateTime;

public interface LoadOrderEvent extends Event {

    ConfirmedOrder getOrder();

    VehicleManager.Warehouse getWarehouse();

    static LoadOrderEvent of(
        LocalDateTime time,
        Vehicle vehicle,
        ConfirmedOrder order,
        VehicleManager.Warehouse warehouse
    ) {
        return new LoadOrderEventImpl(time, vehicle, order, warehouse);
    }
}

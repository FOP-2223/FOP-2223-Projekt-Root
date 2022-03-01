package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtWarehouseEvent;
import projekt.delivery.event.LoadOrderEvent;

class OccupiedWarehouseImpl extends OccupiedNodeImpl<Region.Node> implements VehicleManager.Warehouse {

    OccupiedWarehouseImpl(Region.Node component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public boolean loadOrder(Vehicle vehicle, ConfirmedOrder order) {
        if (vehicle.getOccupied() != this) {
            throw new IllegalArgumentException("The specified vehicle is not located on this node!");
        }

        if (((VehicleImpl) vehicle).loadOrder(order)) {
            vehicleManager.getEventBus().queuePost(LoadOrderEvent.of(
                    vehicleManager.getCurrentTime(),
                    vehicle,
                    order,
                    vehicleManager.getWarehouse()
                )
            );
            return true;
        }
        return false;
    }

    @Override
    protected void emitArrivedEvent(VehicleImpl vehicle, OccupiedEdgeImpl previousEdge) {
        vehicleManager.getEventBus().queuePost(ArrivedAtWarehouseEvent.of(
                vehicleManager.getCurrentTime(),
                vehicle,
                component,
                previousEdge.getComponent()
            )
        );
    }
}

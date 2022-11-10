package projekt.delivery.routing;

import projekt.delivery.event.ArrivedAtWarehouseEvent;
import projekt.delivery.event.LoadOrderEvent;

class OccupiedWarehouseImpl extends OccupiedNodeImpl<Region.Node> implements VehicleManager.Warehouse {

    OccupiedWarehouseImpl(Region.Node component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public void loadOrder(Vehicle vehicle, ConfirmedOrder order, long currentTick) {
        if (vehicle.getOccupied() != this) {
            throw new IllegalArgumentException("The specified vehicle is not located on this node!");
        }

        ((VehicleImpl) vehicle).loadOrder(order);
        vehicleManager.getEventBus().queuePost(LoadOrderEvent.of(
                currentTick,
                vehicle,
                order,
                vehicleManager.getWarehouse()
            )
        );
    }

    @Override
    protected void emitArrivedEvent(VehicleImpl vehicle, OccupiedEdgeImpl previousEdge, long currentTick) {
        vehicleManager.getEventBus().queuePost(ArrivedAtWarehouseEvent.of(
                currentTick,
                vehicle,
                component,
                previousEdge.getComponent()
            )
        );
    }
}

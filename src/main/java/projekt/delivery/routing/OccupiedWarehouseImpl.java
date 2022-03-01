package projekt.delivery.routing;

import projekt.delivery.ConfirmedOrder;

class OccupiedWarehouseImpl extends OccupiedNodeImpl<Region.Node> implements VehicleManager.Warehouse {

    OccupiedWarehouseImpl(Region.Node component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public void loadOrder(Vehicle vehicle, ConfirmedOrder order) {
        if (vehicle.getOccupied() != this) {
            throw new IllegalArgumentException("The specified vehicle is not located on this node!");
        }


    }
}

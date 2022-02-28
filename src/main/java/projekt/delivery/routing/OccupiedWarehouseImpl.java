package projekt.delivery.routing;

import projekt.delivery.ConfirmedOrder;

class OccupiedWarehouseImpl extends OccupiedNodeImpl implements VehicleManager.Warehouse {

    OccupiedWarehouseImpl(Region.Node component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public void loadOrder(Vehicle vehicle, ConfirmedOrder order) {
        // TODO: Julius?
    }
}

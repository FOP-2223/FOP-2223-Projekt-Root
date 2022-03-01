package projekt.delivery.routing;

import projekt.delivery.ConfirmedOrder;

class OccupiedNeighborhoodImpl extends OccupiedNodeImpl<Region.Neighborhood> implements VehicleManager.OccupiedNeighborhood {
    OccupiedNeighborhoodImpl(Region.Neighborhood component, VehicleManager vehicleManager) {
        super(component, vehicleManager);
    }

    @Override
    public void deliverOrder(Vehicle vehicle, ConfirmedOrder order) {
    }
}

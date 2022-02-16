package projekt.delivery.vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

class OccupiedImpl<C extends Region.Component<C>> implements VehicleManager.Occupied<C> {

    private final C component;
    private final VehicleManager vehicleManager;
    private final List<Vehicle> vehicles = new ArrayList<>();
    private final Collection<Vehicle> unmodifiableVehicles =
        Collections.unmodifiableCollection(vehicles);

    OccupiedImpl(C component, VehicleManager vehicleManager) {
        this.component = component;
        this.vehicleManager = vehicleManager;
    }

    @Override
    public C getComponent() {
        return component;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    @Override
    public Collection<Vehicle> getVehicles() {
        return unmodifiableVehicles;
    }

    void addVehicle(Vehicle vehicle) {

    }

    void removeVehicle(Vehicle vehicle) {

    }
}

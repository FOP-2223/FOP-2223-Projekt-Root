package projekt.delivery.routing;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

abstract class AbstractOccupied<C extends Region.Component<C>> implements VehicleManager.Occupied<C> {

    protected final C component;
    protected final VehicleManager vehicleManager;
    protected final Map<VehicleImpl, VehicleStats> vehicles = new HashMap<>();
    private final Collection<Vehicle> unmodifiableVehicles =
        Collections.unmodifiableCollection(vehicles.keySet());
    /**
     * Whether this node was modified in this tick.
     */
    boolean dirty;

    AbstractOccupied(C component, VehicleManager vehicleManager) {
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

    abstract void tick();

    abstract void addVehicle(VehicleImpl vehicle);

    void removeVehicle(Vehicle vehicle) {

    }

    protected static class VehicleStats {
        final LocalDateTime arrived;

        public VehicleStats(LocalDateTime arrived) {
            this.arrived = arrived;
        }
    }

    interface VehicleMutator {
        /**
         * @return True if
         */
        boolean removeFromLast();
    }
}

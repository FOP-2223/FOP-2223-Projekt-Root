package projekt.delivery;

import projekt.base.DistanceCalculator;

import java.util.Set;

public interface VehicleManager {

    Region getRegion();

    DistanceCalculator getDistanceCalculator();

    Set<? extends Vehicle> getVehicles();

    interface Occupied<C extends Region.Component<C>> extends Region.Component<C> {

        C getComponent();

        VehicleManager getRegionManager();

        Set<? extends Vehicle> getVehicles();
    }

    interface Factory {
        VehicleManager create(Region region, DistanceCalculator distanceCalculator);
    }
}

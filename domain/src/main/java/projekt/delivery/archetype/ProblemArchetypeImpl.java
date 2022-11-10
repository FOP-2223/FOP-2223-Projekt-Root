package projekt.delivery.archetype;

import projekt.delivery.DeliveryService;
import projekt.delivery.Simulation;
import projekt.delivery.SimulationConfig;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;

import java.util.Collection;
import java.util.List;

public class ProblemArchetypeImpl implements ProblemArchetype {
    private final Region region;
    private final VehicleManager vehicleManager;
    private final OrderGenerator orderGenerator;
    private final Rater rater;
    private final Runnable updateCallback;
    private final SimulationConfig simulationConfig = new SimulationConfig(1000);

    public ProblemArchetypeImpl(Region region, VehicleManager vehicleManager, OrderGenerator orderGenerator, Rater rater, Runnable updateCallback) {
        this.region = region;
        this.vehicleManager = vehicleManager;
        this.orderGenerator = orderGenerator;
        this.rater = rater;
        this.updateCallback = updateCallback;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    @Override
    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }

    @Override
    public void runSimulation(DeliveryService.Factory deliveryServiceFactory) {
        final var simulation = new Simulation() {
            private DeliveryService deliveryService;

            public void setDeliveryService(DeliveryService deliveryService) {
                this.deliveryService = deliveryService;
            }

            @Override
            public void onStateUpdated() {
                updateCallback.run();
                List<ConfirmedOrder> newOrders = orderGenerator.generateOrders(deliveryService.getCurrentTick());
                if (newOrders == null
                    && vehicleManager.getVehicles().stream().map(Vehicle::getOrders).allMatch(Collection::isEmpty)
                    // TODO: && warehouse does not have any pending orders
                ) {
                    deliveryService.endSimulation();
                } else {
                    deliveryService.deliver(newOrders);
                }
            }
        };
        DeliveryService deliveryService = deliveryServiceFactory.create(vehicleManager,
            rater,
            simulation,
            simulationConfig);
        simulation.setDeliveryService(deliveryService);
        deliveryService.runSimulation();
    }
}

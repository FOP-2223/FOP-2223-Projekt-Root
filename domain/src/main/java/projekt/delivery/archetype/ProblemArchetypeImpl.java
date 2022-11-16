package projekt.delivery.archetype;

import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.event.DeliverOrderEvent;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ProblemArchetypeImpl implements ProblemArchetype {

    private final BasicDeliverySimulation simulation;
    private List<List<ConfirmedOrder>> deliveredOrders = new ArrayList<>();
    public ProblemArchetypeImpl(DeliveryService deliveryService, OrderGenerator orderGenerator) {
        this(deliveryService, orderGenerator, new SimulationConfig(0));
    }

    public ProblemArchetypeImpl(DeliveryService deliveryService, OrderGenerator orderGenerator, SimulationConfig simulationConfig) {
        this.simulation = new BasicDeliverySimulation(simulationConfig, deliveryService);

        //add starting orders
        simulation.getDeliveryService().deliver(orderGenerator.generateOrders(0));

        //Listener to add new orders after each tick
        simulation.addListener(() -> {
            List<ConfirmedOrder> newOrders = orderGenerator.generateOrders(simulation.getCurrentTick());
            if (newOrders == null
                && simulation.getDeliveryService().getVehicleManager().getVehicles().stream().map(Vehicle::getOrders).allMatch(Collection::isEmpty)
                && simulation.getDeliveryService().getPendingOrders().size() == 0
            ) {
                simulation.endSimulation();
            } else {
                simulation.getDeliveryService().deliver(newOrders);
            }
        });

        //Listener to save the current state of the simulation after each tick
        simulation.addListener(() -> {
            if (deliveredOrders.size() == Integer.MAX_VALUE) {
                System.err.println("Maximum length of stored event timeline reached.");
                return;
            }
            deliveredOrders.add(simulation.getCurrentEvents().stream()
                .filter(DeliverOrderEvent.class::isInstance)
                .map(DeliverOrderEvent.class::cast)
                .map(DeliverOrderEvent::getOrder)
                .toList());
        });
    }

    @Override
    public void start() {
        new Thread(simulation::runSimulation).start();
    }

    @Override
    public void start(long maxTickCount) {
        simulation.addListener(() -> {
            if (simulation.getCurrentTick() == maxTickCount) simulation.endSimulation();
        });
        new Thread(simulation::runSimulation).start();
    }

    @Override
    public void end() {
        simulation.endSimulation();
    }

    @Override
    public List<List<ConfirmedOrder>> getAllOrders() {
        return Collections.unmodifiableList(deliveredOrders);
    }

    @Override
    public List<ConfirmedOrder> getOrdersForTick(long tick) {

        if (tick > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("requested tick too large");
        }
        if (tick < 0) {
            throw new IllegalArgumentException("negative tick requested");
        }

        return deliveredOrders.get((int) tick);
    }

}

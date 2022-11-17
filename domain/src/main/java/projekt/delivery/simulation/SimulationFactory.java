package projekt.delivery.simulation;

import projekt.delivery.archetype.DeterministicOrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.deliveryService.DeliveryService;
import projekt.delivery.rating.Rater;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.Vehicle;

import java.util.Collection;
import java.util.List;

public class SimulationFactory {

    public Simulation createSimulation(ProblemArchetype problemArchetype, Rater rater, SimulationConfig simulationConfig) {

        BasicDeliverySimulation simulation = new BasicDeliverySimulation(simulationConfig, rater, problemArchetype.createProblemSolverDeliverySolver());

        //add starting orders
        simulation.getDeliveryService().deliver(problemArchetype.getOrderGenerator().generateOrders(0));

        //Listener to add new orders after each tick
        simulation.addListener(() -> {
            List<ConfirmedOrder> newOrders = problemArchetype.getOrderGenerator().generateOrders(simulation.getCurrentTick());
            if (newOrders == null
                && simulation.getDeliveryService().getVehicleManager().getVehicles().stream().map(Vehicle::getOrders).allMatch(Collection::isEmpty)
                && simulation.getDeliveryService().getPendingOrders().size() == 0
            ) {
                simulation.endSimulation();
            } else {
                simulation.getDeliveryService().deliver(newOrders);
            }
        });

        return simulation;
    }
}
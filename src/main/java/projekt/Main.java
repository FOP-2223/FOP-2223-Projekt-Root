package projekt;

import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;
import projekt.delivery.routing.DijkstraPathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {

        // layer 1

        Region region = Region.builder()
            .addNeighborhood("nodeA", new Location(-2, -2), 1.5)
            .addNode("nodeB", new Location(-2, 2))
            .addNeighborhood("nodeC", new Location(2, 2), 0.5)
            .addNode("nodeD", new Location(2, -2))
            .addEdge("edge1", new Location(-2, -2), new Location(-2, 2), Duration.ofMinutes(1))
            .addEdge("edge2", new Location(-2, 2), new Location(-2, 2), Duration.ofMinutes(1))
            .addEdge("edge3", new Location(-2, -2), new Location(-2, 2), Duration.ofMinutes(1))
            .addEdge("edge4", new Location(-2, -2), new Location(-2, 2), Duration.ofMinutes(1))
            .build();



        // layer 2

        VehicleManager vehicleManager = VehicleManager.builder()
            .time(LocalDateTime.now())
            .region(region)
            .distanceCalculator(new EuclideanDistanceCalculator())
            .pathCalculator(new DijkstraPathCalculator())
            .warehouse(region.getNode(new Location(-2, -2)))
            .build();

        Region.Edge e = region.getEdge(new Location(2, 2), new Location(1, 3));
        VehicleManager.Occupied<Region.Edge> o = vehicleManager.getOccupied(e);

        // layer 3

        // DeliveryService deliveryService = DeliveryService.SIMPLE.create(vehicleManager, new LinearRater());

        // layer 4

        // Pizzeria pizzeria = Pizzeria.LOS_FOPBOTS_HERMANOS.create(deliveryService);

        // the lasagna is complete

        // new MainWindow(region, vehicleManager, deliverService, pizzeria) -> starts GUI thread

        // deliveryService.runSimulation(); // -> blocks the thread until the simulation is finished.
    }
}

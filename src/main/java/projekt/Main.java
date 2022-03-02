package projekt;

import com.formdev.flatlaf.FlatDarkLaf;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;
import projekt.delivery.DeliveryService;
import projekt.delivery.Simulation;
import projekt.delivery.SimulationConfig;
import projekt.delivery.rating.LinearRater;
import projekt.delivery.routing.DijkstraPathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.MainFrame;
import projekt.pizzeria.Pizzeria;

import javax.swing.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        // // layer 1

        Region region = Region.builder()
            .addNeighborhood("Wiesbaden", new Location(-9, -4), 0.75)
            .addNeighborhood("Mainz", new Location(-8, 0), 0.6)
            .addNeighborhood("Frankfurt", new Location(8, -8), 1)
            .addNeighborhood("Darmstadt", new Location(6, 8), 0.5)
            .addNeighborhood("Ruesselsheim", new Location(-2, 0), 0.35)
            .addNeighborhood("Gross-Gerau", new Location(0, 5), 0.2)
            .addNeighborhood("Langen", new Location(6, 0), 0.25)
            .addNeighborhood("Offenbach", new Location(10, -7), 0.35)
            .addNode("Mainspitzdreieck", new Location(-5, 0))
            .addNode("Wiesbadener Kreuz", new Location(-4, -5))
            .addNode("Moenchhof-Dreieck", new Location(1, -2))
            .addNode("Frankfurter Kreuz", new Location(4, -4))
            .addNode("Dreieck Mainz", new Location(-10, -1))
            .addEdge("A643", new Location(-10, -1), new Location(-9, -4), Duration.ofMinutes(2))
            .addEdge("A60", new Location(-10, -1), new Location(-8, 0), Duration.ofMinutes(1))
            .addEdge("A60_1", new Location(-5, 0), new Location(-8, 0), Duration.ofMinutes(1))
            .addEdge("A671", new Location(-5, 0), new Location(-9, -4), Duration.ofMinutes(3))
            .addEdge("A60_2", new Location(-5, 0), new Location(-2, 0), Duration.ofMinutes(1))
            .addEdge("A66", new Location(-4, -5), new Location(-9, -4), Duration.ofMinutes(3))
            .addEdge("A66_1", new Location(-4, -5), new Location(8, -8), Duration.ofMinutes(7))
            .addEdge("A3", new Location(-4, -5), new Location(1, -2), Duration.ofMinutes(2))
            .addEdge("A67", new Location(1, -2), new Location(-2, 0), Duration.ofMinutes(1))
            .addEdge("A3_1", new Location(1, -2), new Location(4, -4), Duration.ofMinutes(2))
            .addEdge("A5", new Location(4, -4), new Location(8, -8), Duration.ofMinutes(2))
            .addEdge("A3_2", new Location(4, -4), new Location(10, -7), Duration.ofMinutes(4))
            .addEdge("A5_1", new Location(4,- 4), new Location(6, 0), Duration.ofMinutes(2))
            .addEdge("A5_2", new Location(6, 0), new Location(6, 8), Duration.ofMinutes(1))
            .addEdge("A67_1", new Location(0, 5), new Location(6, 8), Duration.ofMinutes(3))
            .addEdge("A67_2", new Location(0, 5), new Location(-2, 0), Duration.ofMinutes(2))
            .build();

        // // layer 2

        VehicleManager vehicleManager = VehicleManager.builder()
            .time(LocalDateTime.now())
            .region(region)
            .distanceCalculator(new EuclideanDistanceCalculator())
            .pathCalculator(new DijkstraPathCalculator())
            .warehouse(region.getNode(new Location(0, 5)))
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .addVehicle(2, List.of())
            .build();

        // // layer 3

        final var simulation = new Simulation() {
            private MainFrame mainFrame;

            @Override
            public void onStateUpdated() {
                SwingUtilities.invokeLater(mainFrame::onModelUpdate);
            }

            public void setMainFrame(MainFrame mainFrame) {
                this.mainFrame = mainFrame;
            }
        };
        DeliveryService deliveryService = DeliveryService.BOGO.create(vehicleManager,
            new LinearRater(),
            simulation,
            new SimulationConfig(1000));

        // // layer 4

        Pizzeria pizzeria = Pizzeria.LOS_FOPBOTS_HERMANOS.create(deliveryService);

        // the lasagna is complete

        // Gui Setup
        FlatDarkLaf.setup();
        MainFrame mainFrame = new MainFrame(region, vehicleManager, deliveryService, pizzeria);
        simulation.setMainFrame(mainFrame);
        SwingUtilities.invokeLater(() -> {
//            new MainFrame(null, null, null, null).setVisible(true); // -> starts GUI thread
            mainFrame.setVisible(true); // -> starts GUI thread
        });

        deliveryService.runSimulation(); // -> blocks the thread until the simulation is finished.
    }
}

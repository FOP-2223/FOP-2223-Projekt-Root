package projekt;

import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;
import projekt.delivery.DeliveryService;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.MainFrame;
import projekt.pizzeria.Pizzeria;
import projekt.rating.LinearRater;

import java.time.Duration;

import javax.swing.SwingUtilities;

import com.formdev.flatlaf.FlatDarkLaf;

public class Main {

    public static void main(String[] args) {

        // // layer 1

        // Region region = Region.builder()
        //     .addNeighborhood("nodeA", new Location(-2, -2), 1.5)
        //     .addNode("nodeB", new Location(-2, 2))
        //     .addNeighborhood("nodeC", new Location(2, 2), 0.5)
        //     .addNode("nodeD", new Location(2, -2))
        //     .addEdge("edge1", new Location(-2, -2), new Location(-2, 2), Duration.ofMinutes(1))
        //     .addEdge("edge2", new Location(-2, 2), new Location(-2, 2), Duration.ofMinutes(1))
        //     .addEdge("edge3", new Location(-2, -2), new Location(-2, 2), Duration.ofMinutes(1))
        //     .addEdge("edge4", new Location(-2, -2), new Location(-2, 2), Duration.ofMinutes(1))
        //     .build();

        // // layer 2

        // VehicleManager vehicleManager = VehicleManager.SIMPLE.create(region, new EuclideanDistanceCalculator());

        // // layer 3

        // DeliveryService deliveryService = DeliveryService.SIMPLE.create(vehicleManager, new LinearRater());

        // // layer 4

        // Pizzeria pizzeria = Pizzeria.LOS_FOPBOTS_HERMANOS.create(deliveryService);

        // the lasagna is complete

        // Gui Setup
        FlatDarkLaf.setup();
        SwingUtilities.invokeLater(()->{
            new MainFrame(null, null, null, null).setVisible(true); // -> starts GUI thread
            // new MainFrame(region, vehicleManager, deliveryService, pizzeria); // -> starts GUI thread

        });

        // deliveryService.runSimulation(); // -> blocks the thread until the simulation is finished.
    }
}

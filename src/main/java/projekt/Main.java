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
            .addNeighborhood("Piloty", new Location(-12, -12), 1.5)
            .addNeighborhood("S1|01", new Location(-7, 10),0.5)
            .addNeighborhood("Altes Hauptgebaeude", new Location(-2, -7), 1)
            .addNeighborhood("Bibliothek", new Location(2, -2), 0.5)
            .addNeighborhood("Darmstadtium", new Location(-2, 14), 0.5)
            .addNeighborhood("Mensa", new Location(0, 4), 0.5)
            .addNeighborhood("Herrngarten", new Location(-18, -10), 0.5)
            .addNeighborhood("Maschinenhaus", new Location(2, -6), 0.5)
            .addNeighborhood("S2|08", new Location(-3, -10), 0.5)
            .addNeighborhood("Akaflieg", new Location(4, 0), 0.5)
            .addNeighborhood("S1|13", new Location(0, 9), 0.5)
            .addNeighborhood("Hochschulrechenzentrum", new Location(-2, 10), 0.5)
            .addNeighborhood("S1|14", new Location(3, 7), 0.5)
            .addNeighborhood("Bushaltestelle", new Location(16, 4), 0.5)
            .addNeighborhood("S1|15", new Location(8, 6), 0.5)
            .addNeighborhood("S2|06", new Location(-8, -14), 0.5)
            .addNeighborhood("Mathebau", new Location(8, -18), 0.5)
            .addNeighborhood("S2|20", new Location(4, -15), 0.5)
            .addNeighborhood("Studierendenhaus", new Location(-12, -16), 0.5)
            .addNeighborhood("Testzentrum", new Location(-1, -3), 0.5)

            .addNode("Kantplatz", new Location(2, -10))
            .addNode("Alexander-von-Humboldt-Platz", new Location(0, 0))
            .addNode("Marion-Gräfin-Doenhoff-Platz", new Location(-8, 14))
            .addNode("Karolinenplatz", new Location(-10, 10))
            .addNode("Kreuzung Magdalenenstrasse/Alexanderstrasse", new Location(10, 6))
            .addNode("Eingang Herrngarten", new Location(-6, -8))
            .addNode("Eingang S2|20", new Location(5, -13))
            .addNode("Ende Pankratiusstrasse", new Location(9, -16))
            .addNode("Mitte Hochschulstrasse", new Location(-2, -9))
            .addNode("Mitte Hochschulstrasse_1", new Location(-10, -13))
            .addNode("Mitte Magdalenenstrasse", new Location(7, 0))
            .addNode("Mitte Magdalenenstrasse_1", new Location(6, -2))
            .addNode("Mitte Magdalenenstrasse_2", new Location(4, -6))
            .addNode("Ende Herrngarten", new Location(-4, 0))
            .addNode("Anfang Durchgang Alexanderstrasse", new Location(5, 8))
            .addNode("Zebrastreifen Alexanderstrasse", new Location(-1, 11))

            .addEdge("Alexanderstrasse", new Location(-8, 14), new Location(-1, 11), Duration.ofMinutes(1))
            .addEdge("Alexanderstrasse_1", new Location(-1, 11), new Location(5, 8), Duration.ofMinutes(1))
            .addEdge("Alexanderstrasse_2", new Location(5, 8), new Location(10, 6), Duration.ofMinutes(1))
            .addEdge("Alexanderstrasse_3", new Location(10, 6), new Location(16, 4), Duration.ofMinutes(1))
            .addEdge("Hochschulstrasse", new Location(-6, -8), new Location(-2, -9), Duration.ofMinutes(1))
            .addEdge("Hochschulstrasse_1", new Location(-2, -9), new Location(2, -10), Duration.ofMinutes(1))
            .addEdge("Hochschulstrasse_2", new Location(-12, -16), new Location(-10, -13), Duration.ofMinutes(1))
            .addEdge("Hochschulstrasse_3", new Location(-10, -13), new Location(-6, -8), Duration.ofMinutes(1))
            .addEdge("Magdalenenstrasse", new Location(2, -10), new Location(4, -6), Duration.ofMinutes(9))
            .addEdge("Magdalenenstrasse_1", new Location(4, -6), new Location(6, -2), Duration.ofMinutes(9))
            .addEdge("Magdalenenstrasse_2", new Location(6, -2), new Location(7, 0), Duration.ofMinutes(9))
            .addEdge("Magdalenenstrasse_3", new Location(7, 0), new Location(10, 6), Duration.ofMinutes(9))
            .addEdge("Pankratiusstrasse", new Location(2, -10), new Location(5, -13), Duration.ofMinutes(1))
            .addEdge("Pankratiusstrasse_1", new Location(5, -13), new Location(9, -16), Duration.ofMinutes(1))
            .addEdge("Einfahrt S2|20", new Location(5, -13), new Location(4, -15), Duration.ofMinutes(1))
            .addEdge("Einfahrt Mathebau", new Location(9, -16), new Location(8, -18), Duration.ofMinutes(1))
            .addEdge("Einfahrt S2|08", new Location(-2, -9), new Location(-3, -10), Duration.ofMinutes(1))
            .addEdge("Einfahrt Altes Hauptgebaeude", new Location(-2, -9), new Location(-2, -7), Duration.ofMinutes(1))
            .addEdge("Einfahrt Maschinenhaus", new Location(4, -6), new Location(2, -6), Duration.ofMinutes(1))
            .addEdge("Einfahrt Herrngarten", new Location(-6, -8), new Location(-18, -10), Duration.ofMinutes(1))
            .addEdge("Durchgang Altes Hauptgebäude/Bibliothek", new Location(-2, -7), new Location(2, -2), Duration.ofMinutes(1))
            .addEdge("Einfahrt Bibliothek", new Location(0, 0), new Location(2, -2), Duration.ofMinutes(1))
            .addEdge("Einfahrt Akaflieg", new Location(0, 0), new Location(4, 0), Duration.ofMinutes(1))
            .addEdge("Hinterausgang Akaflieg", new Location(7, 0), new Location(4, 0), Duration.ofMinutes(1))
            .addEdge("Einfahrt Mensa", new Location(0, 0), new Location(0, 4), Duration.ofMinutes(1))
            .addEdge("Durchfahrt Alfred-Humboldt-Platz/Magdalenenstraße", new Location(0, 0), new Location(6, -2), Duration.ofMinutes(1))
            .addEdge("Durchfahrt Alfred-Humboldt-Platz/Herrngarten", new Location(0, 0), new Location(-4, 0), Duration.ofMinutes(1))
            .addEdge("Weg neben altem Hauptgebaeude", new Location(-6, -8), new Location(-4, 0), Duration.ofMinutes(1))
            .addEdge("Vordereingang Piloty", new Location(-10, -13), new Location(-12, -12), Duration.ofMinutes(1))
            .addEdge("Hintereingang Piloty", new Location(-18, -10), new Location(-12, -12), Duration.ofMinutes(1))
            .addEdge("Einfahrt S2|06", new Location(-10, -13), new Location(-8, -14), Duration.ofMinutes(1))
            .addEdge("Treppenabgang", new Location(0, 0), new Location(-1, -3), Duration.ofMinutes(1))
            .addEdge("Durchgang Testzentrum/Magdalenenstrasse", new Location(-1, -3), new Location(4, -6), Duration.ofMinutes(1))
            .addEdge("Altes Hauptgebaeude Hintereingang", new Location(-1, -3), new Location(-2, -7), Duration.ofMinutes(1))
            .addEdge("Einfahrt S1|01", new Location(-10, 10), new Location(-7, 10), Duration.ofMinutes(1))
            .addEdge("Durchgang Alfred-Humboldt-Platz/Karolinenplatz", new Location(0, 0), new Location(-10, 10), Duration.ofMinutes(1))
            .addEdge("Durchgang Marion-Gräfin-Doenhoff-Platz/Karolinenplatz", new Location(-8, 14), new Location(-10, 10), Duration.ofMinutes(1))
            .addEdge("Durchgang Alfred-Humboldt-Platz/Alexanderstrasse", new Location(0, 0), new Location(5, 8), Duration.ofMinutes(1))
            .addEdge("Einfahrt S1|14", new Location(5, 8), new Location(3, 7), Duration.ofMinutes(1))
            .addEdge("Einfahrt S1|15", new Location(10, 6), new Location(8, 6), Duration.ofMinutes(1))
            .addEdge("Einfahrt Hochschulrechenzentrum", new Location(-1, 11), new Location(-2, 10), Duration.ofMinutes(1))
            .addEdge("Einfahrt S1|13", new Location(-1, 11), new Location(0, 9), Duration.ofMinutes(1))
            .addEdge("Hintereingang Darmstadtium", new Location(-1, 11), new Location(-2, 14), Duration.ofMinutes(1))
            .addEdge("Vordereingang Darmstadtium", new Location(-8, 14), new Location(-2, 14), Duration.ofMinutes(1))
            .build();

        // // layer 2

        VehicleManager vehicleManager = VehicleManager.builder()
            .time(LocalDateTime.now())
            .region(region)
            .distanceCalculator(new EuclideanDistanceCalculator())
            .pathCalculator(new DijkstraPathCalculator())
            .warehouse(region.getNode(new Location(-2, 14)))
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

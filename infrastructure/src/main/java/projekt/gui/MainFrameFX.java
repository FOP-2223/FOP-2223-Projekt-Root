package projekt.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import projekt.base.EuclideanDistanceCalculator;
import projekt.base.Location;
import projekt.delivery.archetype.FridayOrderGenerator;
import projekt.delivery.archetype.OrderGenerator;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemArchetypeImpl;
import projekt.delivery.rating.*;
import projekt.delivery.routing.DijkstraPathCalculator;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.service.BasicDeliveryService;
import projekt.delivery.service.DeliveryService;
import projekt.delivery.simulation.BasicDeliverySimulation;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationConfig;

import java.util.HashMap;
import java.util.Map;

public class MainFrameFX extends Application {

    //private SimulationControls simControls;
    public MainFrame mainFrame;

    public MainFrameFX(Region region, VehicleManager vehicleManager, Simulation simulation) {
        this.mainFrame = new MainFrame(region, vehicleManager, simulation);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // todo: something with mainframe
        mainFrame.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

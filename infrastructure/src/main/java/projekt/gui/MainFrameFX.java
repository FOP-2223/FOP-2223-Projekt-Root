package projekt.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;

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

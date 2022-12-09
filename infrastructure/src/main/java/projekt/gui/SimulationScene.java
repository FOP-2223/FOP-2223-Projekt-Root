package projekt.gui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import projekt.delivery.event.Event;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationListener;

import java.util.List;

public class SimulationScene extends Scene implements SimulationListener, ControlledScene<SimulationSceneController> {


    private final SimulationSceneController controller;

    public SimulationScene() {
        super(new BorderPane());
        controller = new SimulationSceneController();
    }

    public void init(Simulation simulation) {
        simulation.addListener(this);
    }

    @Override
    public void onTick(List<Event> events, long tick) {

    }

    @Override
    public SimulationSceneController getController() {
        return controller;
    }
}

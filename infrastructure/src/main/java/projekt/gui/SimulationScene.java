package projekt.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import projekt.delivery.event.Event;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationListener;

import java.util.List;

public class SimulationScene extends Scene implements SimulationListener, ControlledScene<SimulationSceneController> {


    private final SimulationSceneController controller;
    public Region region;
    public VehiclePanel vehicleManager;
    public Vehicle selectedVehicle;
    private Pane root;
    public InfoPanel infoPanel;
    public MapPanel mapPanel;
    public ControlsPanel controlsPanel;
    public Simulation simulation;

    public SimulationScene() {
        super(new BorderPane());
        controller = new SimulationSceneController();
        root = (Pane) getRoot();
    }

    public void init(Simulation simulation) {

        this.simulation = simulation;
        this.simulation.addListener(this);

        infoPanel = new InfoPanel(this);
        mapPanel = new MapPanel(this);
        controlsPanel = new ControlsPanel(this, simulation.getSimulationConfig());

        final BorderPane bp = new BorderPane(new MyMenuBar(this));
        bp.setCenter(mapPanel);
        bp.setLeft(infoPanel);
        bp.setBottom(controlsPanel);
        //root = bp;
    }

    @Override
    public void onTick(List<Event> events, long tick) {

    }

    @Override
    public SimulationSceneController getController() {
        return controller;
    }
}

package projekt.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
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
    private BorderPane root;
    public InfoPanel infoPanel;
    public MapPanel mapPanel;
    public ControlsPanel controlsPanel;
    public Simulation simulation;

    public SimulationScene() {
        super(new BorderPane());
        controller = new SimulationSceneController();
        root = (BorderPane) getRoot();
        root.setPrefSize(500, 500);
        root.setTop(new Label("SIMULATION"));
    }

    public void init(Simulation simulation) {

        this.simulation = simulation;
        this.region = simulation.getDeliveryService().getVehicleManager().getRegion();


        infoPanel = new InfoPanel(this);
        vehicleManager = infoPanel.getDetailsPanel();
        mapPanel = new MapPanel(this);
        controlsPanel = new ControlsPanel(this, simulation.getSimulationConfig());


        final BorderPane bp = new BorderPane(new MyMenuBar(this));
        bp.setCenter(mapPanel);
        bp.setLeft(infoPanel);
        bp.setBottom(controlsPanel);
        bp.setPrefSize(500, 500);
        setRoot(bp);
        root = bp;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        //Execute GUI updates on the javafx application thread
        Platform.runLater(() -> {
            root.setTop(new Label("OnTick"));
        });
    }

    @Override
    public SimulationSceneController getController() {
        return controller;
    }
}

package projekt.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
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
    public Vehicle selectedVehicle;
    public Simulation simulation;
    private BorderPane root;
    public VehiclePanel vehicleManager;
    public InfoPanel infoPanel;
    public MapPanel mapPanel;
    public ControlsPanel controlsPanel;

    public SimulationScene() {
        super(new BorderPane());
        controller = new SimulationSceneController();
        root = (BorderPane) getRoot();
        root.setPrefSize(500, 500);
        //final VBox vBox = new VBox(new MyMenuBar(this));
        root.getChildren().addAll(new Label("SIMULATION"));
    }

    public void init(Simulation simulation) {

        this.simulation = simulation;
        region = simulation.getDeliveryService().getVehicleManager().getRegion();
        infoPanel = new InfoPanel(this);
        mapPanel = new MapPanel(this);
        controlsPanel = new ControlsPanel(this, simulation.getSimulationConfig());

        final BorderPane bp = new BorderPane(new MyMenuBar(this));

        final TitledPane map = new TitledPane("Map", mapPanel);
        map.setCollapsible(false);
        bp.setCenter(map);

        bp.setLeft(infoPanel);

        final TitledPane controls = new TitledPane("Controls", controlsPanel);
        controls.setCollapsible(false);
        bp.setBottom(controls);
        //bp.setPrefSize(500, 500);
        setRoot(bp);
        root = bp;
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        //Execute GUI updates on the javafx application thread
        Platform.runLater(() -> {
            //root.setTop(new Label("OnTick"));
        });
    }

    @Override
    public SimulationSceneController getController() {
        return controller;
    }
}

package projekt.gui.scene;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import projekt.delivery.event.Event;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationListener;
import projekt.gui.*;
import projekt.gui.controller.ControlledScene;
import projekt.gui.controller.SimulationSceneController;

import java.util.List;

public class SimulationScene extends Scene implements SimulationListener, ControlledScene<SimulationSceneController> {


    private final SimulationSceneController controller;
    public Region region;
    public Vehicle selectedVehicle;
    public Simulation simulation;
    private BorderPane root;
    public VehicleManager vehicleManager;
    public InfoPanel infoPanel;
    public MapPanel mapPanel;
    public ControlsPanel controlsPanel;

    public SimulationScene() {
        super(new BorderPane());
        controller = new SimulationSceneController();
        root = (BorderPane) getRoot();

        //final VBox vBox = new VBox(new MyMenuBar(this));
        //root.getChildren().addAll(new Label("SIMULATION"));
    }

    public void init(Simulation simulation) {

        this.simulation = simulation;
        simulation.toggleRunning();
        vehicleManager = simulation.getDeliveryService().getVehicleManager();
        region = vehicleManager.getRegion();

        infoPanel = new InfoPanel(this);
        mapPanel = new MapPanel(this);
        controlsPanel = new ControlsPanel(this, simulation.getSimulationConfig());

        var bp = new BorderPane(new MyMenuBar(this));

        var map = new TitledPane("Map", mapPanel);
        map.setCollapsible(false);
        bp.setCenter(map);

        bp.setLeft(infoPanel);

        var controls = new TitledPane("Controls", controlsPanel);
        controls.setCollapsible(false);
        bp.setBottom(controls);
        //bp.setPrefSize(500, 500);
        setRoot(bp);
        root = bp;
        //root.setPrefSize(500, 500);
        //root.getStylesheets().add("projekt/gui/menuStyle.css");
        root.getStylesheets().add("projekt/gui/darkMode.css");
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

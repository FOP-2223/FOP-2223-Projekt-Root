package projekt.gui.scene;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.BorderPane;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.event.ArrivedAtEdgeEvent;
import projekt.delivery.event.ArrivedAtNodeEvent;
import projekt.delivery.event.Event;
import projekt.delivery.event.SpawnEvent;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationListener;
import projekt.gui.controller.ControlledScene;
import projekt.gui.controller.SimulationSceneController;
import projekt.gui.pane.ControlsPane;
import projekt.gui.pane.MapPane;
import projekt.gui.pane.VehicleInfoPane;

import java.util.List;

public class SimulationScene extends Scene implements SimulationListener, ControlledScene<SimulationSceneController> {

    private final BorderPane root;
    private final SimulationSceneController controller;

    private MapPane mapPane;
    private VehicleInfoPane vehicleInfoPane;

    public SimulationScene() {
        super(new BorderPane());
        controller = new SimulationSceneController();
        root = (BorderPane) getRoot();

        root.setPrefSize(700, 700);
        root.getStylesheets().addAll("projekt/gui/darkMode.css", "projekt/gui/simulationStyle.css");
    }

    public void init(Simulation simulation, ProblemArchetype problem, int run, int simulationRuns) {
        VehicleManager vehicleManager = simulation.getDeliveryService().getVehicleManager();
        Region region = vehicleManager.getRegion();

        mapPane = new MapPane(region.getNodes(), region.getEdges(), vehicleManager.getVehicles());

        ControlsPane controlsPane = new ControlsPane(simulation, problem, run, simulationRuns);
        TitledPane titledControlsPane = new TitledPane("Controls", controlsPane);
        titledControlsPane.setCollapsible(false);

        root.setCenter(mapPane);
        root.setBottom(titledControlsPane);
        vehicleInfoPane = new VehicleInfoPane(simulation);
        root.setLeft(vehicleInfoPane);
    }

    @Override
    public void onTick(List<Event> events, long tick) {
        //Execute GUI updates on the javafx application thread
        Platform.runLater(() -> {
            events.stream()
                .filter(SpawnEvent.class::isInstance)
                .map(SpawnEvent.class::cast)
                .forEach(spawnEvent -> mapPane.addVehicle(spawnEvent.getVehicle()));

            events.stream()
                .filter(ArrivedAtNodeEvent.class::isInstance)
                .map(ArrivedAtNodeEvent.class::cast)
                .forEach(arrivedAtNodeEvent -> mapPane.redrawVehicle(arrivedAtNodeEvent.getVehicle()));

            events.stream()
                .filter(ArrivedAtEdgeEvent.class::isInstance)
                .map(ArrivedAtEdgeEvent.class::cast)
                .forEach(arrivedAtEdgeEvent -> mapPane.redrawVehicle(arrivedAtEdgeEvent.getVehicle()));

            vehicleInfoPane.refresh();
        });

    }

    @Override
    public SimulationSceneController getController() {
        return controller;
    }
}

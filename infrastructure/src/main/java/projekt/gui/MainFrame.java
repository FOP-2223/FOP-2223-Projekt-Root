package projekt.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import projekt.delivery.event.Event;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;
import projekt.delivery.simulation.SimulationListener;

import java.util.List;

public class MainFrame extends Stage implements SimulationListener {

    final Region region;
    final VehicleManager vehicleManager;
    final Simulation simulation;

    private ControlsPanel controlsPanel;
    private InfoPanel infoPanel;
    private MapPanel mapPanel;
    private Vehicle selectedVehicle;

    public MainFrame(Region region, VehicleManager vehicleManager, Simulation simulation) {
        this.region = region;
        this.vehicleManager = vehicleManager;
        this.simulation = simulation;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        setTitle("Mainframe");

        infoPanel = new InfoPanel(null);
        //mapPanel = new MapPanel();
        controlsPanel = new ControlsPanel(null, simulation.getSimulationConfig());

        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //setMinimumSize(new Dimension(500, 500));
        setMinWidth(500);
        setMinHeight(500);

        //setLayout(new BorderLayout(6, 6));
        final BorderPane bp = new BorderPane(new MyMenuBar(null));
        bp.setCenter(mapPanel);
        bp.setLeft(infoPanel);
        bp.setBottom(controlsPanel);
        final Scene scene = new Scene(bp);
        setScene(scene);
    }

    public MapPanel getMapPanel() {
        return mapPanel;
    }

    public InfoPanel getInfoPanel() {
        return infoPanel;
    }

    public ControlsPanel getControlsPanel() {
        return controlsPanel;
    }

    public Vehicle getSelectedVehicle() {
        return selectedVehicle;
    }

    public void setSelectedVehicle(@Nullable Vehicle vehicle) {
        this.selectedVehicle = vehicle;
        onUpdate();
    }

    public void onTick(List<Event> events, long tick) {
        infoPanel.onUpdate();
        onUpdate();
    }

    public void onUpdate() {
        infoPanel.setSelectedVehicle(selectedVehicle);
        //mapPanel.repaint();
    }

    public Region getRegion() {
        return region;
    }

    public VehicleManager getVehicleManager() {
        return vehicleManager;
    }

    public Simulation getSimulation() {
        return simulation;
    }
}

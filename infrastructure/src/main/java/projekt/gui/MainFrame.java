package projekt.gui;

import org.jetbrains.annotations.Nullable;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.routing.VehicleManager;
import projekt.delivery.simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame implements Simulation.Listener {

    final Region region;
    final VehicleManager vehicleManager;
    final Simulation simulation;

    private ControlsPanel controlsPanel;
    private InfoPanel infoPanel;
    private MapPanel mapPanel;
    private MenuBar menuBar;

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
        infoPanel = new InfoPanel(this);
        mapPanel = new MapPanel(this);
        controlsPanel = new ControlsPanel(this, simulation.getSimulationConfig());
        menuBar = new MenuBar(this);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));

        setLayout(new BorderLayout(6, 6));

        // Menu Bar
        setJMenuBar(menuBar);
        add(mapPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.EAST);
        add(controlsPanel, BorderLayout.SOUTH);
        pack();
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

    public void onStateUpdated() {
        infoPanel.onUpdate();
        onUpdate();
    }

    public void onUpdate() {
        infoPanel.setSelectedVehicle(selectedVehicle);
        mapPanel.repaint();
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

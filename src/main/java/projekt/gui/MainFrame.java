package projekt.gui;

import projekt.delivery.DeliveryService;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.pizzeria.Pizzeria;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class MainFrame extends JFrame {

    private Region region;
    private VehicleManager vehicleManager;
    private DeliveryService deliveryService;
    private Pizzeria pizzeria;

    private ControlsPanel controlsPanel;
    private InfoPanel infoPanel;
    private MapPanel mapPanel;
    private MenuBar menuBar;

    public MainFrame(Region region, VehicleManager vehicleManager, DeliveryService deliverService, Pizzeria pizzeria) {
        this.region = region;
        this.vehicleManager = vehicleManager;
        this.deliveryService = deliverService;
        this.pizzeria = pizzeria;

        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {
        infoPanel = new InfoPanel();
        mapPanel = new MapPanel(region, vehicleManager, deliveryService, pizzeria, this);
        controlsPanel = new ControlsPanel(deliveryService.getSimulationConfig());
        menuBar = new MenuBar(this);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(500, 500));

        setLayout(new BorderLayout(6, 6));

        // Menu Bar
        setJMenuBar(menuBar);

        // mapPanel.setBackground(Color.red);
        // infoPanel.setBackground(Color.green);
        // controlsPanel.setBackground(Color.blue);
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
}

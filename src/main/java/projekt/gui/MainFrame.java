package projekt.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import projekt.delivery.DeliveryService;
import projekt.delivery.routing.Region;
import projekt.delivery.routing.VehicleManager;
import projekt.pizzeria.Pizzeria;

public class MainFrame extends JFrame {

    private Region region;
    private VehicleManager vehicleManager;
    private DeliveryService deliveryService;
    private Pizzeria pizzeria;

    private javax.swing.JPanel controlsPanel;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JPanel mapPanel;
    private javax.swing.JMenuBar menuBar;

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
        mapPanel = new MapPanel();
        controlsPanel = new ControlsPanel();
        menuBar = new MenuBar();

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
}

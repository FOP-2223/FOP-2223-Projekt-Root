package projekt.gui;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import projekt.delivery.routing.Vehicle;

import java.awt.*;

public class InfoPanel extends GridPane {

    private final SimulationScene scene;
    private CurrentTimePanel currentTimePanel;
    private OrdersPanel ordersPanel;
    public TitledPane titledOrdersPane;

    public InfoPanel(SimulationScene scene) {
        this.scene = scene;

        initComponents();
    }

    public void initComponents() {
        currentTimePanel = new CurrentTimePanel(scene);
        scene.vehicleManager = new VehiclePanel(scene);
        ordersPanel = new OrdersPanel(scene);

        setPadding(new Insets(10, 0, 0, 0));
        //setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;

        add(currentTimePanel, 0, 0);
        //add(currentTimePanel, constraints);

        constraints.gridheight = 3;
        constraints.gridy = 1;
        constraints.weighty = 1; // height3, y1, wy 1

        add(new ScrollPane(scene.vehicleManager), 0, 1);

        constraints.gridy = 4;
        //add(ordersPanel, constraints);
        titledOrdersPane = new TitledPane("Orders:", ordersPanel);
        titledOrdersPane.setExpanded(true);
        add(titledOrdersPane, 0, 2);
    }

    public VehiclePanel getDetailsPanel() {
        return scene.vehicleManager;
    }

    public OrdersPanel getOrdersPanel() {
        return ordersPanel;
    }

    public CurrentTimePanel getCurrentTimePanel() {
        return currentTimePanel;
    }

    public void onUpdate() {
        scene.vehicleManager.onUpdate();
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        scene.vehicleManager.setSelectedVehicle(selectedVehicle);
    }
}

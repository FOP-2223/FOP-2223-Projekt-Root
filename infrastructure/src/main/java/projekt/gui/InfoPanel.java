package projekt.gui;

import javafx.scene.control.TitledPane;
import javafx.scene.layout.GridPane;
import projekt.delivery.routing.Vehicle;
import projekt.gui.scene.SimulationScene;

public class InfoPanel extends GridPane {

    private final SimulationScene scene;
    private OrdersPanel ordersPanel;
    public TitledPane titledOrdersPane;
    public VehiclePanel vehiclePanel;

    public InfoPanel(SimulationScene scene) {
        this.scene = scene;
        initComponents();
    }

    public void initComponents() {
        //currentTimePanel = new CurrentTimePanel(scene);
        vehiclePanel = new VehiclePanel(scene);
        ordersPanel = new OrdersPanel(scene);

        //add(currentTimePanel, 0, 0);
        // height3, y1, wy 1

        final TitledPane titledVehiclesPane = new TitledPane("Vehicles:", vehiclePanel);
        titledVehiclesPane.setExpanded(true);

        add(titledVehiclesPane, 0, 1);

        titledOrdersPane = new TitledPane("Orders:", ordersPanel);
        titledOrdersPane.setExpanded(true);
        add(titledOrdersPane, 0, 2);
    }

    public void onUpdate() {
        vehiclePanel.onUpdate();
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        vehiclePanel.setSelectedVehicle(selectedVehicle);
    }
}

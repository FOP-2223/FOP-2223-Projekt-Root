package projekt.gui;

import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import projekt.delivery.routing.Vehicle;

import java.awt.*;

public class InfoPanel extends GridPane {

    private final SimulationScene scene;

    private VehiclePanel detailsPanel;
    private CurrentTimePanel currentTimePanel;
    private OrdersPanel ordersPanel;

    public InfoPanel(SimulationScene scene) {
        this.scene = scene;

        initComponents();
    }

    public void initComponents() {
        detailsPanel = new VehiclePanel(scene);
        currentTimePanel = new CurrentTimePanel(scene);



        //detailsPanel = makeDetailsPane(mainFrame);
            //= new VehiclePanel(mainFrame);
        //ordersPanel = new OrdersPanel(mainFrame);

        //setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;

        //getChildren().add(currentTimePanel);
        //add(currentTimePanel, constraints);

        constraints.gridheight = 3;
        constraints.gridy = 1;
        constraints.weighty = 1;
        //getChildren().add(detailsPanel);
        //add(detailsPanel, constraints);

        constraints.gridy = 4;
        //add(ordersPanel, constraints);
        //getChildren().add(ordersPanel);
    }

    private BorderPane makeDetailsPane(MainFrame mainFrame) {
        var lst = new ListView<Vehicle>();
        // cell Renderer
        // @Override getVehicle()
        // component text = "Vehicle %d" + value.getId()
        // selection mode = single
        lst.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        var selectedVehicle = lst.getSelectionModel().getSelectedItem();
        // setModel new
        // vehicles = null
        // getSize = mainframe.vehicles.getSize
        // getElementAt(i) return vehicle i
        // addSelectionListener -> mainframe.setSelected = value

        // onUpdate updateComponentTreeUI
        // setSelected(vehicle) = lst.setSelected(vehicle)
        var sm = SelectionMode.SINGLE;
        final BorderPane borderPane = new BorderPane(lst);
        // Title
        return borderPane;
    }

    public VehiclePanel getDetailsPanel() {
        return detailsPanel;
    }

    public OrdersPanel getOrdersPanel() {
        return ordersPanel;
    }

    public CurrentTimePanel getCurrentTimePanel() {
        return currentTimePanel;
    }

    public void onUpdate() {
        detailsPanel.onUpdate();
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        detailsPanel.setSelectedVehicle(selectedVehicle);
    }
}

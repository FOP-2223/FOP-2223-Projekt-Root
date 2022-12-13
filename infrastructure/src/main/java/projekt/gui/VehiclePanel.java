package projekt.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import projekt.delivery.routing.Vehicle;

import java.util.List;

public class VehiclePanel extends BorderPane {

    private final SimulationScene scene;

    public ListView<Vehicle> selectionList;

    public VehiclePanel(SimulationScene scene) {
        this.scene = scene;
        initComponents();
    }

    /**
     * Used to set title of cells
     */
    static class RenderCell extends ListCell<Vehicle> {
        @Override
        public void updateItem(Vehicle item, boolean empty) {
            super.updateItem(item, empty);
            if (item == null) {
                setText("no Vehicle");
            } else {
                setText(String.format("Vehicle %d", item.getId()));
            }
        }
    }

    public void initComponents() {
        //setLayout(new GridLayout(0, 1));
        //setBorder(new Title = "Vehicle Selection");
        setTop(new Text("Vehicle Selection"));
        //setBorder(new TitledBorder("Vehicle Selection"));
        // getcars:
        var vehicleManager = scene.simulation.getDeliveryService().getVehicleManager();
        var vehicles = FXCollections.observableArrayList(vehicleManager.getVehicles());
        selectionList = new ListView<>(vehicles);
        setCenter(selectionList);
        selectionList.setCellFactory((ListView<Vehicle> l) -> new RenderCell());

        selectionList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, selected) -> scene.selectedVehicle = selected);

        // ------
        /*selectionList.setModel(new AbstractListModel<>() {

            Vehicle[] vehicles = null;

            @Override
            public int getSize() {
                return mainFrame.getVehicleManager().getVehicles().size();
            }

            @Override
            public Vehicle getElementAt(int index) {
                return (vehicles = Objects.requireNonNullElseGet(vehicles, () -> mainFrame.getVehicleManager().getVehicles().toArray(Vehicle[]::new)))[index];
            }
        });*/
    }

    public void onUpdate() {
        //SwingUtilities.updateComponentTreeUI(selectionList);
    }

    public void setSelectedVehicle(Vehicle vehicle) {
        selectionList.getSelectionModel().select(vehicle);
    }

    public List<Vehicle> getVehicles() {
        return selectionList.getItems();
    }
}

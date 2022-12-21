package projekt.gui;

import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import projekt.delivery.routing.Vehicle;
import projekt.gui.scene.SimulationScene;

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
        // getcars:
        var vehicleManager = scene.vehicleManager;
        var vehicles = FXCollections.observableArrayList(vehicleManager.getAllVehicles());
        selectionList = new ListView<>(vehicles);
        setCenter(new ScrollPane(selectionList));
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

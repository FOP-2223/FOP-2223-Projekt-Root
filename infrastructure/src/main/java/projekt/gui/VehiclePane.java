package projekt.gui;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.simulation.Simulation;

public class VehiclePane extends BorderPane {

    private final Simulation simulation;

    public ListView<Vehicle> selectionList;

    public VehiclePane(Simulation simulation) {
        this.simulation = simulation;
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


//        selectionList.getSelectionModel().selectedItemProperty().addListener(
//            (observable, oldValue, selected) -> scene.selectedVehicle = selected);

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
}

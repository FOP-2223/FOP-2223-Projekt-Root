package projekt.gui;

import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import projekt.delivery.routing.Vehicle;

public class VehiclePanel extends BorderPane {

    private final MainFrame mainFrame;

    private ListView<Vehicle> selectionList;

    public VehiclePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    public void initComponents() {
        //setLayout(new GridLayout(0, 1));
        //setBorder(new Title = "Vehicle Selection");

        //setBorder(new TitledBorder("Vehicle Selection"));
        selectionList = new ListView<>();
        setCenter(selectionList);
        // TODO: add to cell Renderer Component:
        // component.setText(String.format("Vehicle %d", value.getId()));

        /*
        selectionList.setCellRenderer(new ListCellRenderer<>() {

            final ListCellRenderer<? super Vehicle> original = selectionList.getCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList<? extends Vehicle> list, Vehicle value, int index, boolean isSelected, boolean cellHasFocus) {
                var component = (JLabel) original.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                component.setText(String.format("Vehicle %d", value.getId()));
                return component;
            }
        });*/

        selectionList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, selected) -> mainFrame.setSelectedVehicle(selected));

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
}

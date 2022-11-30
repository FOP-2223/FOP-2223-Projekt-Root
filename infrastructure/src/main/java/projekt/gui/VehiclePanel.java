package projekt.gui;

import javafx.scene.control.ListView;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import projekt.delivery.routing.Vehicle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Objects;

public class VehiclePanel extends GridPane {

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
        //var scrollPane = new ScrollPane(selectionList);
        getChildren().add(selectionList, BorderLayout.CENTER);
        selectionList.setCellRenderer(new ListCellRenderer<>() {

            final ListCellRenderer<? super Vehicle> original = selectionList.getCellRenderer();

            @Override
            public Component getListCellRendererComponent(JList<? extends Vehicle> list, Vehicle value, int index, boolean isSelected, boolean cellHasFocus) {
                var component = (JLabel) original.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                component.setText(String.format("Vehicle %d", value.getId()));
                return component;
            }
        });
        selectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionList.setModel(new AbstractListModel<>() {

            Vehicle[] vehicles = null;

            @Override
            public int getSize() {
                return mainFrame.getVehicleManager().getVehicles().size();
            }

            @Override
            public Vehicle getElementAt(int index) {
                return (vehicles = Objects.requireNonNullElseGet(vehicles, () -> mainFrame.getVehicleManager().getVehicles().toArray(Vehicle[]::new)))[index];
            }
        });
        selectionList.addListSelectionListener(l -> mainFrame.setSelectedVehicle(selectionList.getSelectedValue()));
    }

    public void onUpdate() {
        SwingUtilities.updateComponentTreeUI(selectionList);
    }

    public void setSelectedVehicle(Vehicle vehicle) {
        selectionList.setSelectedValue(vehicle, true);
    }
}

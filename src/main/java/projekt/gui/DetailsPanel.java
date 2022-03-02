package projekt.gui;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import projekt.delivery.routing.Vehicle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class DetailsPanel extends JPanel {

    private final MainFrame mainFrame;

    private JScrollPane scrollPane;
    private JComboBox<Vehicle> selectionArea;

    public DetailsPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    public void initComponents() {
        setLayout(new GridLayout(0, 1));
        setBorder(new TitledBorder("Location Details"));
        selectionArea = new JComboBox<>();

        add(selectionArea, BorderLayout.CENTER);
        // TODO: Basically everything lol
        // TODO: Replace with JTable, Add Data

        selectionArea.setRenderer(new ListCellRenderer<Vehicle>() {

            ListCellRenderer<? super Vehicle> original = selectionArea.getRenderer();

            @Override
            public Component getListCellRendererComponent(JList<? extends Vehicle> list, Vehicle value, int index, boolean isSelected, boolean cellHasFocus) {

                var component = (JLabel) original.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value != null) {
                    component.setText(String.format("Vehicle %d", value.getId()));
                } else {
                    component.setText("No Vehicle");
                }
                return component;
            }
        });

        selectionArea.addActionListener(l -> {
            mainFrame.setSelectedVehicle((Vehicle) selectionArea.getSelectedItem());
        });

    }

    public void setVehicles(Collection<Vehicle> vehicles) {
        if (selectionArea.getItemCount() > 0)
            // initialize selection are only once
            return;
        selectionArea.addItem(null);
        vehicles.forEach(selectionArea::addItem);

    }

    public void setSelectedVehicle(Vehicle vehicle) {
        selectionArea.setSelectedItem(vehicle);
    }

//    private static class VehicleEntry {
//
//        private static final VehicleEntry NULL = new VehicleEntry(null);
//
//        private final Vehicle vehicle;
//
//        private VehicleEntry(@Nullable Vehicle vehicle) {
//            this.vehicle = vehicle;
//        }
//
//
//
//        @Override
//        public String toString() {
//            if (vehicle == null)
//                return "No Vehicle";
//            return String.format("Vehicle %d", vehicle.getId());
//
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            return super.equals(obj) || Objects.equals(vehicle, obj);
//        }
//
//    }

}

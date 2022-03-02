package projekt.gui;

import projekt.delivery.routing.Vehicle;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class DetailsPanel extends JPanel {
    private JScrollPane scrollPane;
    private JComboBox<VehicleEntry> detailsArea;
    private List<VehicleEntry> vehicles;

    public DetailsPanel() {
        initComponents();
    }

    public void initComponents() {
        setLayout(new GridLayout(0, 1));
        setBorder(new TitledBorder("Location Details"));
        detailsArea = new JComboBox<>();
        detailsArea.setEnabled(false);
        add(detailsArea, BorderLayout.CENTER);
        // TODO: Basically everything lol
        // TODO: Replace with JTable, Add Data
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        detailsArea.removeAllItems();
        vehicles.stream().map(VehicleEntry::new).forEach(detailsArea::addItem);
        detailsArea.setEnabled(detailsArea.getItemCount() != 0);
    }

    public JComboBox getDetailsArea() {
        return detailsArea;
    }

    private static class VehicleEntry {

        private final Vehicle vehicle;

        private VehicleEntry(Vehicle vehicle) {
            this.vehicle = vehicle;
            vehicle.getOrders()
        }

        @Override
        public String toString() {

            return String.format("Vehicle %d", vehicle.getId());
        }
    }

}

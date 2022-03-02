package projekt.gui;

import projekt.delivery.routing.Vehicle;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Set;

public class InfoPanel extends JPanel {
    private DetailsPanel detailsPanel;
    private OrdersPanel ordersPanel;

    public InfoPanel() {
        initComponents();
    }

    public void initComponents() {
        detailsPanel = new DetailsPanel();
        ordersPanel = new OrdersPanel();
        setLayout(new GridLayout(2, 1, 6, 6));
        add(detailsPanel, BorderLayout.NORTH);
        add(ordersPanel, BorderLayout.SOUTH);
    }

    public DetailsPanel getDetailsPanel() {
        return detailsPanel;
    }

    public OrdersPanel getOrdersPanel() {
        return ordersPanel;
    }

    public void setVehicles(Set<Vehicle> vehicles) {
        detailsPanel.setVehicles(vehicles);
    }

}

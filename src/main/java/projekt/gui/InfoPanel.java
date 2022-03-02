package projekt.gui;

import projekt.delivery.routing.Vehicle;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Collection;
import java.util.Set;

public class InfoPanel extends JPanel {

    private final MainFrame mainFrame;

    private DetailsPanel detailsPanel;
    private OrdersPanel ordersPanel;

    public InfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    public void initComponents() {
        detailsPanel = new DetailsPanel(mainFrame);
        ordersPanel = new OrdersPanel(mainFrame);
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

    public void setVehicles(Collection<Vehicle> vehicles) {
        detailsPanel.setVehicles(vehicles);
    }

    public void setSelectedVehicle(Vehicle selectedVehicle) {
        detailsPanel.setSelectedVehicle(selectedVehicle);
    }
}

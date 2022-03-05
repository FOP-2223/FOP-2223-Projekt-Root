package projekt.gui;

import projekt.delivery.routing.Vehicle;

import javax.swing.JPanel;
import java.awt.*;

public class InfoPanel extends JPanel {

    private final MainFrame mainFrame;

    private VehiclePanel detailsPanel;
    private CurrentTimePanel currentTimePanel;
    private OrdersPanel ordersPanel;

    public InfoPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        initComponents();
    }

    public void initComponents() {
        currentTimePanel = new CurrentTimePanel(mainFrame);
        detailsPanel = new VehiclePanel(mainFrame);
        ordersPanel = new OrdersPanel(mainFrame);

        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 1;
        constraints.anchor = GridBagConstraints.PAGE_START;

        constraints.gridheight = 1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1;
        add(currentTimePanel, constraints);

        constraints.gridheight = 3;
        constraints.gridy = 1;
        constraints.weighty = 1;
        add(detailsPanel, constraints);

        constraints.gridy = 4;
        add(ordersPanel, constraints);
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

package projekt.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JPanel;

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
}

package projekt.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class OrdersPanel extends JPanel {
    private JScrollPane scrollPane;
    private JTextArea ordersArea;
    private OrdersControlPanel ordersControlPanel;

    public OrdersPanel() {
        initComponents();
    }

    public void initComponents() {
        ordersArea = new JTextArea(5, 20);
        scrollPane = new JScrollPane(ordersArea);
        ordersControlPanel = new OrdersControlPanel();

        setLayout(new BorderLayout(6,6));
        setBorder(new TitledBorder("Orders"));

        add(scrollPane, BorderLayout.CENTER);
        add(ordersControlPanel, BorderLayout.SOUTH);
    }
}

package projekt.gui;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;

public class OrdersPanel extends JPanel {
    private JScrollPane scrollPane;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> ordersArea;
    private OrdersControlPanel ordersControlPanel;

    public OrdersPanel() {
        initComponents();
    }

    public void initComponents() {
        ordersArea = new JList<>(listModel);
        scrollPane = new JScrollPane(ordersArea);
        ordersControlPanel = new OrdersControlPanel(this);

        setLayout(new BorderLayout(6, 6));
        setBorder(new TitledBorder("Orders"));

        add(scrollPane, BorderLayout.CENTER);
        add(ordersControlPanel, BorderLayout.SOUTH);
    }

    void removeSelected() {
        listModel.remove(ordersArea.getSelectedIndex());
    }

    void addOrder(String element) {
        listModel.addElement(element);
    }
}

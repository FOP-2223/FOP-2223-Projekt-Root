package projekt.gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

public class OrdersControlPanel extends JPanel {
    private JButton addOrderButton;
    private JButton editOrderButton;
    private JButton removeOrderButton;
    private OrdersPanel ordersPanel;
    private OrdersDialog ordersDialog;

    public OrdersControlPanel(OrdersPanel ordersPanel) {
        this.ordersPanel = ordersPanel;
        this.ordersDialog = new OrdersDialog();
        initComponents();
    }

    private void initComponents() {
        addOrderButton = new JButton("Add");
        editOrderButton = new JButton("Edit");
        removeOrderButton = new JButton("Remove");

        addOrderButton.addActionListener(actionEvent -> ordersDialog.showAddOrderDialog(ordersPanel));
        editOrderButton.addActionListener(actionEvent -> {});
        removeOrderButton.addActionListener(actionEvent -> ordersPanel.removeSelected());

        setLayout(new GridLayout(1, 3, 6, 6));

        add(addOrderButton);
        add(editOrderButton);
        add(removeOrderButton);
    }
}

package projekt.gui;

import javax.swing.*;
import java.awt.*;

public class OrdersControlPanel extends JPanel {
    final JButton addOrderButton;
    final JButton editOrderButton;
    final JButton removeOrderButton;
    private final MainFrame mainFrame;
    private final OrdersPanel ordersPanel;
    private final OrdersDialog ordersDialog;

    public OrdersControlPanel(MainFrame mainFrame, OrdersPanel ordersPanel) {
        this.mainFrame = mainFrame;
        this.ordersPanel = ordersPanel;
        this.ordersDialog = new OrdersDialog(mainFrame);

        addOrderButton = new JButton("Add");
        editOrderButton = new JButton("Edit");
        removeOrderButton = new JButton("Remove");
        initComponents();
    }

    private void initComponents() {
        addOrderButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().pause();
            ordersDialog.showAddOrderDialog();
        });
        editOrderButton.setEnabled(false);
        editOrderButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().pause();
            ordersDialog.showEditOrderDialog(ordersPanel.getSelectedOrder());
        });
        removeOrderButton.setEnabled(false);
        removeOrderButton.addActionListener(actionEvent -> ordersPanel.removeSelected());

        setLayout(new GridLayout(1, 3, 6, 6));

        add(addOrderButton);
        add(editOrderButton);
        add(removeOrderButton);
    }
}

package projekt.gui;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.GridLayout;

public class OrdersControlPanel extends JPanel {
    private JButton addOrderButton;
    private JButton editOrderButton;
    private JButton removeOrderButton;
    private final MainFrame mainFrame;
    private final OrdersPanel ordersPanel;
    private final OrdersDialog ordersDialog;

    public OrdersControlPanel(MainFrame mainFrame, OrdersPanel ordersPanel) {
        this.mainFrame = mainFrame;
        this.ordersPanel = ordersPanel;
        this.ordersDialog = new OrdersDialog(mainFrame, ordersPanel);
        initComponents();
    }

    private void initComponents() {
        addOrderButton = new JButton("Add");
        editOrderButton = new JButton("Edit");
        removeOrderButton = new JButton("Remove");

        addOrderButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().pause();
            ordersDialog.showAddOrderDialog();
        });
        editOrderButton.addActionListener(actionEvent -> {});
        removeOrderButton.addActionListener(actionEvent -> ordersPanel.removeSelected());

        setLayout(new GridLayout(1, 3, 6, 6));

        add(addOrderButton);
        add(editOrderButton);
        add(removeOrderButton);
    }
}

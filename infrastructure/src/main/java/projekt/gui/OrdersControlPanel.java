package projekt.gui;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

public class OrdersControlPanel extends GridPane {
    final Button addOrderButton;
    final Button editOrderButton;
    final Button removeOrderButton;
    private final MainFrame mainFrame;
    private final OrdersPanel ordersPanel;
    private final OrdersDialog ordersDialog;

    public OrdersControlPanel(MainFrame mainFrame, OrdersPanel ordersPanel) {
        this.mainFrame = mainFrame;
        this.ordersPanel = ordersPanel;
        this.ordersDialog = new OrdersDialog(mainFrame);

        addOrderButton = new Button("Add");
        editOrderButton = new Button("Edit");
        removeOrderButton = new Button("Remove");
        initComponents();
    }

    private void initComponents() {
        addOrderButton.setOnAction(event -> {
            mainFrame.getControlsPanel().pause();
            ordersDialog.showAddOrderDialog();
        });
        editOrderButton.setDisable(false);
        editOrderButton.setOnAction(event -> {
            mainFrame.getControlsPanel().pause();
            ordersDialog.showEditOrderDialog(ordersPanel.getSelectedOrder());
        });
        removeOrderButton.setDisable(true);
        removeOrderButton.setOnAction(event -> ordersPanel.removeSelected());

        //setLayout(new GridLayout(1, 3, 6, 6));
        // TODO: match gridlayout
        add(addOrderButton, 0, 0, 2, 2);
        add(editOrderButton, 0, 1, 2, 2);
        add(removeOrderButton, 0, 2, 2, 2);
    }
}

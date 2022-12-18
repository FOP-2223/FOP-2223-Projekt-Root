package projekt.gui;

import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import projekt.gui.scene.SimulationScene;

public class OrdersControlPanel extends HBox {
    final Button addOrderButton;
    final Button editOrderButton;
    final Button removeOrderButton;
    private final SimulationScene scene;
    private final OrdersPanel ordersPanel;
    private final OrdersDialog ordersDialog;

    public OrdersControlPanel(SimulationScene scene, OrdersPanel ordersPanel) {
        this.scene = scene;
        this.ordersPanel = ordersPanel;
        this.ordersDialog = new OrdersDialog(scene);

        addOrderButton = new Button("Add");
        editOrderButton = new Button("Edit");
        removeOrderButton = new Button("Remove");
        initComponents();
    }

    private void initComponents() {
        addOrderButton.setOnAction(event -> {
            scene.controlsPanel.pause();
            ordersDialog.showAddOrderDialog();
        });
        editOrderButton.setDisable(false);
        editOrderButton.setOnAction(event -> {
            scene.controlsPanel.pause();
            ordersDialog.showEditOrderDialog(ordersPanel.getSelectedOrder());
        });
        removeOrderButton.setDisable(true);
        removeOrderButton.setOnAction(event -> ordersPanel.removeSelected());

        getChildren().addAll(addOrderButton, editOrderButton, removeOrderButton);
    }
}

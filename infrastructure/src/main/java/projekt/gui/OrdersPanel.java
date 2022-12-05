package projekt.gui;

import javafx.beans.InvalidationListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Font;
import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

public class OrdersPanel extends TableView<ConfirmedOrder> {

    private final MainFrame mainFrame;
    private final OrdersControlPanel ordersControlPanel;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public OrdersPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.ordersControlPanel = new OrdersControlPanel(mainFrame, this);
        initComponents();
    }

    private void initComponents() {
        //setBorder(new TitledBorder("Orders"));
        final var label = new Label("Orders");
        label.setFont(new Font("Arial", 20));
        getChildren().add(label); // TODO: wrong place?
        setEditable(false);
        // Row Selection Event Handler:
        setOnMouseClicked(e -> {
            var selectedRow = getSelectionModel().getSelectedIndex();
            ordersControlPanel.editOrderButton.setDisable(selectedRow < 0);
            ordersControlPanel.removeOrderButton.setDisable(selectedRow < 0);
        });
        InvalidationListener listener;// = new WeakInvalidationListener(getSelectionModel());
        //getSelectionModel().selectedItemProperty().addListener(listener);

        var intColumn = new TableColumn<ConfirmedOrder, Integer>("Order ID");
        intColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        var dateColumn = new TableColumn<ConfirmedOrder, Date>("Delivery Time");
        dateColumn.setCellValueFactory(param -> {
            // TODO: set PropertyValueFactories to change deliveryTick to Date?
            var tick = param.getValue().getActualDeliveryTick();
            return (ObservableValue<Date>) new Date(tick);
        });
        getColumns().addAll(intColumn, dateColumn);

        //tableModel.addRow(new Object[]{0, "19:15 05.03.2022"});
        //var now = LocalDateTime.now().format(dateTimeFormatter);
        var tickInterval = new TickInterval(0, 0);
        getItems().add(new ConfirmedOrder(0, 0, null,  tickInterval, new ArrayList<>(), 0));

        var orders = mainFrame.vehicleManager.getVehicles().stream().flatMap(vehicle -> vehicle.getOrders().stream()).toList();
        getItems().addAll(orders);
        //add(ordersControlPanel, BorderLayout.SOUTH);
        getChildren().add(ordersControlPanel);
    }

    void removeSelected() {
        var selectedOrder = getSelectionModel().getSelectedItem();
        getItems().remove(selectedOrder);
        //table.updateUI();
    }

    ConfirmedOrder getSelectedOrder() {
        return getSelectionModel().getSelectedItem();
    }

    /*private class OrderTableModel extends DefaultTableModel {
        private OrderTableModel() {
            addColumn("Order ID");
            addColumn("Delivery time");

            addRow(new Object[]{0, LocalDateTime.now().format(dateTimeFormatter)});
            orders.values().forEach(order -> addRow(new Object[]{
                order.getOrderID(),
                order.getActualDeliveryTick()
            }));
        }
    }*/
}

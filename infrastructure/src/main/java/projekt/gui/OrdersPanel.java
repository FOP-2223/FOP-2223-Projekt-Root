package projekt.gui;

import javafx.beans.InvalidationListener;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OrdersPanel extends BorderPane {

    private final SimulationScene scene;
    private final OrdersControlPanel ordersControlPanel;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    private TableView<ConfirmedOrder> table;

    public OrdersPanel(SimulationScene scene) {
        this.scene = scene;
        this.ordersControlPanel = new OrdersControlPanel(scene, this);
        initComponents();
    }

    private void initComponents() {

        //setBorder(new TitledBorder("Orders"));

        table = new TableView<ConfirmedOrder>();
        table.setEditable(false);

        // Row Selection Event Handler:
        table.setOnMouseClicked(e -> {
            var selectedRow = table.getSelectionModel().getSelectedIndex();
            ordersControlPanel.editOrderButton.setDisable(selectedRow < 0);
            ordersControlPanel.removeOrderButton.setDisable(selectedRow < 0);
        });
        InvalidationListener listener;// = new WeakInvalidationListener(getSelectionModel());
        //getSelectionModel().selectedItemProperty().addListener(listener);

        var intColumn = new TableColumn<ConfirmedOrder, Integer>("Order ID");
        intColumn.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        var dateColumn = new TableColumn<ConfirmedOrder, Long>("Delivery Time");
        dateColumn.setCellFactory(new Callback<>() {
            @Override
            public TableCell<ConfirmedOrder, Long> call(TableColumn<ConfirmedOrder, Long> param) {
                return new TableCell<>() {
                    @Override
                    protected void updateItem(Long item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(item), ZoneId.systemDefault());
                            setText(dateTime.format(dateTimeFormatter));
                        }
                    }
                };
            }
        });

        /*
        // TODO: set PropertyValueFactories to change deliveryTick to Date?
        var tick = param.getValue().getActualDeliveryTick();
        return new SimpleObjectProperty<>(new Date(tick)); */
        table.getColumns().addAll(intColumn, dateColumn);

        //tableModel.addRow(new Object[]{0, "19:15 05.03.2022"});
        //var now = LocalDateTime.now().format(dateTimeFormatter);
        var tickInterval = new TickInterval(0, 5);
        table.getItems().add(new ConfirmedOrder(0, 0, null,  tickInterval, new ArrayList<>(), 0));

        var orders = scene.vehicleManager.getVehicles().stream()
            .flatMap(vehicle -> vehicle.getOrders().stream()).toList();
        table.getItems().addAll(orders);
        table.getSelectionModel().select(0);
        setCenter(table);

        setBottom(ordersControlPanel);
    }

    void removeSelected() {
        var selectedOrder = table.getSelectionModel().getSelectedItem();
        table.getItems().remove(selectedOrder);
        table.getSelectionModel().select(0);
        //table.updateUI();
    }

    ConfirmedOrder getSelectedOrder() {
        return table.getSelectionModel().getSelectedItem();
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

package projekt.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import projekt.delivery.routing.ConfirmedOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

public class OrdersPanel extends TableView<ConfirmedOrder> {

    private final MainFrame mainFrame;
    private final OrdersControlPanel ordersControlPanel;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    //private OrderTableModel tableModel;
    //private Table table;

    public OrdersPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.ordersControlPanel = new OrdersControlPanel(mainFrame, this);
        initComponents();
    }

    private void initComponents() {
        final var label = new Label("Orders");
        label.setFont(new Font("Arial", 20));
        getChildren().add(label); // TODO: wrong place?
        //setEditable(true);
        var col1 = new TableColumn<ConfirmedOrder, ConfirmedOrder>("title1");
        var col2 = new TableColumn<ConfirmedOrder, ConfirmedOrder>("title2");
        getColumns().addAll(col1, col2);

        tableModel = new OrderTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        setLayout(new BorderLayout(6, 6));
        setBorder(new TitledBorder("Orders"));

        tableModel.addRow(new Object[]{0, "19:15 05.03.2022"});
        table.setModel(new OrderTableModel());
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(e -> {
            ordersControlPanel.editOrderButton.setDisable(getSelectedRow() < 0);
            ordersControlPanel.removeOrderButton.setDisable(table.getSelectedRow() < 0);
        });


        add(scrollPane, BorderLayout.CENTER);

        add(ordersControlPanel, BorderLayout.SOUTH);
    }

    void removeSelected() {
        // TODO: actually remove order
        tableModel.removeRow(table.getSelectedRow());
        table.updateUI();
    }

    ConfirmedOrder getSelectedOrder() {
        return tableModel.orders.get((Integer) tableModel.getValueAt(table.getSelectedRow(), 0));
    }

    private class OrderTableModel extends DefaultTableModel {

        private final Map<Integer, ConfirmedOrder> orders = mainFrame.vehicleManager
            .getVehicles()
            .stream()
            .flatMap(vehicle -> vehicle.getOrders().stream())
            .collect(Collectors.toUnmodifiableMap(ConfirmedOrder::getOrderID, order -> order));

        private OrderTableModel() {
            addColumn("Order ID");
            addColumn("Delivery time");

            addRow(new Object[]{0, LocalDateTime.now().format(dateTimeFormatter)});
            orders.values().forEach(order -> addRow(new Object[]{
                order.getOrderID(),
                order.getActualDeliveryTick()
            }));
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    }

    private class OrderItem {

    }
}

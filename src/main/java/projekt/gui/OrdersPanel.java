package projekt.gui;

import projekt.delivery.routing.ConfirmedOrder;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrdersPanel extends JPanel {

    private final MainFrame mainFrame;

    private JScrollPane scrollPane;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> ordersArea;
    private OrdersControlPanel ordersControlPanel;


    private JTable table;

    public OrdersPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initComponents();
    }

    public void initComponents() {
        table = new JTable();
        scrollPane = new JScrollPane(table);
        ordersControlPanel = new OrdersControlPanel(this);

        setLayout(new BorderLayout(6, 6));
        setBorder(new TitledBorder("Orders"));

        add(scrollPane, BorderLayout.CENTER);

        ConfirmedOrder o;
        TableColumn[] columns = new TableColumn[2];
        columns[0] = new TableColumn(0);
        columns[0].setHeaderValue("Order ID");
        columns[1] = new TableColumn(1);
        columns[1].setHeaderValue("Delivery Time");
        Arrays.stream(columns).forEach(table::addColumn);
        table.setModel(new OrderTableModel());
        add(ordersControlPanel, BorderLayout.SOUTH);
    }

    void removeSelected() {
        listModel.remove(ordersArea.getSelectedIndex());
    }

    private class OrderTableModel extends AbstractTableModel {

        @Override
        public int getRowCount() {
            if (mainFrame.getSelectedVehicle() == null)
                return 0;
            return mainFrame.getSelectedVehicle().getOrders().size();
        }

        @Override
        public int getColumnCount() {
            return 2;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            var order = mainFrame.getSelectedVehicle().getOrders().stream().sorted(Comparator.comparing(ConfirmedOrder::getOrderID)).toList().get(rowIndex);
            return switch (columnIndex) {
                case 0 -> order.getOrderID();
                case 1 -> order.getActualDeliveryTime();
                default -> null;
            };
        }
    }


}

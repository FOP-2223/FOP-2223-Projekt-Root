package projekt.gui;

import projekt.delivery.routing.ConfirmedOrder;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

class OrdersDialog extends JDialog {

    private JLabel textField1Label = new JLabel("  Location:", JLabel.LEFT);
    private JLabel textField2Label = new JLabel("  Delivery time:", JLabel.LEFT);
    private JLabel textField3Label = new JLabel("  Food type:", JLabel.LEFT);
    private JLabel textField4Label = new JLabel("  Extras:", JLabel.LEFT);
    private JTextField textField1 = new JTextField();
    private JSpinner deliveryTimeSelector;
    private JComboBox<String> foodTypeSelector = new JComboBox<>(List.of("Pizza", "Pasta", "Ice cream").toArray(String[]::new));
    private JSpinner extrasSpinner = new JSpinner();
    private JButton okButton = new JButton("Ok");
    private JButton cancelButton = new JButton("Cancel");

    private final MainFrame mainFrame;
    private final OrdersPanel ordersPanel;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    OrdersDialog(MainFrame mainFrame, OrdersPanel ordersPanel) {
        this.mainFrame = mainFrame;
        this.ordersPanel = ordersPanel;

        deliveryTimeSelector = new JSpinner(new SpinnerModel() {
            private LocalDateTime savedLocalDateTime = LocalDateTime.now();

            @Override
            public Object getValue() {
                return savedLocalDateTime.format(dateTimeFormatter);
            }

            @Override
            public void setValue(Object o) {
                savedLocalDateTime = LocalDateTime.parse((String) o, dateTimeFormatter);
            }

            @Override
            public Object getNextValue() {
                savedLocalDateTime = savedLocalDateTime.plusMinutes(1);
                return getValue();
            }

            @Override
            public Object getPreviousValue() {
                savedLocalDateTime = savedLocalDateTime.minusMinutes(1);
                return getValue();
            }

            @Override
            public void addChangeListener(ChangeListener changeListener) {

            }

            @Override
            public void removeChangeListener(ChangeListener changeListener) {

            }
        });

        setMinimumSize(new Dimension(600, 300));
        setModal(true);
    }

    void showAddOrderDialog() {
        setTitle("Add order");
        setLayout(new GridLayout(5, 2, 6, 6));

        textField1.setText("(0, 0)");
        deliveryTimeSelector.setValue(mainFrame.vehicleManager.getCurrentTime().format(dateTimeFormatter));

        for (ActionListener listener : okButton.getActionListeners()) {
            okButton.removeActionListener(listener);
        }
        okButton.addActionListener(actionEvent -> {
            Integer[] coordinates = Arrays.stream(textField1.getText().replaceAll("\\(?\\)?", "").split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
            ConfirmedOrder order = mainFrame.pizzeria.submitOrder(
                coordinates[0],
                coordinates[1],
                Instant.from(LocalDateTime.parse(((String) deliveryTimeSelector.getValue()).substring(0, 16), dateTimeFormatter)),
                List.of()
            );
//            ordersPanel.addOrder(textField1.getText());
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });
        for (ActionListener listener : cancelButton.getActionListeners()) {
            cancelButton.removeActionListener(listener);
        }
        cancelButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });

        add(textField1Label);
        add(textField1);
        add(textField2Label);
        add(deliveryTimeSelector);
        add(textField3Label);
        add(foodTypeSelector);
        add(textField4Label);
        add(extrasSpinner);
        add(okButton);
        add(cancelButton);

        pack();
        setModal(true);
        setVisible(true);
    }

    void showEditOrderDialog(ConfirmedOrder order) {
        setTitle("Edit order");
        setLayout(new GridLayout(4, 2, 6, 6));

        textField1.setText(order.getLocation().toString());
//        deliveryTimeSelector.setText(order.getTimeInterval().getStart().format(dateTimeFormatter));

        okButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });
        cancelButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });

        add(textField1Label);
        add(textField1);
        add(textField2Label);
        add(deliveryTimeSelector);
        add(okButton);
        add(cancelButton);

        pack();
        setModal(true);
        setVisible(true);
    }
}

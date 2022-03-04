package projekt.gui;

import projekt.delivery.routing.ConfirmedOrder;
import projekt.food.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OrdersDialog extends JDialog {

    private final JLabel textField1Label = new JLabel("  Location:", JLabel.LEFT);
    private final JLabel textField2Label = new JLabel("  Delivery time:", JLabel.LEFT);
    private final JLabel textField3Label = new JLabel("  Food type:", JLabel.LEFT);
    private final JLabel textField4Label = new JLabel("  Food variant:", JLabel.LEFT);
    private final JLabel textField5Label = new JLabel("  Extras:", JLabel.LEFT);
    private final JTextField textField1 = new JTextField();
    private final JSpinner deliveryTimeSelector;
    private final JComboBox<String> foodTypeSelector = new JComboBox<>(FoodTypes.ALL.keySet().toArray(String[]::new));
    private final DefaultComboBoxModel<String> foodVariantSelectorModel = new DefaultComboBoxModel<>();
    private final JComboBox<String> foodVariantSelector = new JComboBox<>(foodVariantSelectorModel);
    private final JPanel extrasPanel = new JPanel();
    private final JScrollPane extrasPane = new JScrollPane(extrasPanel);
    private final JButton addFoodButton = new JButton("Add food");
    private final DefaultListModel<Food> foodListModel = new DefaultListModel<>();
    private final JList<Food> currentOrderList = new JList<>(foodListModel);
    private final JButton removeButton = new JButton("Remove");
    private final JButton okButton = new JButton("Ok");
    private final JButton cancelButton = new JButton("Cancel");

    private final MainFrame mainFrame;
    private final OrdersPanel ordersPanel;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    private final Map<String, List<Pair<JCheckBox, Extra<?>>>> extraCheckboxes = FoodTypes.ALL
        .entrySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey,
            entry -> entry
                .getValue()
                .getCompatibleExtras()
                .stream()
                .map(extra -> new Pair<JCheckBox, Extra<?>>(new JCheckBox(extra.getName()), extra))
                .toList()
        ));
    private final Map<String, List<String>> foodVariants = FoodTypes.ALL
        .entrySet()
        .stream()
        .collect(Collectors.toUnmodifiableMap(
            Map.Entry::getKey,
            entry -> entry.getValue().getFoodVariants().stream().map(Food.Variant::getName).toList()
        ));

    OrdersDialog(MainFrame mainFrame, OrdersPanel ordersPanel) {
        this.mainFrame = mainFrame;
        this.ordersPanel = ordersPanel;

        setMinimumSize(new Dimension(600, 300));
        setResizable(false);

        deliveryTimeSelector = new JSpinner(new SpinnerModel() {
            private List<ChangeListener> changeListeners = new ArrayList<>();
            private LocalDateTime savedLocalDateTime = LocalDateTime.now();

            @Override
            public Object getValue() {
                return savedLocalDateTime.format(dateTimeFormatter);
            }

            @Override
            public void setValue(Object o) {
                savedLocalDateTime = LocalDateTime.parse((String) o, dateTimeFormatter);
                changeListeners.forEach(changeListener -> changeListener.stateChanged(new ChangeEvent(this)));
            }

            @Override
            public Object getNextValue() {
                return savedLocalDateTime.plusMinutes(1).format(dateTimeFormatter);
            }

            @Override
            public Object getPreviousValue() {
                return savedLocalDateTime.minusMinutes(1).format(dateTimeFormatter);
            }

            @Override
            public void addChangeListener(ChangeListener changeListener) {
                changeListeners.add(changeListener);
            }

            @Override
            public void removeChangeListener(ChangeListener changeListener) {
                changeListeners.remove(changeListener);
            }
        });

        extrasPane.getVerticalScrollBar().setUnitIncrement(10);
        foodTypeSelector.addActionListener(actionEvent -> {
            String selectedFoodType = (String) foodTypeSelector.getSelectedItem();

            foodVariantSelectorModel.removeAllElements();
            foodVariants.get(selectedFoodType).forEach(foodVariantSelectorModel::addElement);
            foodVariantSelector.updateUI();

            extrasPanel.removeAll();
            List<Pair<JCheckBox, Extra<?>>> extraList = extraCheckboxes.get(selectedFoodType);
            extrasPanel.setLayout(new GridLayout(extraList.size(), 1, 6, 6));
            extraList.forEach(pair -> {
                JCheckBox checkBox = pair.getFirst();
                checkBox.setSelected(false);
                extrasPanel.add(checkBox);
            });
            extrasPanel.updateUI();
        });
        addFoodButton.addActionListener(actionEvent -> {
            String selectedFoodType = (String) foodTypeSelector.getSelectedItem();

            List<Extra<?>> selectedExtras = Arrays
                .stream(extrasPanel.getComponents())
                .map(component ->
                    component instanceof JCheckBox checkBox && checkBox.isSelected()
                        ? extraCheckboxes
                            .get(selectedFoodType)
                            .stream()
                            .filter(pair -> checkBox == pair.getFirst())
                            .findAny()
                            .orElse(new Pair<>(null, null))
                            .getSecond()
                        : null
                )
                .filter(Objects::nonNull)
                .collect(Collectors.toUnmodifiableList());
            Food food = FoodTypes.ALL
                .get(selectedFoodType)
                .getFoodVariants()
                .stream()
                .filter(variant -> variant.getName().equals(foodVariantSelector.getSelectedItem()))
                .findAny()
                .orElseThrow()
                .create((List) selectedExtras);
            foodListModel.addElement(food);
            resetFields();
        });
        okButton.addActionListener(actionEvent -> {
            Integer[] coordinates = Arrays.stream(textField1.getText().replaceAll("\\(?\\)?", "").split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);
            ConfirmedOrder order = mainFrame.pizzeria.submitOrder(
                coordinates[0],
                coordinates[1],
                LocalDateTime.parse(((String) deliveryTimeSelector.getValue()).substring(0, 16), dateTimeFormatter).toInstant(ZoneOffset.UTC),
                IntStream.range(0, foodListModel.getSize()).mapToObj(foodListModel::getElementAt).toList()
            );
//            ordersPanel.addOrder(textField1.getText());
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });
        cancelButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });

        setModal(true);
    }

    void showAddOrderDialog() {
        setTitle("Add order");
        setLayout(new GridLayout(8, 2, 6, 6));

        textField1.setText("(0, 0)");
        deliveryTimeSelector.setValue(mainFrame.vehicleManager.getCurrentTime().format(dateTimeFormatter));

        add(textField1Label);
        add(textField1);
        add(textField2Label);
        add(deliveryTimeSelector);
        add(textField3Label);
        add(foodTypeSelector);
        add(textField4Label);
        add(foodVariantSelector);
        add(textField5Label);
        add(extrasPane);
        add(new JLabel());
        add(addFoodButton);
        add(currentOrderList);
        add(removeButton);
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

    private void resetFields() {
        textField1.setText("(0, 0)");
        deliveryTimeSelector.setValue(mainFrame.vehicleManager.getCurrentTime().format(dateTimeFormatter));
        foodTypeSelector.setSelectedIndex(0);
    }

    private static class Pair<A, B> {

        private final A a;
        private final B b;

        private Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

        public A getFirst() {
            return a;
        }

        public B getSecond() {
            return b;
        }
    }
}

package projekt.gui;

import projekt.delivery.routing.ConfirmedOrder;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class OrdersDialog extends JDialog {

    private final JTextField textField1 = new JTextField();
    private final JSpinner deliveryTimeSelector;
    private final JTextField foodTextField = new JTextField();
    private final JButton addFoodButton = new JButton("Add food");
    private final DefaultListModel<String> foodListModel = new DefaultListModel<>();
    private final JList<String> foodList = new JList<>(foodListModel);
    private final JScrollPane foodListPane = new JScrollPane(foodList);
    private final JButton removeFoodButton = new JButton("Remove Food");
    private final JButton okButton = new JButton("Submit order");
    private final JButton cancelButton = new JButton("Cancel");

    private final MainFrame mainFrame;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    /*
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
*/
    OrdersDialog(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        setMinimumSize(new Dimension(600, 500));
        setMaximumSize(new Dimension(600, 600)); // ignored for whatever reason... ask your favorite higher being as to why

        //TODO in LocalDateTime umrechnen?
        deliveryTimeSelector = new JSpinner(new SpinnerNumberModel(
            mainFrame.getSimulation().getCurrentTick(), mainFrame.getSimulation().getCurrentTick(),Long.MAX_VALUE,1));

        foodList.addListSelectionListener(listSelectionEvent -> removeFoodButton.setEnabled(foodList.getSelectedIndex() >= 0));
        addFoodButton.addActionListener(actionEvent -> {
            String food = foodTextField.getText();
            foodListModel.addElement(food);
            resetFields();
        });
        removeFoodButton.setEnabled(false);
        removeFoodButton.addActionListener(actionEvent -> foodListModel.remove(foodList.getSelectedIndex()));
        okButton.addActionListener(actionEvent -> {
            Integer[] coordinates = Arrays.stream(textField1.getText().replaceAll("\\(?\\)?", "").split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toArray(Integer[]::new);

            /* TODO Wie orders submitten ohne pizzeria?
            ConfirmedOrder order = mainFrame.pizzeria.submitOrder(
                coordinates[0],
                coordinates[1],
                LocalDateTime.parse(((String) deliveryTimeSelector.getValue()).substring(0, 16), dateTimeFormatter).toInstant(ZoneOffset.UTC),
                IntStream.range(0, foodListModel.getSize()).mapToObj(foodListModel::getElementAt).toList()
            );*/
//            ordersPanel.addOrder(textField1.getText());
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });
        cancelButton.addActionListener(actionEvent -> {
            mainFrame.getControlsPanel().unpause();
            setVisible(false);
        });

        // TODO: why are the components jumping around depending on the number of entries in extrasPane...
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        constraints.weighty = 1;
        constraints.insets = new Insets(6, 6, 6, 6);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        add(new JLabel("Location:", JLabel.LEFT), constraints);

        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.weightx = 1;
        add(textField1, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        add(new JLabel("Delivery time:", JLabel.LEFT), constraints);

        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
        constraints.weightx = 1;
        add(deliveryTimeSelector, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.weightx = 0;
        add(new JLabel("Food", JLabel.LEFT), constraints);

        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        constraints.weightx = 1;
        add(foodTextField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.gridheight = 2;
        add(foodListPane, constraints);

        constraints.gridx = 2;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        add(addFoodButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        add(removeFoodButton, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        add(okButton, constraints);

        constraints.gridx = 2;
        constraints.gridy = 5;
        constraints.gridwidth = 2;
        constraints.gridheight = 1;
        add(cancelButton, constraints);

        setModal(true);
    }

    void showAddOrderDialog() {
        setTitle("Add order");
        // For some reason every time the dialog is opened, a component is added at the beginning, but I can't remove it
        // or everything else goes belly up because Swing...
        // removeAll();

        textField1.setText("(0, 0)");
        deliveryTimeSelector.setValue(mainFrame.getSimulation().getCurrentTick());

        pack();
        setVisible(true);
    }

    void showEditOrderDialog(ConfirmedOrder order) {
        setTitle("Edit order");

        // TODO: make okButton update actual order
        textField1.setText(order.getLocation().toString());
        deliveryTimeSelector.setValue(order.getTimeInterval().getStart());
        order.getFoodList().forEach(foodListModel::addElement);

        pack();
        setVisible(true);
    }

    private void resetFields() {
        textField1.setText("(0, 0)");
        deliveryTimeSelector.setValue(mainFrame.getSimulation().getCurrentTick());
        foodTextField.setText("");
    }
}

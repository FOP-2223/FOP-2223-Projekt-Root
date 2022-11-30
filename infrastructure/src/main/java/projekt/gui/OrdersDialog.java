package projekt.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import projekt.delivery.routing.ConfirmedOrder;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;

class OrdersDialog extends Dialog<ConfirmedOrder> {

    private final TextField locationTextField = new TextField();
    private final Spinner<Long> deliveryTimeSpinner;
    private final TextField foodTextField = new TextField();
    private final Button addFoodButton = new Button("Add food");
    //private final DefaultListModel<String> foodListModel = new DefaultListModel<>();
    private final ListView<String> foodList = new ListView<>();
    //private final ScrollPane foodListPane = new ScrollPane(foodList);
    private final Button removeFoodButton = new Button("Remove Food");
    private final Button okButton = new Button("Submit order");
    private final Button cancelButton = new Button("Cancel");

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

        this.getGraphic().minHeight(600);
        this.getGraphic().minWidth(600);
        this.getGraphic().maxHeight(600);
        this.getGraphic().maxWidth(600);
        //setMinimumSize(new Dimension(600, 500));
        //setMaximumSize(new Dimension(600, 600));
        // ignored for whatever reason... ask your favorite higher being as to why


        deliveryTimeSpinner = new Spinner<>(mainFrame.getSimulation().getCurrentTick(), Long.MAX_VALUE, 1);
        deliveryTimeSpinner.getValueFactory().setValue(mainFrame.getSimulation().getCurrentTick());
        //TODO in LocalDateTime umrechnen?

        foodList.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                removeFoodButton.setDisable(foodList.getSelectionModel().getSelectedIndex() < 0);
            }
        );
        addFoodButton.setOnAction(actionEvent -> {
            String food = foodTextField.getText();
            foodList.getItems().add(food);
            resetFields();
        });
        removeFoodButton.setDisable(true);
        removeFoodButton.setOnAction(actionEvent -> foodList.getItems().remove(foodList.getSelectionModel().getSelectedItem()));

        okButton.setOnAction(actionEvent -> {
            Integer[] coordinates = Arrays.stream(locationTextField.getText().replaceAll("\\(?\\)?", "").split(","))
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
            okButton.setVisible(false);
        });

        cancelButton.setOnAction(actionEvent -> {
            mainFrame.getControlsPanel().unpause();
            cancelButton.setVisible(false);
        });

        /*// OLD TODO: why are the components jumping around depending on the number of entries in extrasPane...
        setLayout(new GridBagLayout());
        final GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.anchor = GridBagConstraints.PAGE_START;
        */

        final GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(6, 6, 6, 6));

        grid.add(new Label("Location:"), 0, 0, 1, 1);
        grid.add(locationTextField, 1, 0, 3, 1);

        grid.add(new Label("Delivery time:"), 0, 1, 1, 1);
        grid.add(deliveryTimeSpinner, 1, 1, 3, 1);

        grid.add(new Label("Food"), 0, 2, 1, 1);
        grid.add(foodTextField, 1, 2, 3, 1);

        grid.add(foodList, 0, 3, 2, 2);
        grid.add(addFoodButton, 2, 3, 2, 1);

        grid.add(removeFoodButton, 2, 4, 2, 1);
        grid.add(okButton, 0, 5, 2, 1);

        grid.add(cancelButton, 2, 5, 2, 1);

        getDialogPane().setContent(grid);
    }

    void showAddOrderDialog() {
        setTitle("Add order");
        // For some reason every time the dialog is opened, a component is added at the beginning, but I can't remove it
        // or everything else goes belly up because Swing...
        // removeAll();

        locationTextField.setText("(0, 0)");
        deliveryTimeSpinner.getValueFactory().
            setValue(mainFrame.getSimulation().getCurrentTick());
        getDialogPane().setVisible(true);
    }

    void showEditOrderDialog(ConfirmedOrder order) {
        setTitle("Edit order");

        // TODO: make okButton update actual order
        locationTextField.setText(order.getLocation().toString());
        deliveryTimeSpinner.getValueFactory().setValue(order.getDeliveryInterval().getStart());
        //order.getFoodList().forEach(foodListModel::addElement);

        //pack();
        getDialogPane().setVisible(true);
    }

    private void resetFields() {
        locationTextField.setText("(0, 0)");
        deliveryTimeSpinner.getValueFactory().setValue(mainFrame.getSimulation().getCurrentTick());
        foodTextField.setText("");
    }
}

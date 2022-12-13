package projekt.gui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import projekt.base.Location;
import projekt.base.TickInterval;
import projekt.delivery.routing.ConfirmedOrder;
import projekt.delivery.routing.VehicleManager;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

class OrdersDialog extends Dialog<ConfirmedOrder> {

    private final TextField locationTextField = new TextField();
    private final Spinner<Long> deliveryTimeSpinner;
    private final TextField foodTextField = new TextField();
    private final Button addFoodButton = new Button("Add food");
    //private final DefaultListModel<String> foodListModel = new DefaultListModel<>();
    private final ListView<String> foodList = new ListView<>();
    //private final ScrollPane foodListPane = new ScrollPane(foodList);
    private final Button removeFoodButton = new Button("Remove Food");
    ButtonType buttonTypeOk = new ButtonType("Submit order", ButtonBar.ButtonData.OK_DONE);
    private final Button okButton = new Button("Submit order");
    private final Button cancelButton = new Button("Cancel");

    private final SimulationScene scene;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    private Integer[] coordinates;

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
    OrdersDialog(SimulationScene scene) {
        this.scene = scene;
        setHeaderText("Order dialog");
        setTitle("Order Title");

        getDialogPane().getButtonTypes().add(buttonTypeOk);
        //setMinimumSize(new Dimension(600, 500));
        //setMaximumSize(new Dimension(600, 600));
        // ignored for whatever reason... ask your favorite higher being as to why
        deliveryTimeSpinner = new Spinner<>(new SpinnerValueFactory<Long>() {
            @Override
            public void decrement(int steps) {
                setValue(getValue() - steps);
            }

            @Override
            public void increment(int steps) {
                setValue(getValue() + steps);
            }
        });
        deliveryTimeSpinner.getValueFactory().setValue(scene.simulation.getCurrentTick());
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

        coordinates = new Integer[2];

        okButton.setOnAction(actionEvent -> {



//            ordersPanel.addOrder(textField1.getText());
            //scene.getControlsPanel().unpause();
            okButton.setVisible(false);

            okButton.setDefaultButton(true);
        });

        cancelButton.setOnAction(actionEvent -> {
            //scene.getControlsPanel().unpause();
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

        setResultConverter(new Callback<ButtonType, ConfirmedOrder>() {
            @Override
            public ConfirmedOrder call(ButtonType buttonType) {

                if (buttonType == buttonTypeOk) {

                    coordinates = Arrays.stream(locationTextField.getText().replaceAll("\\(?\\)?", "").split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toArray(Integer[]::new);

                    var tick = deliveryTimeSpinner.getValue();
                    //var t = (String) deliveryTimeSelector.getValue());
                    //IntStream.range(0, foodListModel.getSize()).mapToObj(foodListModel::getElementAt).toList();
                    var deliveryTime = LocalDateTime.parse(tick + "", dateTimeFormatter).toInstant(ZoneOffset.UTC);
                    final TickInterval tickInterval = new TickInterval(0, tick);
                    var location = new Location(coordinates[0], coordinates[1]);
                    var deliveryService = scene.simulation.getDeliveryService();
                    List<VehicleManager.OccupiedRestaurant> restaurants = deliveryService.getVehicleManager().getOccupiedRestaurants().stream().toList();
                    List<String> foodItems = List.of("");
                    return new ConfirmedOrder(location, restaurants.get(0), tickInterval, foodItems, 0.0);

                    /* TODO Wie orders submitten ohne pizzeria?
                    ConfirmedOrder order = scene.pizzeria.submitOrder(
                        coordinates[0],
                        coordinates[1],
                        LocalDateTime.parse(((String) deliveryTimeSelector.getValue()).substring(0, 16), dateTimeFormatter).toInstant(ZoneOffset.UTC),
                        IntStream.range(0, foodListModel.getSize()).mapToObj(foodListModel::getElementAt).toList()
                    );*/

                }
                return null;
            }
        });
    }

    void showAddOrderDialog() {
        setTitle("Add order");
        // For some reason every time the dialog is opened, a component is added at the beginning, but I can't remove it
        // or everything else goes belly up because Swing...
        // removeAll();

        locationTextField.setText("(0, 0)");
        deliveryTimeSpinner.getValueFactory().
            setValue(scene.simulation.getCurrentTick());
        getDialogPane().setVisible(true);
        var result = showAndWait();
        ConfirmedOrder selected;
        if (result.isPresent()) {
            selected = result.get();
        }
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
        deliveryTimeSpinner.getValueFactory().setValue(scene.simulation.getCurrentTick());
        foodTextField.setText("");
    }
}

package projekt.gui.pane;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import projekt.delivery.routing.Vehicle;
import projekt.delivery.simulation.Simulation;

import java.util.List;
import java.util.stream.Collectors;

public class VehicleInfoPane extends Pane {

    private final Simulation simulation;
    private ObservableList<Vehicle> vehicles;

    public VehicleInfoPane(Simulation simulation) {
        this.simulation = simulation;
        TitledPane titledVehiclesPane = new TitledPane("Vehicles:", createVehicleTableView());
        titledVehiclesPane.setCollapsible(false);
        heightProperty().addListener((obs, oldValue, newValue) -> titledVehiclesPane.setMinHeight(newValue.doubleValue()));

        getChildren().addAll(titledVehiclesPane);
    }

    public void refresh() {
        vehicles.clear();
        vehicles.addAll(simulation.getDeliveryService().getVehicleManager().getVehicles());
    }

    private TableView<Vehicle> createVehicleTableView() {
        vehicles = FXCollections.observableArrayList(
            simulation.getDeliveryService().getVehicleManager().getVehicles());

        TableView<Vehicle> vehiclesTableView = new TableView<>(vehicles);
        vehiclesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        ScrollPane scrollPane = new ScrollPane(vehiclesTableView);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        TableColumn<Vehicle, String> idColumn = new TableColumn<>("ID");
        idColumn.setMinWidth(25);
        idColumn.setMaxWidth(25);
        idColumn.widthProperty().addListener((observable, oldValue, newValue) -> System.out.println(newValue));
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(Integer.toString(data.getValue().getId())));

        TableColumn<Vehicle, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(data -> {
            if (data.getValue().getOccupied().getComponent() instanceof projekt.delivery.routing.Region.Node node) {
                return new SimpleStringProperty(node.getLocation().toString());
            } else if (data.getValue().getOccupied().getComponent() instanceof projekt.delivery.routing.Region.Edge edge) {
                return new SimpleStringProperty("%s - %s".formatted(edge.getNodeA().getLocation(), edge.getNodeB().getLocation()));
            } else {
                return null;
            }
        });

        TableColumn<Vehicle, String> ordersColumn = new TableColumn<>("Orders");
        ordersColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOrders().stream()
            .flatMap(order -> order.getFoodList().stream())
            .collect(Collectors.joining("\n"))));

        vehiclesTableView.getColumns().addAll(List.of(idColumn, locationColumn, ordersColumn));

        return vehiclesTableView;
    }
}

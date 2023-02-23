package projekt.gui.scene;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import projekt.base.*;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.generator.OrderGenerator;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.routing.*;
import projekt.gui.controller.VehicleManagerCreationSceneController;
import projekt.gui.pane.MapPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VehicleManagerCreationScene extends MenuScene<VehicleManagerCreationSceneController> {

    private final MapPane mapPane = new MapPane();
    private final Region.Builder regionBuilder = Region.builder();
    private final VehicleManager.Builder vehicleManagerBuilder = VehicleManager.builder();
    private final Button createNodeButton = new Button("Create Node At (X1, Y1)");
    private final Button createNeighborhoodButton = new Button("Create Neighborhood At (X1, Y1)");
    private final Button createRestaurantButton = new Button("Create Restaurant At (X1, Y1)");
    private final Button createEdgeButton = new Button("Create Edge From (X1, Y1) To (X2, Y2)");
    private final Button removeNodeButton = new Button("Remove Node");
    private final Button removeEdgeButton = new Button("Remove Edge");
    private final Button createVehicleButton = new Button("Create Vehicle");
    private final Button removeVehicleButton = new Button("Remove Vehicle");
    private String name;
    private Long simulationLength;
    private VehicleManager vehicleManager;
    private OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder;
    private Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap;
    private int x1;
    private int x2;
    private int y1;
    private int y2;
    private double capacity = 1.0;
    private String componentName;
    private Region.Restaurant.Preset preset;

    public VehicleManagerCreationScene() {
        super(new VehicleManagerCreationSceneController(), "Edit Vehicle Manager");
    }

    public void init(List<ProblemArchetype> problems,
                     String name,
                     Long simulationLength,
                     VehicleManager vehicleManager,
                     OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder,
                     Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap) {
        this.name = name;
        this.simulationLength = simulationLength;
        this.vehicleManager = vehicleManager;
        this.orderGeneratorFactoryBuilder = orderGeneratorFactoryBuilder;
        this.raterFactoryBuilderMap = raterFactoryBuilderMap;
        super.init(problems);
    }

    @Override
    public void initComponents() {

        if (vehicleManager != null) {
            mapPane.addAllNodes(vehicleManager.getRegion().getNodes());
            mapPane.addAllEdges(vehicleManager.getRegion().getEdges());
            mapPane.addAllVehicles(vehicleManager.getAllVehicles());
        }

        root.setCenter(mapPane);
        root.setRight(createOptionsScrollPane());

        mapPane.onNodeSelection(n -> {
            removeNodeButton.setDisable(n.getAdjacentEdges().size() != 0);
            createVehicleButton.setDisable(!(n instanceof Region.Restaurant));
        });
        mapPane.onNodeRemoveSelection(n -> {
            removeNodeButton.setDisable(true);
            createVehicleButton.setDisable(true);
        });

        mapPane.onEdgeSelection(e -> removeEdgeButton.setDisable(false));
        mapPane.onEdgeRemoveSelection(e -> removeEdgeButton.setDisable(true));

        mapPane.onVehicleSelection(v -> removeVehicleButton.setDisable(false));
        mapPane.onVehicleRemoveSelection(v -> removeVehicleButton.setDisable(true));
    }

    private ScrollPane createOptionsScrollPane() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);

        vBox.getChildren().addAll(
            createDistanceCalculatorChoiceBox(),
            createPathCalculatorChoiceBox(),
            createNameHBox(),
            createCord1HBox(),
            createCord2HBox(),
            createCreateNodeButton(),
            createCreateNeighborhoodButton(),
            createRestaurantPresetChoiceBox(),
            createCreateRestaurantButton(),
            createCreateEdgeButton(),
            createRemoveNodeButton(),
            createRemoveEdgeButton(),
            createCapacityHBox(),
            createCreateVehicleButton(),
            createRemoveVehicleButton(),
            createCreateButton()
        );

        vBox.getChildren().stream()
            .filter(Button.class::isInstance)
            .map(Button.class::cast)
            .forEach(button -> {
                button.setPrefSize(200, 50);
                button.setMaxWidth(Double.MAX_VALUE);
            });
        vBox.setPadding(new Insets(20, 20, 20, 20));

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setContent(vBox);
        scrollPane.setMinWidth(300);

        return scrollPane;
    }

    private void handleValueChange() {
        createNodeButton.setDisable(!regionBuilder.checkNode(componentName, new Location(x1, y1)));
        removeNodeButton.setDisable(mapPane.getSelectedNode() == null);
        createNeighborhoodButton.setDisable(!regionBuilder.checkNode(componentName, new Location(x1, y1)));
        createEdgeButton.setDisable(!regionBuilder.checkEdge(componentName, new Location(x1, y1), new Location(x2, y2)));
        removeEdgeButton.setDisable(mapPane.getSelectedEdge() == null);
        createRestaurantButton.setDisable(!regionBuilder.checkNode(preset.name(), new Location(x1, y1)));
        if (capacity == 0) {
            createVehicleButton.setDisable(true);
        }
        if (mapPane.getSelectedNode() != null) {
            removeNodeButton.setDisable(mapPane.getSelectedNode().getAdjacentEdges().size() != 0);
        }
    }

    private void updateMap() {
        Region region = regionBuilder.build();
        vehicleManagerBuilder.region(region);
        VehicleManager vehicleManager = vehicleManagerBuilder.build();

        Region.Node selectedNode = mapPane.getSelectedNode();
        Region.Edge selectedEdge = mapPane.getSelectedEdge();

        mapPane.clear();
        mapPane.addAllNodes(region.getNodes());
        mapPane.addAllEdges(region.getEdges());
        mapPane.addAllVehicles(vehicleManager.getAllVehicles());
        if (region.getNodes().size() == 1) mapPane.center();

        if (selectedNode != null) {
            selectedNode = region.getNode(selectedNode.getLocation());
        }
        if (selectedNode != null) {
            mapPane.selectNode(selectedNode);
        }

        if (selectedEdge != null) {
            selectedEdge = region.getEdge(selectedEdge.getNodeA().getLocation(), selectedEdge.getNodeB().getLocation());
        }
        if (selectedEdge != null) {
            mapPane.selectEdge(selectedEdge);
        }
    }

    private HBox createDistanceCalculatorChoiceBox() {

        HBox box = new HBox();

        Label label = new Label("Distance Calculator:");

        EuclideanDistanceCalculator euclideanDistanceCalculator = new EuclideanDistanceCalculator();
        ChessboardDistanceCalculator chessboardDistanceCalculator = new ChessboardDistanceCalculator();
        ManhattanDistanceCalculator manhattanDistanceCalculator = new ManhattanDistanceCalculator();

        ChoiceBox<DistanceCalculator> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().setAll(euclideanDistanceCalculator, chessboardDistanceCalculator, manhattanDistanceCalculator);
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(DistanceCalculator distanceCalculator) {
                return distanceCalculator.getClass().getSimpleName();
            }

            @Override
            public DistanceCalculator fromString(String distanceCalculator) {
                throw new UnsupportedOperationException();
            }
        });

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> regionBuilder.distanceCalculator(choiceBox.getItems().get((Integer) newValue)));

        choiceBox.getSelectionModel().select(euclideanDistanceCalculator);

        box.getChildren().addAll(label, createIntermediateRegion(0), choiceBox);

        return box;
    }

    private HBox createPathCalculatorChoiceBox() {

        HBox box = new HBox();

        Label label = new Label("Path Calculator:");

        DijkstraPathCalculator dijkstraPathCalculator = new DijkstraPathCalculator();

        ChoiceBox<PathCalculator> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().setAll(dijkstraPathCalculator);
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(PathCalculator pathCalculator) {
                return pathCalculator.getClass().getSimpleName();
            }

            @Override
            public PathCalculator fromString(String distanceCalculator) {
                throw new UnsupportedOperationException();
            }
        });

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> vehicleManagerBuilder.pathCalculator(choiceBox.getItems().get((Integer) newValue)));

        choiceBox.getSelectionModel().select(dijkstraPathCalculator);

        box.getChildren().addAll(label, createIntermediateRegion(0), choiceBox);

        return box;
    }

    private HBox createNameHBox() {
        HBox box = new HBox();

        Label nameLabel = new Label("Name: ");

        TextField nameTextField = new TextField();
        nameTextField.setMaxWidth(200);
        nameTextField.textProperty().addListener((obs, oldValue, newValue) -> {
            componentName = newValue;
            handleValueChange();
        });

        box.getChildren().addAll(nameLabel, createIntermediateRegion(0), nameTextField);

        return box;
    }

    private HBox createCord1HBox() {
        HBox cord1Box = new HBox();

        Label x1Label = new Label("X1: ");
        TextField x1TextField = createIntegerTextField(x -> {
            x1 = x;
            handleValueChange();
        }, 0);
        x1TextField.setMaxWidth(60);

        Label y1Label = new Label("Y1: ");
        TextField y1TextField = createIntegerTextField(y -> {
            y1 = y;
            handleValueChange();
        }, 0);
        y1TextField.setMaxWidth(60);

        cord1Box.getChildren().addAll(x1Label, x1TextField, createIntermediateRegion(10), y1Label, y1TextField);

        return cord1Box;
    }

    private HBox createCord2HBox() {
        HBox cord2Box = new HBox();

        Label x2Label = new Label("X2: ");
        TextField x2TextField = createIntegerTextField(x -> {
            x2 = x;
            handleValueChange();
        }, 0);
        x2TextField.setMaxWidth(60);

        Label y2Label = new Label("Y2: ");
        TextField y2TextField = createIntegerTextField(y -> {
            y2 = y;
            handleValueChange();
        }, 0);
        y2TextField.setMaxWidth(60);

        cord2Box.getChildren().addAll(x2Label, x2TextField, createIntermediateRegion(10), y2Label, y2TextField);

        return cord2Box;
    }

    private Button createCreateNodeButton() {
        createNodeButton.setOnAction(e -> {
            regionBuilder.addNode(componentName, new Location(x1, y1));
            updateMap();
            handleValueChange();
        });

        createNodeButton.setDisable(true);

        return createNodeButton;
    }

    private Button createCreateNeighborhoodButton() {
        createNeighborhoodButton.setOnAction(e -> {
            regionBuilder.addNeighborhood(componentName, new Location(x1, y1));
            updateMap();
            handleValueChange();
        });

        createNeighborhoodButton.setDisable(true);

        return createNeighborhoodButton;
    }

    private HBox createRestaurantPresetChoiceBox() {

        HBox box = new HBox();

        Label label = new Label("Restaurant:");

        ChoiceBox<Region.Restaurant.Preset> choiceBox = new ChoiceBox<>();

        choiceBox.getItems().setAll(
            Region.Restaurant.LOS_FOPBOTS_HERMANOS,
            Region.Restaurant.JAVA_HUT,
            Region.Restaurant.PASTAFAR,
            Region.Restaurant.ISENJAR,
            Region.Restaurant.MIDDLE_FOP,
            Region.Restaurant.MOUNT_DOOM_PIZZA
        );
        choiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Region.Restaurant.Preset preset) {
                return preset.name();
            }

            @Override
            public Region.Restaurant.Preset fromString(String distanceCalculator) {
                throw new UnsupportedOperationException();
            }
        });

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((obs, oldValue, newValue) -> {
            preset = choiceBox.getItems().get((Integer) newValue);
            handleValueChange();
        });

        choiceBox.getSelectionModel().select(Region.Restaurant.LOS_FOPBOTS_HERMANOS);

        box.getChildren().addAll(label, createIntermediateRegion(0), choiceBox);

        return box;
    }

    private Button createCreateRestaurantButton() {
        createRestaurantButton.setOnAction(e -> {
            regionBuilder.addRestaurant(new Location(x1, y1), preset);
            updateMap();
            handleValueChange();
        });

        return createRestaurantButton;
    }

    private Button createCreateEdgeButton() {
        createEdgeButton.setOnAction(e -> {
            regionBuilder.addEdge(componentName, new Location(x1, y1), new Location(x2, y2));
            updateMap();
            handleValueChange();
        });

        createEdgeButton.setDisable(true);

        return createEdgeButton;
    }

    private Button createRemoveNodeButton() {
        removeNodeButton.setOnAction(e -> {
            regionBuilder.removeComponent(mapPane.getSelectedNode().getName());
            vehicleManagerBuilder.removeVehicle(mapPane.getSelectedNode().getLocation());

            updateMap();
            handleValueChange();
        });

        removeNodeButton.setDisable(true);

        return removeNodeButton;
    }

    private Button createRemoveEdgeButton() {
        removeEdgeButton.setOnAction(e -> {
            regionBuilder.removeComponent(mapPane.getSelectedEdge().getName());
            updateMap();
            handleValueChange();
        });

        removeEdgeButton.setDisable(true);

        return removeEdgeButton;
    }

    private HBox createCapacityHBox() {
        HBox box = new HBox();

        Label capacityLabel = new Label("Capacity: ");

        TextField capacityTextField = createPositiveDoubleTextField(d -> {
            capacity = d;
            handleValueChange();
        }, 1.0);

        box.getChildren().addAll(capacityLabel, createIntermediateRegion(0), capacityTextField);

        return box;
    }

    private Button createCreateVehicleButton() {
        createVehicleButton.setOnAction(e -> {
            vehicleManagerBuilder.addVehicle(mapPane.getSelectedNode().getLocation(), capacity);
            updateMap();
            handleValueChange();
        });

        createVehicleButton.setDisable(true);

        return createVehicleButton;
    }

    private Button createRemoveVehicleButton() {
        removeVehicleButton.setOnAction(e -> {
            vehicleManagerBuilder.removeVehicle(mapPane.getSelectedNode().getLocation());
            updateMap();
            handleValueChange();
        });

        removeVehicleButton.setDisable(true);

        return removeVehicleButton;
    }

    private Button createCreateButton() {
        Button createButton = new Button("Create");

        createButton.setOnAction(e -> {
            VehicleManager vehicleManager = vehicleManagerBuilder.region(regionBuilder.build()).build();

            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });

        return createButton;
    }


    @Override
    public void initReturnButton() {
        returnButton.setOnAction(e -> {
            ProblemCreationScene scene = (ProblemCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.PROBLEM_CREATION, getController().getStage());
            scene.init(new ArrayList<>(problems), name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
    }

    @Override
    public VehicleManagerCreationSceneController getController() {
        return controller;
    }

}

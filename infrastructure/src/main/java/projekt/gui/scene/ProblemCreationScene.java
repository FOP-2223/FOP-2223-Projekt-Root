package projekt.gui.scene;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.archetype.ProblemArchetypeImpl;
import projekt.delivery.generator.OrderGenerator;
import projekt.delivery.rating.Rater;
import projekt.delivery.rating.RatingCriteria;
import projekt.delivery.rating.TravelDistanceRater;
import projekt.delivery.routing.VehicleManager;
import projekt.gui.controller.ProblemCreationSceneController;
import projekt.gui.pane.ProblemArchetypeOverviewPane;
import projekt.io.IOHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProblemCreationScene extends MenuScene<ProblemCreationSceneController> {


    private final BorderPane createNewPane = new BorderPane();
    private final Button createAndAddButton = new Button("Create And Add");
    private final ScrollPane problemsPane = new ScrollPane();
    private final VBox buttonsVbox = new VBox();
    private TableView<ProblemArchetype> table;
    private VehicleManager vehicleManager;
    private OrderGenerator.FactoryBuilder orderGeneratorFactoryBuilder;
    private Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap;
    private Long simulationLength;
    private String name;

    public ProblemCreationScene() {
        super(new ProblemCreationSceneController(), "Add ProblemArchetype");
    }

    public void init(List<ProblemArchetype> problems,
                     String name,
                     Long simulationLength,
                     VehicleManager vehicleManager,
                     OrderGenerator.FactoryBuilder orderGeneratorFactory,
                     Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap) {
        this.name = name == null ? "" : name;
        this.simulationLength = simulationLength == null ? 0L : simulationLength;
        this.vehicleManager = vehicleManager;
        this.orderGeneratorFactoryBuilder = orderGeneratorFactory;
        this.raterFactoryBuilderMap = raterFactoryBuilderMap == null ? new HashMap<>() : raterFactoryBuilderMap;
        super.init(problems);
    }

    @Override
    public void initComponents() {
        TabPane tabPane = new TabPane();

        buttonsVbox.setPrefSize(200, 100);
        buttonsVbox.setAlignment(Pos.CENTER);
        buttonsVbox.setSpacing(10);

        setupProblemsPane();

        buttonsVbox.getChildren().add(createNameHBox());
        buttonsVbox.getChildren().add(createSimulationLengthHBox());
        buttonsVbox.getChildren().add(createNewVehicleManagerButton());
        buttonsVbox.getChildren().add(createNewOrderGeneratorButton());
        buttonsVbox.getChildren().add(createNewRaterFactoryButton());
        buttonsVbox.getChildren().add(createCreateAndAddButton());

        limitWidth(buttonsVbox.getChildren(), 200);

        createNewPane.setCenter(buttonsVbox);

        handleValueChange();

        Tab createNewTab = new Tab("Create new ProblemArchetype", createNewPane);

        BorderPane tableBorderPane = new BorderPane();
        setupTable();
        tableBorderPane.setCenter(table);
        tableBorderPane.setBottom(createAddButton());
        tableBorderPane.setPadding(new Insets(20, 20, 20, 20));

        Tab selectExistingTab = new Tab("Select existing Problem", tableBorderPane);

        createNewTab.setClosable(false);
        selectExistingTab.setClosable(false);

        tabPane.getTabs().add(createNewTab);
        tabPane.getTabs().add(selectExistingTab);

        root.setCenter(tabPane);
    }

    @Override
    public void initReturnButton() {
        returnButton.setOnAction(e -> {
            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(new ArrayList<>(problems));
        });
    }

    private void handleValueChange() {
        reloadProblemOverviewPanel();
        buttonsVbox.getChildren().remove(problemsPane);
        setupProblemsPane();
        buttonsVbox.getChildren().add(problemsPane);
        createAndAddButton.setDisable(checkProblem().size() > 0);
    }

    private void reloadProblemOverviewPanel() {
        ProblemArchetypeOverviewPane problemArchetypeOverviewPane = new ProblemArchetypeOverviewPane(name, simulationLength, orderGeneratorFactoryBuilder, raterFactoryBuilderMap, vehicleManager);
        createNewPane.setRight(problemArchetypeOverviewPane);
        BorderPane.setAlignment(problemArchetypeOverviewPane, Pos.CENTER);
    }

    private List<String> checkProblem() {
        List<String> problems = new ArrayList<>();

        if (name == null) {
            problems.add("Name is not set");
        } else if (name.trim().equals("")) {
            problems.add("Name is empty");
        } else if (IOHelper.readProblems().stream().anyMatch(problem -> problem.name().equals(name))) {
            problems.add("Duplicate name");
        }

        if (simulationLength < 0) {
            problems.add("Simulation length is negative");
        }

        if (orderGeneratorFactoryBuilder == null) {
            problems.add("Order generator is not set");
        }

        if (vehicleManager == null) {
            problems.add("Vehicle manager is not set");
        }

        return problems;
    }

    private void setupProblemsPane() {
        VBox outerBox = new VBox();

        Label title = new Label("Problems:");
        title.setStyle("""
               -fx-underline: true;
               -fx-text-fill: red;
            """);
        outerBox.getChildren().add(title);
        outerBox.getChildren().add(new Label(""));

        for (String problemDescription : checkProblem()) {
            Label label = new Label(problemDescription);
            label.setStyle("""
                    -fx-text-fill: red;
                """);
            outerBox.getChildren().add(label);
        }

        problemsPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        problemsPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        problemsPane.setContent(outerBox);
        problemsPane.setPrefSize(180, 150);
    }

    private void setupTable() {
        table = createProblemArchetypeTableView();
        table.getItems().setAll(IOHelper.readProblems().stream()
            .filter(problem -> problems.stream()
                .noneMatch(problem2 -> problem2.name().equals(problem.name())))
            .toList());
    }

    private HBox createAddButton() {
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            problems.add(table.getSelectionModel().getSelectedItem());
            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(problems);
        });
        addButton.setPrefWidth(200);

        HBox addButtonHBox = new HBox();
        addButtonHBox.setAlignment(Pos.CENTER);
        addButtonHBox.getChildren().add(addButton);
        return addButtonHBox;
    }

    private Button createCreateAndAddButton() {
        createAndAddButton.setOnAction(e -> {
            ProblemArchetypeImpl problem = new ProblemArchetypeImpl(orderGeneratorFactoryBuilder.build(),
                vehicleManager,
                raterFactoryBuilderMapToFactory(raterFactoryBuilderMap),
                simulationLength,
                name);

            problems.add(problem);
            IOHelper.writeProblem(problem);

            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(new ArrayList<>(problems));
        });
        return createAndAddButton;
    }

    private HBox createNameHBox() {
        HBox nameHBox = new HBox();

        Label nameLabel = new Label("Name:");

        TextField nameTextField = new TextField();
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            name = nameTextField.getText();
            handleValueChange();
        });
        nameTextField.setMaxWidth(200);
        nameTextField.setText(name);

        nameHBox.getChildren().addAll(nameLabel, createIntermediateRegion(0), nameTextField);
        return nameHBox;
    }

    private HBox createSimulationLengthHBox() {
        HBox lengthHBox = new HBox();

        Label lengthLabel = new Label("Length:");

        TextField lengthTextField = createLongTextField(value -> {
            simulationLength = value;
            handleValueChange();
        }, 0L);
        lengthTextField.setMaxWidth(200);
        lengthTextField.setText(simulationLength.toString());

        lengthHBox.getChildren().addAll(lengthLabel, createIntermediateRegion(0), lengthTextField);
        return lengthHBox;
    }

    private Button createNewRaterFactoryButton() {
        Button newRaterButton = new Button("Edit Rater");
        newRaterButton.setOnAction(e -> {
            RaterFactoryMapCreationScene scene = (RaterFactoryMapCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.RATER_CREATION, getController().getStage());
            scene.init(problems, name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
        return newRaterButton;
    }

    private Map<RatingCriteria, Rater.Factory> raterFactoryBuilderMapToFactory(Map<RatingCriteria, Rater.FactoryBuilder> raterFactoryBuilderMap) {
        return raterFactoryBuilderMap.entrySet().stream()
            .map(entry -> {
                if (entry.getValue() instanceof TravelDistanceRater.FactoryBuilder travelDistanceRater) {
                    travelDistanceRater.setVehicleManager(vehicleManager);
                }
                return Map.entry(entry.getKey(), entry.getValue().build());
            })
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Button createNewOrderGeneratorButton() {
        Button newOrderGeneratorButton = new Button("Edit Order Generator");
        newOrderGeneratorButton.setOnAction(e -> {
            OrderGeneratorFactoryCreationScene scene = (OrderGeneratorFactoryCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.ORDER_CREATION, getController().getStage());
            scene.init(problems, name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
        return newOrderGeneratorButton;
    }

    private Button createNewVehicleManagerButton() {
        Button newVehicleManagerButton = new Button("Edit Vehicle Manager");
        newVehicleManagerButton.setOnAction(e -> {
            VehicleManagerCreationScene scene = (VehicleManagerCreationScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.VEHICLE_MANAGER_CREATION, getController().getStage());
            scene.init(problems, name, simulationLength, vehicleManager, orderGeneratorFactoryBuilder, raterFactoryBuilderMap);
        });
        return newVehicleManagerButton;
    }

    @Override
    public ProblemCreationSceneController getController() {
        return controller;
    }


}

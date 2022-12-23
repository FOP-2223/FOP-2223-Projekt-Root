package projekt.gui.scene;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.gui.controller.ControlledScene;
import projekt.gui.controller.MenuSceneController;
import projekt.io.ProblemArchetypeIO;

import java.io.*;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;

public abstract class MenuScene<SC extends MenuSceneController> extends Scene implements ControlledScene<SC> {

    protected final BorderPane root;
    protected final SC controller;

    protected final Button returnButton;
    protected final Button quitButton;

    protected List<ProblemArchetype> problems;

    public MenuScene(SC controller, String title, String... styleSheets) {
        super(new BorderPane());

        // Typesafe reference to the root group of the scene.
        root = (BorderPane) getRoot();
        root.setPrefSize(700, 700);
        this.controller = controller;

        final Label titleLabel = new Label(title);
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");

        root.setTop(titleLabel);
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        //setup return and quit buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(20, 20, 20, 20));

        returnButton = new Button("Return");
        buttons.getChildren().add(returnButton);

        quitButton = new Button("Quit");
        quitButton.setOnAction(e -> getController().quit(e));
        buttons.getChildren().add(quitButton);

        root.setBottom(buttons);

        // apply styles
        if (styleSheets != null) {
            root.getStylesheets().addAll(styleSheets);
        }
        root.getStylesheets().add("projekt/gui/menuStyle.css");
        root.getStylesheets().add("projekt/gui/darkMode.css");
    }

    public void init(List<ProblemArchetype> problems) {
        this.problems = problems;
        initComponents();
        initReturnButton();
    }

    public abstract void initComponents();

    public abstract void initReturnButton();

    @Override
    public SC getController() {
        return controller;
    }


    // --- common util --- //

    public static TextField createLongTextField(Consumer<Long> valueChangeConsumer, Long initialValue) {
        TextField longTextField = new TextField();
        longTextField.setText(initialValue.toString());
        longTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                longTextField.setText(oldValue);
                return;
            }

            if (newValue.equals("")) return;

            try {
                valueChangeConsumer.accept(Long.parseLong(longTextField.getText()));
            } catch (NumberFormatException exc) {
                longTextField.setText(oldValue);
            }
        });

        return longTextField;
    }

    public static TextField createPositiveIntegerTextField(Consumer<Integer> valueChangeConsumer, Integer initialValue) {
        TextField positivIntegerTextField = new TextField();
        positivIntegerTextField.setText(initialValue.toString());
        positivIntegerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                positivIntegerTextField.setText(oldValue);
                return;
            }

            if (newValue.equals("")) return;

            try {
                valueChangeConsumer.accept(Integer.parseInt(positivIntegerTextField.getText()));
            } catch (NumberFormatException exc) {
                positivIntegerTextField.setText(oldValue);
            }
        });

        return positivIntegerTextField;
    }

    public static TextField createIntegerTextField(Consumer<Integer> valueChangeConsumer, Integer initialValue) {
        TextField negativeIntegerTextField = new TextField();
        negativeIntegerTextField.setText(initialValue.toString());
        negativeIntegerTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("-?\\d*")) {
                negativeIntegerTextField.setText(oldValue);
                return;
            }

            if (newValue.equals("") || newValue.equals("-")) return;

            try {
                valueChangeConsumer.accept(Integer.parseInt(negativeIntegerTextField.getText()));
            } catch (NumberFormatException exc) {
                negativeIntegerTextField.setText(oldValue);
            }
        });

        return negativeIntegerTextField;
    }

    public static TextField createDoubleTextField(Consumer<Double> valueChangeConsumer, Double initialValue) {
        TextField doubleTextField = new TextField();
        doubleTextField.setText(initialValue.toString());
        doubleTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                doubleTextField.setText(oldValue);
                return;
            }

            if (newValue.equals("")) return;

            try {
                valueChangeConsumer.accept(Double.parseDouble(doubleTextField.getText()));
            } catch (NumberFormatException exc) {
                doubleTextField.setText(oldValue);
            }
        });

        return doubleTextField;
    }

    public static Region createIntermediateRegion(int minWidth) {
        Region intermediateRegion = new Region();
        intermediateRegion.setMinWidth(minWidth);
        HBox.setHgrow(intermediateRegion, Priority.ALWAYS);

        return intermediateRegion;
    }

    public static Label createIndentedLabel(String label) {
        return new Label("         " + label);
    }

    public static void limitWidth(ObservableList<Node> nodes, int maxWidth) {
        for (Node child : nodes) {
            if (child instanceof Region region) {
                region.setMaxWidth(maxWidth);
            }
        }
    }

    public static TableView<ProblemArchetype> createProblemArchetypeTableView() {
        TableView<ProblemArchetype> table = new TableView<>();
        table.setMaxWidth(200);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setMinHeight(200);

        TableColumn<ProblemArchetype, String> column = new TableColumn<>("ProblemArchetype");
        column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().name()));
        column.setMinWidth(200);

        table.getColumns().add(column);

        return table;
    }

    public Set<File> getAllFilesInResourceDir() {
        File dir = new File(Objects.requireNonNull(getClass().getResource("presets")).getPath());
        return new HashSet<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public Set<File> getAllFilesInBuildDir() {
        File dir = new File(Path.of(System.getProperty("user.dir"), "projekt", "gui", "problems").toUri());

        if (!dir.exists()) {
            dir.mkdirs();
        }

        return new HashSet<>(Arrays.asList(Objects.requireNonNull(dir.listFiles())));
    }

    public List<ProblemArchetype> readProblems() {
        List<ProblemArchetype> problems = new ArrayList<>();
        Set<File> files = new HashSet<>();
        files.addAll(getAllFilesInResourceDir());
        files.addAll(getAllFilesInBuildDir());

        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                problems.add(ProblemArchetypeIO.readProblemArchetype(reader));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return problems;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void writeProblem(ProblemArchetype problem) {
        File dir = Path.of(System.getProperty("user.dir"), "projekt", "gui", "problems").toFile();
        File file = Path.of(dir.getPath(), problem.name() + ".txt").toFile();

        if (!dir.exists()) {
            dir.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            file.createNewFile();
            ProblemArchetypeIO.writeProblemArchetype(writer, problem);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

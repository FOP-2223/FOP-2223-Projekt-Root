package projekt.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.RatingCriteria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaterScene extends Scene implements ControlledScene<RaterSceneController> {

    private final BorderPane root;
    private final RaterSceneController controller;

    public RaterScene() {
        super(new BorderPane());
        controller = new RaterSceneController();
        root = (BorderPane) getRoot();
        root.setPrefSize(500, 500);

        final Label titleLabel = new Label("Simulation Score");
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");
        root.setTop(titleLabel);
    }

    public void init(Map<RatingCriteria, Double> result, List<ProblemArchetype> problems) {

        //Configure Title
        final Label titleLabel = new Label("Simulation Score");
        titleLabel.setPadding(new Insets(20, 20, 20, 20));
        titleLabel.setId("Title");
        BorderPane.setAlignment(titleLabel, Pos.CENTER);

        //Configuring category and NumberAxis
        CategoryAxis xaxis= new CategoryAxis();
        Axis<Number> yaxis = new NumberAxis(0.0,1,0.05);
        yaxis.setLabel("Score");

        //Configuring BarChart
        BarChart<String, Number> bar = new BarChart<>(xaxis, yaxis);
        root.setCenter(bar);

        //Add simulation scores to the XYSeries
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        for (Map.Entry<RatingCriteria, Double> entry : result.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey().name(), entry.getValue()));
        }

        //Adding series to the barchart
        bar.getData().add(series);
        bar.setLegendVisible(false);

        root.setTop(titleLabel);

        //setup return and quit buttons
        HBox buttons = new HBox();
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPadding(new Insets(20, 20, 20, 20));

        final var returnButton = new Button("Return");
        returnButton.setOnAction(e -> {
            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(new ArrayList<>(problems));
        });
        buttons.getChildren().add(returnButton);

        final var quitButton = new Button("Quit");
        quitButton.setOnAction(getController()::quit);
        buttons.getChildren().add(quitButton);

        root.setBottom(buttons);

        // apply styles
        root.getStylesheets().add("projekt/gui/darkMode.css");
        root.getStylesheets().add("projekt/gui/raterStyle.css");
    }

    @Override
    public RaterSceneController getController() {
        return controller;
    }
}

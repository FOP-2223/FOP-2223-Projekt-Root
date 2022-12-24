package projekt.gui.scene;

import javafx.scene.chart.*;
import projekt.delivery.archetype.ProblemArchetype;
import projekt.delivery.rating.RatingCriteria;
import projekt.gui.controller.RaterSceneController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RaterScene extends MenuScene<RaterSceneController> {
    private Map<RatingCriteria, Double> result;

    public RaterScene() {
        super(new RaterSceneController(), "Simulation Score", "projekt/gui/raterStyle.css");
    }

    public void init(List<ProblemArchetype> problems, Map<RatingCriteria, Double> result) {
        this.result = result;
        super.init(problems);
    }

    @Override
    public void initComponents() {
        //Configuring category and NumberAxis
        CategoryAxis xaxis = new CategoryAxis();
        Axis<Number> yaxis = new NumberAxis(0.0, 1, 0.05);
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
    }

    @Override
    public void initReturnButton() {
        returnButton.setOnAction(e -> {
            MainMenuScene scene = (MainMenuScene) SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getController().getStage());
            scene.init(new ArrayList<>(problems));
        });
    }

    @Override
    public RaterSceneController getController() {
        return controller;
    }
}

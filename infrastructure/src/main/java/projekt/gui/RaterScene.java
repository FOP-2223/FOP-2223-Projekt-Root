package projekt.gui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import projekt.delivery.rating.RatingCriteria;

import java.util.Map;

public class RaterScene extends Scene implements ControlledScene<RaterSceneController> {

    private final BorderPane root;
    private final RaterSceneController controller;

    public RaterScene() {
        super(new BorderPane());
        controller = new RaterSceneController();
        root = (BorderPane) getRoot();
        root.setTop(new Label("RATER"));
        root.setPrefSize(500, 500);
    }

    public void init(Map<RatingCriteria, Double> result) {
        root.setTop(new Label("RATER INIT"));
    }

    @Override
    public RaterSceneController getController() {
        return controller;
    }
}

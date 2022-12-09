package projekt.gui;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import projekt.delivery.rating.RatingCriteria;

import java.util.Map;

public class RaterScene extends Scene implements ControlledScene<RaterSceneController> {

    private final RaterSceneController controller;
    public RaterScene() {
        super(new BorderPane());
        controller = new RaterSceneController();
    }

    public void init(Map<RatingCriteria, Double> result) {

    }

    @Override
    public RaterSceneController getController() {
        return controller;
    }
}

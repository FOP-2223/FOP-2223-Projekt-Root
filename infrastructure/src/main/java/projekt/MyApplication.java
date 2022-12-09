package projekt;

import javafx.application.Application;
import javafx.stage.Stage;
import projekt.gui.MainMenuScene;
import projekt.gui.SceneSwitcher;

public class MyApplication extends javafx.application.Application {

    public static void launch(String[] args) {
        Application.launch(args);

    }

    @Override
    public void start(Stage primaryStage) {
        SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, primaryStage);
    }
}

package projekt.gui.controller;

import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.Nullable;
import projekt.gui.scene.SceneSwitcher;

/**
 * A SceneController is responsible for dynamically managing a {@link Scene} and its {@link Stage}.
 */
public abstract class SceneController {
    // --Variables-- //

    /**
     * The {@link Stage} that is managed by this {@link SceneController}.
     */
    private Stage stage;

    /**
     * The {@link Stage} that is managed by this {@link SceneController}.
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * Specifies the title of the {@link Stage}.
     * This is used in {@link #initStage(Stage)} to set the title of the {@link Stage}.
     *
     * @return The title of the {@link Stage}.
     */
    public abstract String getTitle();

    // --Setup Methods-- //

    /**
     * Initializes the {@link Stage} of this {@link SceneController}.
     * This default implementation sets the title of the {@link Stage} to {@link #getTitle()}.
     *
     * @param stage The {@link Stage} to initialize.
     */
    public void initStage(final Stage stage) {
        (this.stage = stage).setTitle(getTitle());
    }

    // --Button Actions-- //

    /**
     * Called when the user clicks the "Main Menu" button.
     *
     * @param e The {@link ActionEvent} that triggered this method.
     */
    public Scene loadMainMenuScene(final @Nullable ActionEvent e) {
        return SceneSwitcher.loadScene(SceneSwitcher.SceneType.MAIN_MENU, getStage());
    }
}

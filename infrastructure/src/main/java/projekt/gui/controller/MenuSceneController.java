package projekt.gui.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public abstract class MenuSceneController extends SceneController {

    /**
     * Called when the user clicks the "Quit" button.
     *
     * @param e The {@link ActionEvent} that triggered this method.
     */
    public void quit(final ActionEvent e) {
        Platform.exit();
    }
}

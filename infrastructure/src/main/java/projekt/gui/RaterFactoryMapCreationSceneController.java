package projekt.gui;

import javafx.event.ActionEvent;

public class RaterFactoryMapCreationSceneController extends SceneController {

    @Override
    public String getTitle() {
        return "Create new Raters";
    }

    /**
     * Called when the user clicks the "Quit" button.
     *
     * @param e The {@link ActionEvent} that triggered this method.
     */
    public void quit(final ActionEvent e) {
        getStage().close();
    }
}

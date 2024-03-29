package projekt;

import javafx.application.Application;
import projekt.gui.MyApplication;
import projekt.io.IOHelper;

/**
 * An implementation of the {@link Projekt} interface that starts the gui.
 */
@SuppressWarnings("unused")
public class GUIProjektImpl implements Projekt {

    @Override
    public void start() {
        IOHelper.initProblemPresets();
        Application.launch(MyApplication.class);
    }
}

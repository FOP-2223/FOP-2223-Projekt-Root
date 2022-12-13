package projekt.gui;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;

public class CurrentTimePanel extends Pane {

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    private final Text currentTimeLabel = new Text();

    CurrentTimePanel(SimulationScene scene) {

        getChildren().add(currentTimeLabel);

        new Thread(() -> {
            while (true) {
                currentTimeLabel.setText("Current time and date: " +
                    scene.simulation.getSimulationConfig().tickToLocalDateTime(
                        scene.simulation.getCurrentTick()
                    ).format(dateTimeFormatter));
                try {
                    //noinspection BusyWait
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

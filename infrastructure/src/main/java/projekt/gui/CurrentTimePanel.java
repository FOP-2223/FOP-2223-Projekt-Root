package projekt.gui;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

import java.time.format.DateTimeFormatter;

public class CurrentTimePanel extends Pane {

    private final MainFrame mainFrame;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    //private final JLabel currentTimeLabel = new JLabel();
    private final Text currentTimeLabel = new Text();

    CurrentTimePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        getChildren().add(currentTimeLabel);

        new Thread(() -> {
            while (true) {
                currentTimeLabel.setText("Current time and date: " +
                    mainFrame.getSimulation().getSimulationConfig().tickToLocalDateTime(
                        mainFrame.getSimulation().getCurrentTick()
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

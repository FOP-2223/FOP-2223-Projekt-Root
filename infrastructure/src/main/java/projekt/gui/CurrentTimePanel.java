package projekt.gui;

import javax.swing.*;
import java.time.format.DateTimeFormatter;

public class CurrentTimePanel extends JPanel {

    private final MainFrame mainFrame;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");
    private final JLabel currentTimeLabel = new JLabel();

    CurrentTimePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        add(currentTimeLabel);

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

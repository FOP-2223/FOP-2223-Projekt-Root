package projekt.gui;

import javax.swing.*;
import java.time.format.DateTimeFormatter;

public class CurrentTimePanel extends JPanel {

    private JLabel currentTimeLabel = new JLabel();
    private final MainFrame mainFrame;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    CurrentTimePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // TODO: update on tick
        currentTimeLabel.setText("Current time: " + mainFrame.vehicleManager.getCurrentTime().format(dateTimeFormatter));
        add(currentTimeLabel);
    }
}

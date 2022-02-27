package projekt.gui;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class ControlsPanel extends JPanel {

    private javax.swing.JButton playPauseButton;
    private javax.swing.JButton singleStepButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JSlider tickSpeedSlider;
    private javax.swing.JLabel tickSpeedSliderLabel;

    public ControlsPanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(1, 5, 6, 6));
        setBorder(new TitledBorder("Controls"));
        // setBorder(new CompoundBorder(new TitledBorder("Controls"), new
        // EmptyBorder(12, 0, 0, 0))); // More space up top
        playPauseButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        singleStepButton = new javax.swing.JButton();
        tickSpeedSlider = new javax.swing.JSlider();
        tickSpeedSliderLabel = new javax.swing.JLabel();

        playPauseButton.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        playPauseButton.setText("Play/Pause");

        stopButton.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        stopButton.setText("Stop");

        singleStepButton.setFont(new java.awt.Font("Dialog", 0, 16)); // NOI18N
        singleStepButton.setText("single Step");

        // tickSpeedSlider.setToolTipText("");

        tickSpeedSliderLabel.setText("Tick Speed");

        add(playPauseButton);
        add(stopButton);
        add(singleStepButton);
        add(tickSpeedSlider);
        add(tickSpeedSliderLabel);
    }
}

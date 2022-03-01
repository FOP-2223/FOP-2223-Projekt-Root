package projekt.gui;

import projekt.delivery.SimulationConfig;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;

public class ControlsPanel extends JPanel {

    private JButton playPauseButton;
    private JButton singleStepButton;
    private JButton stopButton;
    private JSlider tickIntervalSlider;
    private JLabel tickIntervalSliderLabel;
    private JLabel mousePositionLabel;
    private SimulationConfig simulationConfig;
    private boolean paused = false;

    public ControlsPanel(SimulationConfig simulationConfig) {
        this.simulationConfig = simulationConfig;
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridLayout(1, 6, 6, 6));
        setBorder(new TitledBorder("Controls"));
        // setBorder(new CompoundBorder(new TitledBorder("Controls"), new
        // EmptyBorder(12, 0, 0, 0))); // More space up top
        playPauseButton = new JButton();
        stopButton = new JButton();
        singleStepButton = new JButton();
        tickIntervalSlider = new JSlider();
        tickIntervalSliderLabel = new JLabel();
        mousePositionLabel = new JLabel();

        playPauseButton.setFont(new Font("Dialog", 0, 16)); // NOI18N
        playPauseButton.setText("Play / Pause");
        playPauseButton.addActionListener(actionEvent -> {
            simulationConfig.setPaused(paused = !paused);
            singleStepButton.setEnabled(paused);
            updateText();
        });

        stopButton.setFont(new Font("Dialog", 0, 16)); // NOI18N
        stopButton.setText("Stop");
        stopButton.setEnabled(false);

        singleStepButton.setFont(new Font("Dialog", 0, 16)); // NOI18N
        singleStepButton.setText("Single step");
        singleStepButton.setEnabled(false);
        singleStepButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

            }
        });

        // tickSpeedSlider.setToolTipText("");
        tickIntervalSlider.setValue(5);
        tickIntervalSlider.setMinimum(2);
        tickIntervalSlider.setMaximum(10);
        tickIntervalSlider.setMajorTickSpacing(1);
        tickIntervalSlider.setSnapToTicks(true);
        tickIntervalSlider.addChangeListener(changeEvent -> {
            simulationConfig.setMillisecondsPerTick(-50 * tickIntervalSlider.getValue() + 600);
            updateText();
        });

        updateText();

        add(playPauseButton);
        add(stopButton);
        add(singleStepButton);
        add(tickIntervalSlider);
        add(tickIntervalSliderLabel);
        add(mousePositionLabel);
    }

    private void updateText() {
        tickIntervalSliderLabel.setText(
            String.format(
                "Tick interval: %d ms %s",
                -50 * tickIntervalSlider.getValue() + 600,
                paused ? "(paused)" : ""
            )
        );
    }

    public JLabel getMousePositionLabel() {
        return mousePositionLabel;
    }
}
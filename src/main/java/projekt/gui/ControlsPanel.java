package projekt.gui;

import projekt.delivery.SimulationConfig;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.TitledBorder;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.IntUnaryOperator;

public class ControlsPanel extends JPanel {

    private JButton playPauseButton;
    private JButton singleStepButton;
    private JButton stopButton;
    private JSlider tickIntervalSlider;
    private JLabel tickIntervalSliderLabel;
    private JLabel mousePositionLabel;
    private final MainFrame mainFrame;
    private final SimulationConfig simulationConfig;
    private boolean paused = false;
    private final IntUnaryOperator speedFunction = i -> 1000 - 100 * i;

    public ControlsPanel(MainFrame mainFrame, SimulationConfig simulationConfig) {
        this.mainFrame = mainFrame;
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
            if (!paused) {
                pause();
            } else {
                unpause();
            }
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
                // TODO: advance one step - make AbstractDeliverService#runTick() public?
                mainFrame.deliveryService.runTick();
            }
        });

        // tickSpeedSlider.setToolTipText("");
        tickIntervalSlider.setValue(0);
        tickIntervalSlider.setMinimum(0);
        tickIntervalSlider.setMaximum(9);
        tickIntervalSlider.setMajorTickSpacing(1);
        tickIntervalSlider.setSnapToTicks(true);
        tickIntervalSlider.addChangeListener(changeEvent -> {
            simulationConfig.setMillisecondsPerTick(speedFunction.applyAsInt(tickIntervalSlider.getValue()));
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
                speedFunction.applyAsInt(tickIntervalSlider.getValue()),
                paused ? "(paused)" : ""
            )
        );
    }

    public JLabel getMousePositionLabel() {
        return mousePositionLabel;
    }

    public void pause() {
        simulationConfig.setPaused(paused = true);
        singleStepButton.setEnabled(true);
        updateText();
    }

    public void unpause() {
        simulationConfig.setPaused(paused = false);
        singleStepButton.setEnabled(false);
        updateText();
    }
}

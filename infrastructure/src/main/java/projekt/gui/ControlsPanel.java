package projekt.gui;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import projekt.delivery.simulation.SimulationConfig;

import java.util.function.IntUnaryOperator;

public class ControlsPanel extends GridPane {

    private final MainFrame mainFrame;
    private final SimulationConfig simulationConfig;
    private final IntUnaryOperator speedFunction = i -> 1000 - 100 * i;
    private Button playPauseButton;
    private Button singleStepButton;
    private Button stopButton;
    private Slider tickIntervalSlider;
    private Label tickIntervalSliderLabel;
    private Label mousePositionLabel;
    private boolean paused = false;

    public ControlsPanel(MainFrame mainFrame, SimulationConfig simulationConfig) {
        this.mainFrame = mainFrame;
        this.simulationConfig = simulationConfig;
        initComponents();
    }

    private void initComponents() {
        //setLayout(new GridLayout(1, 6, 6, 6));
        //setBorder(new TitledBorder("Controls"));
        final Label title = new Label("Controls");
        getChildren().add(title);
        // setBorder(new CompoundBorder(new TitledBorder("Controls"), new
        // EmptyBorder(12, 0, 0, 0))); // More space up top
        playPauseButton = new Button();
        stopButton = new Button();
        singleStepButton = new Button();
        tickIntervalSlider = new Slider();
        tickIntervalSliderLabel = new Label();
        mousePositionLabel = new Label();

        final Font dialog = new Font("Dialog", 16);
        playPauseButton.setFont(dialog); // NOI18N

        playPauseButton.setText("Play / Pause");
        playPauseButton.setOnAction(actionEvent -> {
            if (!paused) {
                pause();
            } else {
                unpause();
            }
        });

        stopButton.setFont(dialog); // NOI18N
        stopButton.setText("Stop");
        stopButton.setDisable(true);

        singleStepButton.setFont(dialog); // NOI18N
        singleStepButton.setText("Single step");
        singleStepButton.setDisable(true);
        singleStepButton.setOnAction(actionEvent -> {
            // TODO: advance one step - make AbstractDeliverService#runTick() public?
            mainFrame.getSimulation().runCurrentTick();
        });

        // tickSpeedSlider.setToolTipText("");
        tickIntervalSlider.setValue(0);
        tickIntervalSlider.setMin(0);
        tickIntervalSlider.setMax(9);
        tickIntervalSlider.setMajorTickUnit(1);
        tickIntervalSlider.setSnapToTicks(true);
        tickIntervalSlider.setOnInputMethodTextChanged(changeEvent -> {
            simulationConfig.setMillisecondsPerTick(speedFunction.applyAsInt((int) tickIntervalSlider.getValue()));
            updateText();
        });

        updateText();

        getChildren().add(playPauseButton);
        getChildren().add(stopButton);
        getChildren().add(singleStepButton);
        getChildren().add(tickIntervalSlider);
        getChildren().add(tickIntervalSliderLabel);
        getChildren().add(mousePositionLabel);
    }

    private void updateText() {
        tickIntervalSliderLabel.setText(
            String.format(
                "Tick interval: %d ms %s",
                speedFunction.applyAsInt((int) tickIntervalSlider.getValue()),
                paused ? "(paused)" : ""
            )
        );
    }

    public Label getMousePositionLabel() {
        return mousePositionLabel;
    }

    public void pause() {
        simulationConfig.setPaused(paused = true);
        singleStepButton.setDisable(false);
        updateText();
    }

    public void unpause() {
        simulationConfig.setPaused(paused = false);
        singleStepButton.setDisable(true);
        updateText();
    }
}

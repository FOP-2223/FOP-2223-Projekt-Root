package projekt.gui;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import projekt.delivery.simulation.SimulationConfig;
import projekt.gui.scene.SimulationScene;

import java.util.function.IntUnaryOperator;

public class ControlsPanel extends BorderPane {

    private final SimulationScene scene;
    private final SimulationConfig simulationConfig;
    private final IntUnaryOperator speedFunction = i -> 1000 - 100 * i;
    private Button playPauseButton;
    private Button singleStepButton;
    private Button stopButton;
    private Slider tickIntervalSlider;
    private Label tickIntervalSliderLabel;
    private Label mousePositionLabel;
    private boolean paused = false;

    public ControlsPanel(SimulationScene scene, SimulationConfig simulationConfig) {
        this.scene = scene;
        this.simulationConfig = simulationConfig;
        initComponents();
    }

    private void initComponents() {
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
            scene.simulation.runCurrentTick();
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
        var slider = new VBox(tickIntervalSlider, tickIntervalSliderLabel);

        updateText();
        setPadding(new Insets(5));
        final HBox buttons = new HBox(playPauseButton, stopButton, singleStepButton,
            slider, mousePositionLabel);
        buttons.setPadding(new Insets(0, 10, 0, 10));
        buttons.setSpacing(10);
        setCenter(buttons);
    }

    private void updateText() {
        tickIntervalSliderLabel.setText(
            String.format(
                "   Tick interval: %d ms %s",
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

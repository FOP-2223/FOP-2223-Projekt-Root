package projekt.delivery;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationConfig {
    private final AtomicInteger millisecondsPerTick;
    private volatile boolean paused = false;

    public SimulationConfig(int millisecondsPerTick) {
        this.millisecondsPerTick = new AtomicInteger(millisecondsPerTick);
    }

    public int getMillisecondsPerTick() {
        return millisecondsPerTick.get();
    }

    public void setMillisecondsPerTick(int millisecondsPerTick) {
        this.millisecondsPerTick.set(millisecondsPerTick);
    }

    public LocalDateTime tickToLocalDateTime(long ticks) {
        //TODO sinnvoll? oder 1 Tick = 1 Minute immer?
        return LocalDateTime.now().minusNanos(1000 * millisecondsPerTick.get() * ticks);
    }
    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

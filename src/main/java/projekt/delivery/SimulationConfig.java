package projekt.delivery;

import java.util.concurrent.atomic.AtomicInteger;

public class SimulationConfig {
    private final AtomicInteger millisecondsPerTick;
    private volatile boolean paused = false;
    private final Object lock = new Object();

    public SimulationConfig(int millisecondsPerTick) {
        this.millisecondsPerTick = new AtomicInteger(millisecondsPerTick);
    }

    public int getMillisecondsPerTick() {
        return millisecondsPerTick.get();
    }

    public void setMillisecondsPerTick(int millisecondsPerTick) {
        this.millisecondsPerTick.set(millisecondsPerTick);
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        lock.notify();
    }

    public Object getLock() {
        return lock;
    }
}

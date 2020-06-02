package me.ilnicki.bg.core.util.tick;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class UpdateSpeedTester {
    public final static TimeSupplier SYSTEM_TIME = System::nanoTime;

    private final SortedSet<Long> updateTimestamps = new TreeSet<>();
    private final TimeSupplier timeSupplier;
    private final long period;

    private final Set<SpeedConsumer> listeners = new HashSet<>();
    private int speed = 0;

    public UpdateSpeedTester(TimeSupplier timeSupplier, long period) {
        this.timeSupplier = timeSupplier;
        this.period = period;
    }

    public void update() {
        updateTimestamps.add(timeSupplier.get());

        if (updateTimestamps.first() < timeSupplier.get() - period) {
            setSpeed(updateTimestamps.size());
            updateTimestamps.clear();
        }
    }

    public int getSpeed() {
        return speed;
    }

    private void setSpeed(int speed) {
        this.speed = speed;

        listeners.forEach(lst -> lst.accept(speed));
    }

    public void addListener(SpeedConsumer listener) {
        listeners.add(listener);
    }

    public void removeListener(SpeedConsumer listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public interface TimeSupplier extends Supplier<Long> {
    }

    public interface SpeedConsumer extends Consumer<Integer> {
    }
}

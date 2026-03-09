package carwash;

import java.util.Objects;

/**
 * Immutable event data sent to observers on each simulation update.
 */
public class EventSnapshot {
    private final String eventName;
    private final int carId;
    private final String machine;

    public EventSnapshot(String eventName, int carId, String machine) {
        if (carId < -1) {
            throw new IllegalArgumentException("carId must be >= -1.");
        }
        this.eventName = Objects.requireNonNull(eventName, "eventName must not be null.");
        this.carId = carId;
        this.machine = Objects.requireNonNull(machine, "machine must not be null.");
    }

    public String getEventName() {
        return eventName;
    }

    public int getCarId() {
        return carId;
    }

    public String getMachine() {
        return machine;
    }
}

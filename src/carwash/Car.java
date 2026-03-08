package carwash;

/**
 * Represents one car in the simulation.
 */
public class Car {
    private final int id;
    private double queuedAt;

    public Car(int id) {
        this.id = id;
        this.queuedAt = -1;
    }

    public int getId() {
        return id;
    }

    public void setQueuedAt(double queuedAt) {
        this.queuedAt = queuedAt;
    }

    public double getQueuedAt() {
        return queuedAt;
    }
}
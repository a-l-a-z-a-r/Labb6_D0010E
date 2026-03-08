package carwash;

/**
 * Car entity with a unique id.
 */
public class Car {
    private final int id;

    public Car(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return Integer.toString(id);
    }
}

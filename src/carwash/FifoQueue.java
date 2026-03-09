package carwash;

import java.util.ArrayList;

/**
 * Simple FIFO list of cars waiting for service.
 */
public class FifoQueue {
    private final ArrayList<Car> queue = new ArrayList<>();

    public void add(Car c) {
        queue.add(c);
    }

    public Car remove() {
        return queue.remove(0);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}

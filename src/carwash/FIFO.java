package carwash;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * FIFO queue of cars waiting for service.
 */
public class FIFO {
    private final Queue<Car> queue = new ArrayDeque<>();

    public void add(Car c) {
        queue.add(c);
    }

    public Car remove() {
        return queue.remove();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}

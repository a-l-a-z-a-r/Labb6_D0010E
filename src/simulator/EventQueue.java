package simulator;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Priority queue for future events.
 */
public class EventQueue {
    private final PriorityQueue<Event> queue =
            new PriorityQueue<>(Comparator.comparingDouble(Event::getTime));

    public void add(Event e) {
        queue.add(e);
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Event getNext() {
        return queue.poll();
    }
}

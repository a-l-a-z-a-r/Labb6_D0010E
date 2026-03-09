package simulator;

import java.util.ArrayList;

/**
 * Simple event list where getNext() returns the earliest event.
 */
public class EventQueue {
    private final ArrayList<Event> events = new ArrayList<>();

    public void add(Event e) {
        events.add(e);
    }

    public boolean isEmpty() {
        return events.isEmpty();
    }

    public Event getNext() {
        if (events.isEmpty()) {
            return null;
        }

        int earliestIndex = 0;
        double earliestTime = events.get(0).getTime();

        for (int i = 1; i < events.size(); i++) {
            double time = events.get(i).getTime();
            if (time < earliestTime) {
                earliestTime = time;
                earliestIndex = i;
            }
        }

        return events.remove(earliestIndex);
    }
}

package simulator;

/**
 * Generic discrete event-driven simulator.
 */
public class Simulator {
    private final EventQueue queue;
    private final State state;
    private double currentTime;

    public Simulator(EventQueue queue, State state) {
        this.queue = queue;
        this.state = state;
    }

    public void schedule(Event event) {
        queue.add(event);
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void run() {
        while (state.isRunning() && !queue.isEmpty()) {
            Event event = queue.getNext();
            currentTime = event.getTime();
            event.execute(this, state);
        }
    }
}

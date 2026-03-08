package simulator;

/**
 * Generic start event. Subclasses may schedule initial domain events.
 */
public class StartEvent extends Event {
    public StartEvent(double time) {
        super(time);
    }

    @Override
    public void execute(Simulator sim, State state) {
        // Intentionally empty in the generic simulator.
    }
}

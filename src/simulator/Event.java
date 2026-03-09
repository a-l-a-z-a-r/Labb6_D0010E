package simulator;

/**
 * Base type for all events in the simulator.
 */
public abstract class Event {
    private final double time;

    protected Event(double time) {
        if (!Double.isFinite(time) || time < 0) {
            throw new IllegalArgumentException("time must be finite and >= 0.");
        }
        this.time = time;
    }

    public final double getTime() {
        return time;
    }

    public abstract void execute(Simulator sim, State state);
}
	

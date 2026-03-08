package simulator;

/**
 * Stop event that ends the simulation immediately.
 */
public class StopEvent extends Event {
    public StopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(Simulator sim, State state) {
        state.setRunning(false);
    }
}

package carwash;

import simulator.Simulator;
import simulator.State;

/**
 * Leave event for a car that completed washing.
 */
public class LeaveEvent extends CarWashEvent {
    private final boolean fastMachine;

    public LeaveEvent(double time, Car car, boolean fastMachine) {
        super(time, car);
        this.fastMachine = fastMachine;
    }

    @Override
    public void execute(Simulator sim, State genericState) {
        CarWashState state = (CarWashState) genericState;
        state.advanceTime(getTime());

        String machine = fastMachine ? "Fast" : "Slow";

        if (state.getQueue().isEmpty()) {
            if (fastMachine) {
                state.increaseFast();
            } else {
                state.increaseSlow();
            }
        } else {
            Car next = state.getQueue().remove();
            double service = fastMachine ? state.nextFastServiceTime() : state.nextSlowServiceTime();
            sim.schedule(new LeaveEvent(getTime() + service, next, fastMachine));
            state.dequeueQueueTime(next);
        }

        state.setEventSnapshot("Leave", car.getId(), machine);
        state.notifyObservers();
    }
}

package carwash;

import java.util.Locale;

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
        String note = "machine becomes free";

        if (state.getQueue().isEmpty()) {
            if (fastMachine) {
                state.increaseFast();
            } else {
                state.increaseSlow();
            }
        } else {
            Car next = state.getQueue().remove();
            double queuedFor = state.dequeueQueueTime(next);
            double service = fastMachine ? state.nextFastServiceTime() : state.nextSlowServiceTime();
            sim.schedule(new LeaveEvent(getTime() + service, next, fastMachine));
            note = String.format(
                    Locale.US,
                    "next car %d from queue, waited %.2f, service %.2f",
                    next.getId(),
                    queuedFor,
                    service);
        }

        state.setEventSnapshot("Leave", car.getId(), machine, note);
        state.notifyObservers();
    }
}

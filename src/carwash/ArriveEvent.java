package carwash;

import java.util.Locale;

import simulator.Simulator;
import simulator.State;

/**
 * Arrival event for a car.
 */
public class ArriveEvent extends CarWashEvent {
    public ArriveEvent(double time, Car car) {
        super(time, car);
    }

    @Override
    public void execute(Simulator sim, State genericState) {
        CarWashState state = (CarWashState) genericState;
        state.advanceTime(getTime());

        String note;
        String machine = "-";

        if (state.getFreeFast() > 0) {
            state.decreaseFast();
            state.countEnteredCar();
            double service = state.nextFastServiceTime();
            sim.schedule(new LeaveEvent(getTime() + service, car, true));
            machine = "Fast";
            note = String.format(Locale.US, "direct, service %.2f", service);
        } else if (state.getFreeSlow() > 0) {
            state.decreaseSlow();
            state.countEnteredCar();
            double service = state.nextSlowServiceTime();
            sim.schedule(new LeaveEvent(getTime() + service, car, false));
            machine = "Slow";
            note = String.format(Locale.US, "direct, service %.2f", service);
        } else if (state.canQueueMore()) {
            state.countEnteredCar();
            state.getQueue().add(car);
            state.markQueued(car);
            note = "queued";
        } else {
            state.rejectCar();
            note = "rejected";
        }

        Car nextCar = new Car(state.nextCarId());
        sim.schedule(new ArriveEvent(getTime() + state.nextArrivalDelta(), nextCar));

        state.setEventSnapshot("Arrive", car.getId(), machine, note);
        state.notifyObservers();
    }
}

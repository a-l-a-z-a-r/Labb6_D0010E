package carwash;

import simulator.Simulator;
import simulator.State;
import simulator.StopEvent;

/**
 * Stop event specialized for ending the car wash simulation.
 */
public class CarWashStopEvent extends StopEvent {
    public CarWashStopEvent(double time) {
        super(time);
    }

    @Override
    public void execute(Simulator sim, State state) {
        CarWashState carWashState = (CarWashState) state;
        carWashState.advanceTime(getTime());
        carWashState.publishUpdate(new EventSnapshot("Stop", -1, "-"));
        carWashState.setRunning(false);
    }
}

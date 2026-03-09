package carwash;

import simulator.Simulator;
import simulator.StartEvent;
import simulator.State;

/**
 * Start event specialized for initializing the car wash simulation.
 */
public class CarWashStartEvent extends StartEvent {
    public CarWashStartEvent(double time) {
        super(time);
    }

    @Override
    public void execute(Simulator sim, State state) {
        CarWashState carWashState = (CarWashState) state;
        carWashState.advanceTime(getTime());
        carWashState.publishUpdate(new EventSnapshot("Start", -1, "-"));

        Car firstCar = new Car(carWashState.nextCarId());
        sim.schedule(new ArriveEvent(getTime() + carWashState.cariscomingnow(), firstCar));
    }
}

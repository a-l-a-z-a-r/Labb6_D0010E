package carwash;

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
    public void execute(Simulator sim, State state) {
        CarWashState carWashState = (CarWashState) state;
        carWashState.advanceTime(getTime());

        MachineType machine = MachineType.NONE;

        if (carWashState.getFreeFast() > 0) {
            carWashState.decreaseFast();
            carWashState.countEnteredCar();
            double service = carWashState.nextFastServiceTime();
            sim.schedule(new LeaveEvent(getTime() + service, car, true));
            machine = MachineType.FAST;
        } else if (carWashState.getFreeSlow() > 0) {
            carWashState.decreaseSlow();
            carWashState.countEnteredCar();
            double service = carWashState.nextSlowServiceTime();
            sim.schedule(new LeaveEvent(getTime() + service, car, false));
            machine = MachineType.SLOW;
        } else if (carWashState.canQueueMore()) {
            carWashState.countEnteredCar();
            carWashState.getQueue().add(car);
            carWashState.markQueued(car);
        } else {
            carWashState.rejectCar();
        }

        Car nextCar = new Car(carWashState.nextCarId());
        sim.schedule(new ArriveEvent(getTime() + carWashState.cariscomingnow(), nextCar));

        carWashState.publishUpdate(new EventSnapshot("Arrive", car.getId(), machine));
    }
}

package carwash;

import simulator.Event;

/**
 * Base type for car wash events involving a car.
 */
public abstract class CarWashEvent extends Event {
    protected final Car car;

    protected CarWashEvent(double time, Car car) {
        super(time);
        this.car = car;
    }

    public Car getCar() {
        return car;
    }
}

package event;

public class Arrive extends carwash.ArriveEvent {
    public Arrive(double time, car.Car car) {
        super(time, car);
    }
}

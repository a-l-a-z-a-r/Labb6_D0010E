package Event;

public class Arrive extends carwash.ArriveEvent {
	public Arrive(double time, Car.Car car) {
		super(time, car);
	}
}

package labb6.Event;

public class Arrive extends carwash.ArriveEvent {
	public Arrive(double time, labb6.Car.Car car) {
		super(time, car);
	}
}

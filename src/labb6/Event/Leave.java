package labb6.Event;

public class Leave extends carwash.LeaveEvent {
	public Leave(double time, labb6.Car.Car car, boolean fastMachine) {
		super(time, car, fastMachine);
	}
}

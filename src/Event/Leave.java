package Event;

public class Leave extends carwash.LeaveEvent {
	public Leave(double time, Car.Car car, boolean fastMachine) {
		super(time, car, fastMachine);
	}
}

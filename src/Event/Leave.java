package event;

public class Leave extends carwash.LeaveEvent {
    public Leave(double time, car.Car car, boolean fastMachine) {
        super(time, car, fastMachine);
    }
}

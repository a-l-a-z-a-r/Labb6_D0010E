package carwash;

import simulator.Simulator;
import simulator.State;

/**
 * Leave event for a car that completed washing.
 */
public class LeaveEvent extends CarWashEvent {
    private final boolean fastMachine;

    public LeaveEvent(double time, Car car, boolean fastMachine) {
        super(time, car);
        this.fastMachine = fastMachine;
    }

    @Override
    public void execute(Simulator sim, State state) {
        CarWashState carWashState = (CarWashState) state;
        carWashState.advanceTime(getTime());

                                                                                                                                                                                       
        MachineType machine;
        if (fastMachine) {
            machine = MachineType.FAST;
        } else {
            machine = MachineType.SLOW;
        }

        if (carWashState.getQueue().isEmpty()) {
            if (fastMachine) {
                carWashState.increaseFast();
            } else {
                carWashState.increaseSlow();
            }
        } else {
            Car next = carWashState.getQueue().remove();
             double service;                                                                                                                                                               
                if (fastMachine) {                                                                                                                                                            
                    service = carWashState.nextFastServiceTime();                                                                                                                             
                } else {                                                                                                                                                                      
                    service = carWashState.nextSlowServiceTime();                                                                                                                             
                }                                                                                                                                                                             
 
            sim.schedule(new LeaveEvent(getTime() + service, next, fastMachine));
            carWashState.dequeueQueueTime(next);
        }

        carWashState.publishUpdate(new EventSnapshot("Leave", car.getId(), machine));
    }
}

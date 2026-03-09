package carwash;

import random.ExponentialRandomStream;
import random.UniformRandomStream;
import simulator.State;

/**
 * Car wash specific simulation state.
 */
public class CarWashState extends State {
    private double currentTime;
    private final int totalFast;
    private final int totalSlow;
    private int freeFast;
    private int freeSlow;
    private final int maxQueueSize;
    private final FifoQueue queue = new FifoQueue();
    private final UniformRandomStream fastServiceStream;
    private final UniformRandomStream slowServiceStream;
    private final ExponentialRandomStream arrivalStream;
    private int nextCarId;
    private int rejectedCars;
    private int enteredCars;
    private double totalQueueTime;
    private double totalIdleTime;

    private String eventName = "";
    private int eventCarId = -1;
    private String eventMachine = "-";

    public CarWashState(
            int fastMachines,
            int slowMachines,
            int maxQueueSize,
            ExponentialRandomStream arrivalStream,
            UniformRandomStream fastServiceStream,
            UniformRandomStream slowServiceStream) {
        this.totalFast = fastMachines;
        this.totalSlow = slowMachines;
        this.freeFast = fastMachines;
        this.freeSlow = slowMachines;
        this.maxQueueSize = maxQueueSize;
        this.arrivalStream = arrivalStream;
        this.fastServiceStream = fastServiceStream;
        this.slowServiceStream = slowServiceStream;
    }

    public void setCurrentTime(double t) {
        currentTime = t;
    }

    public double getCurrentTime() {
        return currentTime;
    }

    public void advanceTime(double newTime) {
        double dt = newTime - currentTime;
        if (dt > 0) {
            totalIdleTime += dt * (freeFast + freeSlow);
            totalQueueTime += dt * queue.size();
        }
        currentTime = newTime;
    }

    public int getFreeFast() {
        return freeFast;
    }

    public int getFreeSlow() {
        return freeSlow;
    }

    public void decreaseFast() {
        freeFast--;
    }

    public void increaseFast() {
        freeFast++;
    }

    public void decreaseSlow() {
        freeSlow--;
    }

    public void increaseSlow() {
        freeSlow++;
    }

    public FifoQueue getQueue() {
        return queue;
    }

    public boolean canQueueMore() {
        return queue.size() < maxQueueSize;
    }

    public void markQueued(Car car) {
        car.setQueuedAt(currentTime);
    }

    public double dequeueQueueTime(Car car) {
        return currentTime - car.getQueuedAt();
    }

    public void rejectCar() {
        rejectedCars++;
    }

    public void addQueueTime(double t) {
        totalQueueTime += t;
    }

    public void addIdleTime(double t) {
        totalIdleTime += t;
    }

    public double nextArrivalDelta() {
        return arrivalStream.next();
    }

    public double nextFastServiceTime() {
        return fastServiceStream.next();
    }

    public double nextSlowServiceTime() {
        return slowServiceStream.next();
    }

    public int nextCarId() {
        return nextCarId++;
    }

    public void countEnteredCar() {
        enteredCars++;
    }

    public int getEnteredCars() {
        return enteredCars;
    }

    public int getRejectedCars() {
        return rejectedCars;
    }

    public double getTotalQueueTime() {
        return totalQueueTime;
    }

    public double getTotalIdleTime() {
        return totalIdleTime;
    }

    public int getQueueSize() {
        return queue.size();
    }

    public int getTotalFast() {
        return totalFast;
    }

    public int getTotalSlow() {
        return totalSlow;
    }

    public int getMaxQueueSize() {
        return maxQueueSize;
    }

    public void setEventSnapshot(String eventName, int carId, String machine) {
        this.eventName = eventName;
        this.eventCarId = carId;
        this.eventMachine = machine;
    }

    public String getEventName() {
        return eventName;
    }

    public int getEventCarId() {
        return eventCarId;
    }

    public String getEventMachine() {
        return eventMachine;
    }
}

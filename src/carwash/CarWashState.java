package carwash;

import java.util.Objects;

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

    public CarWashState(int fastMachines, int slowMachines, int maxQueueSize, ExponentialRandomStream arrivalStream,
            UniformRandomStream fastServiceStream, UniformRandomStream slowServiceStream) {
        if (fastMachines < 0 || slowMachines < 0 || maxQueueSize < 0) {
            throw new IllegalArgumentException("Machine counts and maxQueueSize must be >= 0.");
        }
        this.totalFast = fastMachines;
        this.totalSlow = slowMachines;
        this.freeFast = fastMachines;
        this.freeSlow = slowMachines;
        this.maxQueueSize = maxQueueSize;
        this.arrivalStream = Objects.requireNonNull(arrivalStream, "arrivalStream must not be null.");
        this.fastServiceStream = Objects.requireNonNull(fastServiceStream, "fastServiceStream must not be null.");
        this.slowServiceStream = Objects.requireNonNull(slowServiceStream, "slowServiceStream must not be null.");
    }

    double getCurrentTime() {
        return currentTime;
    }

    void advanceTime(double newTime) {
        if (!Double.isFinite(newTime) || newTime < 0) {
            throw new IllegalArgumentException("newTime must be finite and >= 0.");
        }
        if (newTime < currentTime) {
            throw new IllegalArgumentException("Time cannot move backwards.");
        }

        double lastTimeSession = newTime - currentTime;
        totalIdleTime += lastTimeSession * (freeFast + freeSlow);
        totalQueueTime += lastTimeSession * queue.size();
        currentTime = newTime;
    }

    int getFreeFast() {
        return freeFast;
    }

    int getFreeSlow() {
        return freeSlow;
    }

    void decreaseFast() {
        if (freeFast <= 0) {
            throw new IllegalStateException("No free fast machines to allocate.");
        }
        freeFast--;
    }

    void increaseFast() {
        if (freeFast >= totalFast) {
            throw new IllegalStateException("Fast machine count cannot exceed total.");
        }
        freeFast++;
    }

    void decreaseSlow() {
        if (freeSlow <= 0) {
            throw new IllegalStateException("No free slow machines to allocate.");
        }
        freeSlow--;
    }

    void increaseSlow() {
        if (freeSlow >= totalSlow) {
            throw new IllegalStateException("Slow machine count cannot exceed total.");
        }
        freeSlow++;
    }

    FifoQueue getQueue() {
        return queue;
    }

    boolean canQueueMore() {
        return queue.size() < maxQueueSize;
    }

    void markQueued(Car car) {
        Objects.requireNonNull(car, "car must not be null.");
        car.setQueuedAt(currentTime);
    }

    double dequeueQueueTime(Car car) {
        Objects.requireNonNull(car, "car must not be null.");
        return currentTime - car.getQueuedAt();
    }

    void rejectCar() {
        rejectedCars++;
    }

    double cariscomingnow() {
        return arrivalStream.next();
    }

    double nextFastServiceTime() {
        return fastServiceStream.next();
    }

    double nextSlowServiceTime() {
        return slowServiceStream.next();
    }

    int nextCarId() {
        return nextCarId++;
    }

    void countEnteredCar() {
        enteredCars++;
    }

    int getEnteredCars() {
        return enteredCars;
    }

    int getRejectedCars() {
        return rejectedCars;
    }

    double getTotalQueueTime() {
        return totalQueueTime;
    }

    double getTotalIdleTime() {
        return totalIdleTime;
    }

    int getQueueSize() {
        return queue.size();
    }
}

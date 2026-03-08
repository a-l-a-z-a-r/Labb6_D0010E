package simulator;

import carwash.ArriveEvent;
import carwash.Car;
import carwash.CarWashState;
import carwash.StatusView;
import random.ExponentialRandomStream;
import random.UniformRandomStream;

/**
 * Runs the car wash simulation.
 */
public class MainSim {
    public static void main(String[] args) {
        int fastMachines = 2;
        int slowMachines = 2;
        int maxQueueSize = 5;
        double stopTime = 15.0;
        double lambda = 2.0;
        double fastMin = 2.8;
        double fastMax = 4.6;
        double slowMin = 3.5;
        double slowMax = 6.7;
        long seed = 12348;

        ExponentialRandomStream arrivals = new ExponentialRandomStream(lambda, seed);
        UniformRandomStream fastService = new UniformRandomStream(fastMin, fastMax, seed);
        UniformRandomStream slowService = new UniformRandomStream(slowMin, slowMax, seed);

        CarWashState state = new CarWashState(
                fastMachines,
                slowMachines,
                maxQueueSize,
                arrivals,
                fastService,
                slowService);

        EventQueue queue = new EventQueue();
        Simulator sim = new Simulator(queue, state);
        StatusView view = new StatusView(state);
        state.addObserver(view);

        System.out.printf("Fast machines: %d%n", fastMachines);
        System.out.printf("Slow machines: %d%n", slowMachines);
        System.out.printf("Fast distribution: (%.1f, %.1f)%n", fastMin, fastMax);
        System.out.printf("Slow distribution: (%.1f, %.1f)%n", slowMin, slowMax);
        System.out.printf("Exponential distribution with lambda = %.1f%n", lambda);
        System.out.printf("seed = %d%n", seed);
        System.out.printf("Max Queue size: %d%n", maxQueueSize);
        view.printHeader();

        sim.schedule(new CarWashStartEvent(0.0));
        sim.schedule(new CarWashStopEvent(stopTime));

        sim.run();
        view.printSummary();
    }

    private static final class CarWashStartEvent extends StartEvent {
        private CarWashStartEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulator sim, State genericState) {
            CarWashState state = (CarWashState) genericState;
            state.advanceTime(getTime());
            state.setEventSnapshot("Start", -1, "-", "");
            state.notifyObservers();

            Car firstCar = new Car(state.nextCarId());
            sim.schedule(new ArriveEvent(getTime() + state.nextArrivalDelta(), firstCar));
        }
    }

    private static final class CarWashStopEvent extends StopEvent {
        private CarWashStopEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulator sim, State genericState) {
            CarWashState state = (CarWashState) genericState;
            state.advanceTime(getTime());
            state.setEventSnapshot("Stop", -1, "-", "");
            state.notifyObservers();
            super.execute(sim, genericState);
        }
    }
}

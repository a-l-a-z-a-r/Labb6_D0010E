package labb6;

import carwash.ArriveEvent;
import carwash.Car;
import carwash.CarWashState;
import carwash.StatusView;
import random.ExponentialRandomStream;
import random.UniformRandomStream;
import simulator.EventQueue;
import simulator.Simulator;
import simulator.StartEvent;
import simulator.State;
import simulator.StopEvent;

/**
 * Runs the car wash simulation.
 */
public class StartCarWashSim {
    public static void run() {
        int fastMachines = 2;
        int slowMachines = 2;
        int maxQueueSize = 5;
        double stopTime = 15.0;
        double lambda = 2.0;
        double fastMin = 2.8;
        double fastMax = 4.6;
        double slowMin = 3.5;
        double slowMax = 6.7;
        long seed = 1234;

        ExponentialRandomStream arrivals = new ExponentialRandomStream(lambda, seed);
        UniformRandomStream fastService = new UniformRandomStream(fastMin, fastMax, seed);
        UniformRandomStream slowService = new UniformRandomStream(slowMin, slowMax, seed);

        CarWashState state = new CarWashState(fastMachines,slowMachines,maxQueueSize,arrivals,fastService,slowService);

        EventQueue queue = new EventQueue();
        Simulator sim = new Simulator(queue, state);
        StatusView view = new StatusView(state);
        state.addObserver(view);

        String fastRange = "(" + fastMin + ", " + fastMax + ")";
        String slowRange = "(" + slowMin + ", " + slowMax + ")";
        String lambdaText = lambda + "";

        System.out.println("Fast machines: " + fastMachines);
        System.out.println("Slow machines: " + slowMachines);
        System.out.println("Fast distribution: " + fastRange);
        System.out.println("Slow distribution: " + slowRange);
        System.out.println("Exponential distribution with lambda = " + lambdaText);
        System.out.println("seed = " + seed);
        System.out.println("Max Queue size: " + maxQueueSize);
        view.printHeader();

        sim.schedule(new CarWashStartEvent(0.0));
        sim.schedule(new CarWashStopEvent(stopTime));

        sim.run();
        view.printSummary();
    }

    /**
     * Start event specialized for initializing the car wash simulation.
     */
    private static final class CarWashStartEvent extends StartEvent {
        private CarWashStartEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulator sim, State state) {
            CarWashState carWashState = (CarWashState) state;
            carWashState.advanceTime(getTime());
            carWashState.setEventSnapshot("Start", -1, "-");
            carWashState.publishUpdate();

            Car firstCar = new Car(carWashState.nextCarId());
            sim.schedule(new ArriveEvent(getTime() + carWashState.cariscomingnow(), firstCar));
        }
    }

    /**
     * Stop event specialized for ending the car wash simulation.
     */
    private static final class CarWashStopEvent extends StopEvent {
        private CarWashStopEvent(double time) {
            super(time);
        }

        @Override
        public void execute(Simulator sim, State state) {
            CarWashState carWashState = (CarWashState) state;
            carWashState.advanceTime(getTime());
            carWashState.setEventSnapshot("Stop", -1, "-");
            carWashState.publishUpdate();
            carWashState.setRunning(false);
        }
    }


}

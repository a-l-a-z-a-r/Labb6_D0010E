package labb6;

import carwash.CarWashState;
import carwash.CarWashStartEvent;
import carwash.CarWashStopEvent;
import carwash.StatusView;
import random.ExponentialRandomStream;
import random.UniformRandomStream;
import simulator.EventQueue;
import simulator.Simulator;

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
}

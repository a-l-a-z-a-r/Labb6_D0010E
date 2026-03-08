package carwash;

import java.util.Formatter;
import java.util.Locale;

import simulator.Observer;

/**
 * Console view of simulation events and cumulative statistics.
 */
public class StatusView implements Observer {
    private final CarWashState state;
    private final Formatter out;

    public StatusView(CarWashState state) {
        this.state = state;
        this.out = new Formatter(System.out, Locale.US);
    }
    @Override
    public void update() {
        String carText = state.getEventCarId() >= 0 ? Integer.toString(state.getEventCarId()) : "-";
        out.format(
                "%7.2f  %-7s  %4s  %7s  %4d  %4d  %8.2f  %9.2f  %9d  %8d  %s%n",
                state.getCurrentTime(),
                state.getEventName(),
                carText,
                state.getEventMachine(),
                state.getFreeFast(),
                state.getFreeSlow(),
                state.getTotalIdleTime(),
                state.getTotalQueueTime(),
                state.getQueueSize(),
                state.getRejectedCars(),
                state.getEventNote());
        out.flush();
    }

    public void printHeader() {
        out.format("--------------------------------------------------------------%n");
        out.format("   Time  Event      Id  Machine  Fast  Slow  IdleTime  QueueTime  QueueSize  Rejected  Note%n");
        out.format("--------------------------------------------------------------%n");
        out.flush();
    }

    public void printSummary() {
        double meanQueue = state.getEnteredCars() == 0 ? 0.0 : state.getTotalQueueTime() / state.getEnteredCars();
        out.format("--------------------------------------------------------------%n");
        out.format("Total idle machine time: %.2f%n", state.getTotalIdleTime());
        out.format("Total queueing time:     %.2f%n", state.getTotalQueueTime());
        out.format("Mean queueing time:      %.2f%n", meanQueue);
        out.format("Rejected cars:           %d%n", state.getRejectedCars());
        out.flush();
    }
}

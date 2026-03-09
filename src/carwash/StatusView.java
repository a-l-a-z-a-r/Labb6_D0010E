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
        String timeValue = alignLeft(formatNumber(state.getCurrentTime()), 7);
        String eventName = alignLeft(state.getEventName(), 7);
        String carId = alignRight(carText, 4);
        String machine = alignLeft(state.getEventMachine(), 7);
        String freeFast = alignRight(Integer.toString(state.getFreeFast()), 4);
        String freeSlow = alignRight(Integer.toString(state.getFreeSlow()), 4);
        String idleTime = alignRight(formatNumber(state.getTotalIdleTime()), 8);
        String queueTime = alignRight(formatNumber(state.getTotalQueueTime()), 9);
        String queueSize = alignRight(Integer.toString(state.getQueueSize()), 9);
        String rejected = alignRight(Integer.toString(state.getRejectedCars()), 8);

        String line = timeValue + "  " + eventName + "  " + carId + "  " + machine + "  " + freeFast + "  " + freeSlow
                + "  " + idleTime + "  " + queueTime + "  " + queueSize + "  " + rejected;
        out.format(line + System.lineSeparator());
        out.flush();
    }

    public void printHeader() {
        out.format("--------------------------------------------------------------" + System.lineSeparator());
        out.format("   Time  Event      Id  Machine  Fast  Slow  IdleTime  QueueTime  QueueSize  Rejected"
                + System.lineSeparator());
        out.format("--------------------------------------------------------------" + System.lineSeparator());
        out.flush();
    }

    public void printSummary() {
        double meanQueue = state.getEnteredCars() == 0 ? 0.0 : state.getTotalQueueTime() / state.getEnteredCars();
        out.format("--------------------------------------------------------------" + System.lineSeparator());
        out.format("Total idle machine time: " + formatNumber(state.getTotalIdleTime()) + System.lineSeparator());
        out.format("Total queueing time:     " + formatNumber(state.getTotalQueueTime()) + System.lineSeparator());
        out.format("Mean queueing time:      " + formatNumber(meanQueue) + System.lineSeparator());
        out.format("Rejected cars:           " + state.getRejectedCars() + System.lineSeparator());
        out.flush();
    }

    private String formatNumber(double value) {
        return String.format(Locale.US, "%.2f", value);
    }

    private String alignLeft(String value, int width) {
        if (value.length() >= width) {
            return value;
        }
        return value + " ".repeat(width - value.length());
    }

    private String alignRight(String value, int width) {
        if (value.length() >= width) {
            return value;
        }
        return " ".repeat(width - value.length()) + value;
    }
}

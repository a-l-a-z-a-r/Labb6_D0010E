package carwash;

import java.util.Observable;
import java.util.Observer;

/**
 * Console view of simulation events and cumulative statistics.
 */
public class StatusView implements Observer {
    private final CarWashState state;

    public StatusView(CarWashState state) {
        this.state = state;
    }

    @Override
    public void update(Observable o, Object arg) {
          String carText;                                                                                                                                                               
            if (state.getEventCarId() >= 0) {                                                                                                                                             
                carText = Integer.toString(state.getEventCarId());                                                                                                                        
            } else {                                                                                                                                                                      
                carText = "-";                                                                                                                                                            
            }                    
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
        System.out.println(line);
    }

    public void printHeader() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("   Time  Event      Id  Machine  Fast  Slow  IdleTime  QueueTime  QueueSize  Rejected");
        System.out.println("--------------------------------------------------------------");
    }

    public void printSummary() {
        double meanQueue = state.getEnteredCars() == 0 ? 0.0 : state.getTotalQueueTime() / state.getEnteredCars();
        System.out.println("--------------------------------------------------------------");
        System.out.println("Total idle machine time: " + formatNumber(state.getTotalIdleTime()));
        System.out.println("Total queueing time:     " + formatNumber(state.getTotalQueueTime()));
        System.out.println("Mean queueing time:      " + formatNumber(meanQueue));
        System.out.println("Rejected cars:           " + state.getRejectedCars());
    }

    private String formatNumber(double value) {
        return String.format("%.2f", value);
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

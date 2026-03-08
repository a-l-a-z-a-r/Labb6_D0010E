package simulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared simulation state.
 */
public class State {
    private boolean running = true;
    private final List<Observer> observers = new ArrayList<>();

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void setRunning(boolean r) {
        running = r;
    }

    public boolean isRunning() {
        return running;
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }
}

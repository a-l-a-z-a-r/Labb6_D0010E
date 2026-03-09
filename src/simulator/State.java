package simulator;

import java.util.Observable;

/**
 * Shared simulation state.
 */
public class State extends Observable {
    private boolean running = true;

    public void setRunning(boolean r) {
        running = r;
    }

    public boolean isRunning() {
        return running;
    }

    public void publishUpdate(Object arg) {
        setChanged();
        notifyObservers(arg);
    }
}

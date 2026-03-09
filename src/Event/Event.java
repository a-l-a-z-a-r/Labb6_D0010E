package event;

import simulator.Simulator;
import simulator.State;

public abstract class Event extends simulator.Event {
	protected Event(double time) {
		super(time);
	}

	@Override
	public abstract void execute(Simulator sim, State state);
}

package car;

public class CarMachine {
    public enum Type {
        FAST,
        SLOW
    }

    private final Type type;

    public CarMachine(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}

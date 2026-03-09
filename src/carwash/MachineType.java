package carwash;

/**
 * Machine category used in event output.
 */
public enum MachineType {
    FAST("Fast"),
    SLOW("Slow"),
    NONE("-");

    private final String label;

    MachineType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

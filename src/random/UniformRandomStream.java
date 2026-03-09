package random;

import java.util.Random;

/**
 * Random number stream with uniformly distributed samples in a range.
 */
public class UniformRandomStream {
    private final Random rand;
    private final double lower;
    private final double width;

    public UniformRandomStream(double lower, double upper, long seed) {
        validateBounds(lower, upper);
        this.rand = new Random(seed);
        this.lower = lower;
        this.width = upper - lower;
    }

    public UniformRandomStream(double lower, double upper) {
        validateBounds(lower, upper);
        this.rand = new Random();
        this.lower = lower;
        this.width = upper - lower;
    }

    public double next() {
        return lower + rand.nextDouble() * width;
    }

    private static void validateBounds(double lower, double upper) {
        if (!Double.isFinite(lower) || !Double.isFinite(upper)) {
            throw new IllegalArgumentException("lower and upper must be finite.");
        }
        if (lower < 0 || upper < 0) {
            throw new IllegalArgumentException("lower and upper must be >= 0.");
        }
        if (upper < lower) {
            throw new IllegalArgumentException("upper must be >= lower.");
        }
    }
}

package random;

import java.util.Random;

/**
 * Random number stream with exponentially distributed samples.
 */
public class ExponentialRandomStream {
    private final Random rand;
    private final double lambda;

    public ExponentialRandomStream(double lambda, long seed) {
        validateLambda(lambda);
        this.rand = new Random(seed);
        this.lambda = lambda;
    }

    public ExponentialRandomStream(double lambda) {
        validateLambda(lambda);
        this.rand = new Random();
        this.lambda = lambda;
    }

    public double next() {
        return -Math.log(rand.nextDouble()) / lambda;
    }

    private static void validateLambda(double lambda) {
        if (!Double.isFinite(lambda) || lambda <= 0) {
            throw new IllegalArgumentException("lambda must be finite and > 0.");
        }
    }
}

package me.etki.grac.policy;

import me.etki.grac.utility.MathUtilities;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class LinearBackoffRetryPolicy extends AbstractRetryPolicy {

    public static final long DEFAULT_STEP = 250;

    private final TimeRange range;

    public LinearBackoffRetryPolicy(
            TimeRange range,
            int maximumAttempts,
            boolean shouldRetryOnServerError,
            boolean shouldRetryOnClientError) {

        super(maximumAttempts, shouldRetryOnServerError, shouldRetryOnClientError);
        this.range = range;
    }

    public LinearBackoffRetryPolicy(
            TimeRange range,
            int maximumAttempts,
            boolean shouldRetryOnServerError) {

        super(maximumAttempts, shouldRetryOnServerError);
        this.range = range;
    }

    public LinearBackoffRetryPolicy(TimeRange range, int maximumAttempts) {
        super(maximumAttempts);
        this.range = range;
    }

    public LinearBackoffRetryPolicy(TimeRange range) {
        this.range = range;
    }

    public LinearBackoffRetryPolicy(long step, int maximumAttempts) {
        super(maximumAttempts);
        this.range = new TimeRange(step, step * (getMaximumAttempts() - 1));
    }

    @Override
    public long calculateDelay(int attempt) {
        int steps = getMaximumAttempts() - 2;
        long span = range.getMaximumDelay() - range.getMinimumDelay();
        long step = span / steps;
        long delay = MathUtilities.applyRandomFactor(range.getMinimumDelay(), range.getRandomFactor()) +
                ((attempt - 2) * MathUtilities.applyRandomFactor(step, range.getRandomFactor()));
        long minimum = MathUtilities.applyRandomFactor(range.getMinimumDelay(), range.getRandomFactor());
        long maximum = MathUtilities.applyRandomFactor(range.getMaximumDelay(), range.getRandomFactor());
        return MathUtilities.limit(delay, minimum, maximum);
    }
}

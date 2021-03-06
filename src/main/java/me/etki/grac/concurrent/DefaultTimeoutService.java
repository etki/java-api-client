package me.etki.grac.concurrent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Etki {@literal <etki@etki.name>}
 * @version %I%, %G%
 * @since 0.1.0
 */
public class DefaultTimeoutService implements TimeoutService {

    private final ScheduledExecutor executor;

    public DefaultTimeoutService(ScheduledExecutor executor) {
        this.executor = executor;
    }

    @Override
    public <T> CompletableFuture<Void> setTimeout(CompletableFuture<T> future, long delay, TimeUnit unit) {
        return executor.schedule(() -> {
            String message = "CompletableFuture has timed out after delay of " + delay + " " + unit.toString();
            future.completeExceptionally(new TimeoutException(message));
        }, delay, unit);
    }
}

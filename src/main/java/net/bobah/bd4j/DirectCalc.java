package net.bobah.bd4j;

import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Direct calculation simulator. Invokes callback asynchronously after 5 seconds.
 */
public final class DirectCalc implements ICalc {
    private static final Logger log = getLogger(DirectCalc.class);
    private final long delayMs;
    private final Executor asyncDelay;

    DirectCalc() {
        this(5_000L, newSingleThreadExecutor());
    }

    DirectCalc(long delayMs, Executor asyncDelay) {
        this.delayMs = delayMs;
        this.asyncDelay = asyncDelay;
    }

    @Override
    public Future<Integer> calculate(int key) {
        final CompletableFuture<Integer> future = new CompletableFuture<>();
        final int result = Integer.valueOf(new StringBuilder(String.valueOf(key)).reverse().toString());
        asyncDelay.execute(() -> {
            try {
                log.debug("calculating for {}...", key);
                sleep(delayMs);
                log.debug("calculated for {}...", key);
                future.complete(result);
            } catch (InterruptedException ex) {
                log.debug("interrupted for {}...", key);
                future.completeExceptionally(ex);
                currentThread().interrupt();
            }
        });
        return future;
    }
}

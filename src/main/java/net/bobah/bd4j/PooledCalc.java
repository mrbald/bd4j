package net.bobah.bd4j;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.stream.IntStream;

import static java.util.stream.IntStream.range;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A pool backed delegating calculator with round-robin load balancing.
 *
 * Alternative balancing policies
 * - sticky (on the hash code of the input % number of executors, etc.)
 * - LRU
 * - least loaded
 */
public final class PooledCalc implements ICalc {
    private static final Logger log = getLogger(PooledCalc.class);

    private static final int DELEGATE_POOL_SIZE = 5;
    private static final AtomicIntegerFieldUpdater<PooledCalc> nextDelegateIdxTracker
            = AtomicIntegerFieldUpdater.newUpdater(PooledCalc.class, "nextDelegateIdx");
    private volatile int nextDelegateIdx = 0;

    private final ICalc[] delegatesPool;

    @Inject
    PooledCalc(@Named("pool-worker") Provider<ICalc> delegateProvider) {
        this.delegatesPool = range(1, DELEGATE_POOL_SIZE)
                .mapToObj(i -> delegateProvider.get())
                .toArray(ICalc[]::new);
    }

    @Override
    public Future<Integer> calculate(int key) {
        log.debug("calculating for {}...", key);
        return getNextDelegate().calculate(key);
    }

    private ICalc getNextDelegate() {
        return delegatesPool[nextDelegateIdxTracker.getAndIncrement(this) % DELEGATE_POOL_SIZE];
    }
}

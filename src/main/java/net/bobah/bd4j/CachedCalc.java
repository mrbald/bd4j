package net.bobah.bd4j;

import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * A cache backed delegating calculator with round-robin load balancing.
 */
public final class CachedCalc implements ICalc {
    private static final Logger log = getLogger(CachedCalc.class);

    private final ICalc delegate;
    private final ConcurrentMap<Integer, Future<Integer>> cache = new ConcurrentHashMap<>();

    @Inject
    CachedCalc(@Named("cache-loader") ICalc delegate) {
        this.delegate = delegate;
    }

    @Override
    public Future<Integer> calculate(int key) {
        log.debug("calculating for {}...", key);
        return cache.computeIfAbsent(key, delegate::calculate);
    }
}

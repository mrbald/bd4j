package net.bobah.bd4j;

import com.google.inject.AbstractModule;

import java.util.concurrent.ScheduledExecutorService;

import static com.google.inject.name.Names.named;
import static java.util.concurrent.Executors.newScheduledThreadPool;

public final class WithCachedCalc extends AbstractModule {
    @Override
    protected void configure() {
        bind(ICalc.class)
                .annotatedWith(named("cache-loader"))
                .to(DirectCalc.class);

        bind(ICalc.class)
                .annotatedWith(named("frontend"))
                .to(CachedCalc.class);
    }
}

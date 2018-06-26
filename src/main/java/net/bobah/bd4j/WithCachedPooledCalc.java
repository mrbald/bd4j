package net.bobah.bd4j;

import com.google.inject.AbstractModule;

import static com.google.inject.name.Names.named;

public final class WithCachedPooledCalc extends AbstractModule {
    @Override
    protected void configure() {
        bind(ICalc.class)
                .annotatedWith(named("pool-worker"))
                .to(DirectCalc.class);

        bind(ICalc.class)
                .annotatedWith(named("cache-loader"))
                .to(PooledCalc.class);

        bind(ICalc.class)
                .annotatedWith(named("frontend"))
                .to(CachedCalc.class);
    }
}

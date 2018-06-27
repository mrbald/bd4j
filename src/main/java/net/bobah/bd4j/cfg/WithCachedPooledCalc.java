package net.bobah.bd4j.cfg;

import com.google.inject.AbstractModule;
import net.bobah.bd4j.CachedCalc;
import net.bobah.bd4j.DirectCalc;
import net.bobah.bd4j.ICalc;
import net.bobah.bd4j.PooledCalc;

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

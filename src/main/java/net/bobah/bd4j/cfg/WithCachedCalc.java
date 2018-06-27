package net.bobah.bd4j.cfg;

import com.google.inject.AbstractModule;
import net.bobah.bd4j.CachedCalc;
import net.bobah.bd4j.DirectCalc;
import net.bobah.bd4j.ICalc;

import static com.google.inject.name.Names.named;

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

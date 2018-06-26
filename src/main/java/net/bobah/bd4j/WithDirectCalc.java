package net.bobah.bd4j;

import com.google.inject.AbstractModule;

import static com.google.inject.name.Names.named;

public final class WithDirectCalc extends AbstractModule {
    @Override
    protected void configure() {
        bind(ICalc.class)
                .annotatedWith(named("frontend"))
                .to(DirectCalc.class);
    }
}

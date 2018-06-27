package net.bobah.bd4j.cfg;

import com.google.inject.AbstractModule;
import net.bobah.bd4j.DirectCalc;
import net.bobah.bd4j.ICalc;

import static com.google.inject.name.Names.named;

public final class WithDirectCalc extends AbstractModule {
    @Override
    protected void configure() {
        bind(ICalc.class)
                .annotatedWith(named("frontend"))
                .to(DirectCalc.class);
    }
}

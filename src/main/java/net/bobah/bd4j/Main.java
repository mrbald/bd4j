package net.bobah.bd4j;

import com.google.inject.Guice;
import com.google.inject.Module;
import net.bobah.bd4j.cfg.WithCachedCalc;
import net.bobah.bd4j.cfg.WithCachedPooledCalc;
import net.bobah.bd4j.cfg.WithDirectCalc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.stream.IntStream;

import static com.google.inject.Stage.PRODUCTION;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;

/**
 * Application launcher.<br>
 * <br>
 * Run as:<br><pre>
 *      java -cp ... net.bobah.bd4j.Main &lt;experiment_id&gt;<br></pre>
 * <br>
 * See {@link ExperimentId} enum for the valid values of the &lt;experiment_id&gt; argument.
 */
public final class Main
{
    private static final Logger log = LoggerFactory.getLogger(Main.class);
    private static final ExperimentId DEFAULT_EXPERIMENT_ID = ExperimentId.CACHED_POOLED;

    private final ICalc calc;

    private enum ExperimentId {
        DIRECT(new WithDirectCalc()),
        CACHED(new WithCachedCalc()),
        CACHED_POOLED(new WithCachedPooledCalc());

        final Module module;

        ExperimentId(Module module) {
            this.module = module;
        }
    }

    @Inject
    public Main(
            @Named("frontend") ICalc calc
    ) {
        this.calc = calc;
    }

    private void run() {
        log.info("requesting calculation");
        final Collection<Future<Integer>> futures = IntStream.of(8192, 16384, 65536, 16384, 65536, 8192)
                .sequential()
                .mapToObj(calc::calculate)
                .collect(toList());

        log.info("collecting results");

        futures.forEach(future -> {
            try {
                log.info("calc.calculate(): {}", future.get(30, SECONDS));
            } catch (InterruptedException|ExecutionException|TimeoutException ex) {
                log.info("exception", ex);
            }
        });
    }

    // run as java -cp ... net.bobah.bd4j.Main <DIRECT|CACHED|CACHED_POOLED>
    public static void main(String[] args)
    {
        currentThread().setName("main");

        log.info("hi");

        if (args.length != 1) {
            log.info("no ExperimentId specified, running with {}", DEFAULT_EXPERIMENT_ID);
            args = new String[] { DEFAULT_EXPERIMENT_ID.name() };
        }
        final ExperimentId experimentId = ExperimentId.valueOf(args[0]);
        log.info("running as {}", experimentId);

        Guice
                .createInjector(PRODUCTION, experimentId.module)
                .getInstance(Main.class)
                .run();
        log.info("bye");

        // TODO: VL: lifecycle management
        System.exit(0);
    }
}

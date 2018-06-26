package net.bobah.bd4j;

import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Future;

/**
 * Unit test for simple Main.
 */
public class DirectCalcTest
{
    private ICalc calc;

    @Before
    public void setUp() {
        calc = new DirectCalc(3L, Runnable::run);
    }

    @Test
    public void calcShouldWork() throws Exception {
        final Future<Integer> future = calc.calculate(42);
        assert future.isDone();
        assert future.get() == 24;
    }
}

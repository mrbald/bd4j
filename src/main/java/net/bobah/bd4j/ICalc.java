package net.bobah.bd4j;

import java.util.concurrent.Future;

/**
 * The calculator interface
 */
public interface ICalc {
    Future<Integer> calculate(int key);
}

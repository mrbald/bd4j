# BD4J

## Background

The project demonstrates a possible approach to scaling a request-response service in successive steps.
The first implied step (not included here) is the profiling of the service endpoint itself and eliminating bottlenecks which are possible to get rid of.
The second step is inserting a lazy cache in front of the service endpoint.
The final third step (for now) is replacing the the direct service endpoint with a load balanced pool of direct endpoints.

![diagram][diagram]

## Building

Command:
```bash
$ mvn clean package
```

## Running
A single calculation is synthetically adjusted to take 5 seconds to highlight the effect of caching and pooling.

### CACHED_POOLED (total runtime ~ 5 sec)

Command:
```bash
$ mvn exec:java -Dexperiment=CACHED_POOLED
```

Output:
```
00:00:55.908 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - hi // 121 ms
00:00:55.910 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - running as CACHED_POOLED // 123 ms
00:00:56.205 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - requesting calculation // 418 ms
00:00:56.210 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 8192... // 423 ms
00:00:56.211 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.PooledCalc - calculating for 8192... // 424 ms
00:00:56.217 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 16384... // 430 ms
00:00:56.218 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.PooledCalc - calculating for 16384... // 431 ms
00:00:56.218 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 8192... // 431 ms
00:00:56.218 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 65536... // 431 ms
00:00:56.218 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.PooledCalc - calculating for 65536... // 431 ms
00:00:56.218 DEBUG [pool-2-thread-1] n.b.b.DirectCalc - calculating for 16384... // 431 ms
00:00:56.222 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 16384... // 435 ms
00:00:56.222 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 65536... // 435 ms
00:00:56.222 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 8192... // 435 ms
00:00:56.222 DEBUG [pool-3-thread-1] n.b.b.DirectCalc - calculating for 65536... // 435 ms
00:00:56.223 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - collecting results // 436 ms
00:00:01.218 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 8192... // 5431 ms
00:00:01.218 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 2918 // 5431 ms
00:00:01.218 DEBUG [pool-2-thread-1] n.b.b.DirectCalc - calculated for 16384... // 5431 ms
00:00:01.219 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 48361 // 5432 ms
00:00:01.223 DEBUG [pool-3-thread-1] n.b.b.DirectCalc - calculated for 65536... // 5436 ms
00:00:01.223 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 63556 // 5436 ms
00:00:01.223 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 48361 // 5436 ms
00:00:01.223 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 63556 // 5436 ms
00:00:01.223 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 2918 // 5436 ms
00:00:01.223 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - bye // 5436 ms

```

### CACHED (total runtime ~ 15 sec)

Command:
```bash
$ mvn exec:java -Dexperiment=CACHED
```

Output:
```
00:00:38.444 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - hi // 119 ms
00:00:38.446 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - running as CACHED // 121 ms
00:00:38.630 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - requesting calculation // 305 ms
00:00:38.777 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 8192... // 452 ms
00:00:38.783 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 16384... // 458 ms
00:00:38.783 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 65536... // 458 ms
00:00:38.783 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 16384... // 458 ms
00:00:38.783 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 65536... // 458 ms
00:00:38.783 DEBUG [net.bobah.bd4j.Main.main()] n.b.b.CachedCalc - calculating for 8192... // 458 ms
00:00:38.783 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - collecting results // 458 ms
00:00:38.785 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 8192... // 460 ms
00:00:43.785 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 8192... // 5460 ms
00:00:43.785 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 2918 // 5460 ms
00:00:43.785 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 16384... // 5460 ms
00:00:48.785 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 16384... // 10460 ms
00:00:48.786 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 65536... // 10461 ms
00:00:48.786 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 48361 // 10461 ms
00:00:53.786 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 65536... // 15461 ms
00:00:53.786 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 63556 // 15461 ms
00:00:53.787 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 48361 // 15462 ms
00:00:53.787 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 63556 // 15462 ms
00:00:53.787 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 2918 // 15462 ms
00:00:53.787 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - bye // 15462 ms
```

### DIRECT (total runtime ~ 30 sec)

Command:
```bash
$ mvn exec:java -Dexperiment=DIRECT
```

Output:
```
00:00:13.014 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - hi // 125 ms
00:00:13.016 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - running as DIRECT // 127 ms
00:00:13.212 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - requesting calculation // 323 ms
00:00:13.380 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - collecting results // 491 ms
00:00:13.382 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 8192... // 493 ms
00:00:18.382 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 8192... // 5493 ms
00:00:18.382 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 2918 // 5493 ms
00:00:18.382 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 16384... // 5493 ms
00:00:23.383 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 16384... // 10494 ms
00:00:23.383 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 65536... // 10494 ms
00:00:23.383 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 48361 // 10494 ms
00:00:28.383 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 65536... // 15494 ms
00:00:28.383 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 16384... // 15494 ms
00:00:28.383 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 63556 // 15494 ms
00:00:33.384 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 16384... // 20495 ms
00:00:33.384 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 65536... // 20495 ms
00:00:33.384 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 48361 // 20495 ms
00:00:38.384 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 65536... // 25495 ms
00:00:38.384 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 63556 // 25495 ms
00:00:38.385 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculating for 8192... // 25496 ms
00:00:43.385 DEBUG [pool-1-thread-1] n.b.b.DirectCalc - calculated for 8192... // 30496 ms
00:00:43.385 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - calc.calculate(): 2918 // 30496 ms
00:00:43.385 INFO  [net.bobah.bd4j.Main.main()] n.b.b.Main - bye // 30496 ms
```


[diagram]: ./diagram.png

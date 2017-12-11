package com.abinesh.app.aggregator;

import com.abinesh.app.message.DedupeStatus;
import com.abinesh.app.logger.UniqueLineLogger;
import com.abinesh.app.stats.StatsThisCycle;

import java.io.IOException;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * Stats aggregator for the lines
 *
 * Receives the status from the DataProcessorWorker and then aggregates them
 */
public class StatsAggregator implements Runnable {

    ConcurrentLinkedQueue<DedupeStatus> dedupeStatusQueue;
    private StatsThisCycle statsThisCycle;

    //To not make the file IO affect the statistics aggregation.
    private LinkedBlockingDeque<Integer> lineLoggerQueue;

    public StatsAggregator(ConcurrentLinkedQueue<DedupeStatus> dedupeStatusQueue) {
        this.dedupeStatusQueue = dedupeStatusQueue;
        statsThisCycle = new StatsThisCycle();
        lineLoggerQueue = new LinkedBlockingDeque<Integer>();
        startPeriodicStatsForwarder();
        startLineLogger();
    }

    @Override
    public void run() {
        while (true) {
            DedupeStatus status = dedupeStatusQueue.poll();
            if (status == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                if (status.isUnique()) {
                    synchronized (statsThisCycle) {
                        statsThisCycle.incrementUnique();
                    }
                    lineLoggerQueue.add(status.getNumber());
                }else{
                    synchronized (statsThisCycle) {
                        statsThisCycle.incrementDuplicate();
                    }
                }
            }
        }
    }

    public void startPeriodicStatsForwarder() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        StatsForwarder statsForwarder = new StatsForwarder(statsThisCycle);
        scheduler.scheduleAtFixedRate(statsForwarder, 0, 10, SECONDS);
    }

    public void startLineLogger() {
        Thread t = null;
        try {
            t = new Thread(new UniqueLineLogger(lineLoggerQueue));
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.start();
    }
}

package com.abinesh.app.aggregator;

import com.abinesh.app.logger.StatsConsoleAdapter;
import com.abinesh.app.stats.StatsThisCycle;

/**
 * Stats Forwarder.
 * Resets the stats everytime it runs.
 *
 */
public class StatsForwarder implements Runnable {

    private StatsThisCycle statsThisCycle;
    private StatsConsoleAdapter consoleAdapter;

    public StatsForwarder(StatsThisCycle statsThisCycle) {
        this.statsThisCycle = statsThisCycle;
        consoleAdapter = StatsConsoleAdapter.getInstance();
    }

    @Override
    public void run() {
        consoleAdapter.logStats(statsThisCycle);
        synchronized (statsThisCycle){
            statsThisCycle.resetCycleStats();
        }
    }
}

package com.abinesh.app.logger;

import com.abinesh.app.stats.StatsThisCycle;

/**
 * Created by abine on 12/10/2017.
 */
public class StatsConsoleAdapter {
    private static StatsConsoleAdapter consoleAdapter = new StatsConsoleAdapter();

    public static StatsConsoleAdapter getInstance() {
        return consoleAdapter;
    }

    private StatsConsoleAdapter() {
    }

    public void logStats(StatsThisCycle statsThisCycle){
        System.out.println(statsThisCycle);
    }
}

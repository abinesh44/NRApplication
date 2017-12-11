package com.abinesh.app.stats;

/**
 * Datastructure for statistics in a particular cycle.
 */
public class StatsThisCycle {
    private int duplicatesSeen;
    private int uniqueCount;
    private int totalUniqueCount;

    public StatsThisCycle() {
    }

    public StatsThisCycle(int duplicatesSeen, int uniqueCount) {
        this.duplicatesSeen = duplicatesSeen;
        this.uniqueCount = uniqueCount;
    }

    public void resetCycleStats(){
        this.duplicatesSeen=0;
        this.uniqueCount=0;
    }

    public void incrementUnique(){
        uniqueCount++;
        totalUniqueCount++;
    }

    public void incrementDuplicate(){
        duplicatesSeen++;
    }

    @Override
    public String toString() {
        return "Received "+uniqueCount+" unique numbers, "+duplicatesSeen+" duplicates.\n" +
                "Unique total: "+ totalUniqueCount;
    }
}

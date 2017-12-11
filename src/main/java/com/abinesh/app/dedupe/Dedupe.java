package com.abinesh.app.dedupe;

/**
 * Datastructure to maintain the duplication logic backed by a byte map.
 * To keep the memory footprint to minimal, the dedupe DS is backed by a byte array.
 *
 */
public class Dedupe {
    private static Dedupe dedupe = new Dedupe();


    private int lowerBound = 0;
    private int upperBound = 999999999;
    byte[] byteMap;

    public static Dedupe getInstance() {
        return dedupe;
    }


    private Dedupe(){
        byteMap = new byte[upperBound+1];
    }

    private Dedupe(int lowerBound, int upperBound) {
        this.lowerBound=lowerBound;
        this.upperBound=upperBound;
    }

    /**
     * Given a number, checks if a number has already been visited
     * @param n Number to be checked.
     *
     * @return
     */
    public boolean isVisited(int n){
        return (byteMap[n] & 1)==1;
    }

    /**
     *
     * @param n
     */
    public void setVisited(int n){
        byteMap[n] = (byte)1;
    }
}

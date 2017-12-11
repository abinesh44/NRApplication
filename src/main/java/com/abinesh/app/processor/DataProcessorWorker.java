package com.abinesh.app.processor;

import com.abinesh.app.message.DedupeStatus;
import com.abinesh.app.dedupe.Dedupe;

import java.util.concurrent.*;

/**
 * Data Worker, checks if we have already visited the word(number) already,
 * Updates the status accordingly.
 */
public class DataProcessorWorker implements Runnable {

    private LinkedBlockingQueue<Integer> workerQueue;
    private Dedupe dedupe;
    ConcurrentLinkedQueue<DedupeStatus> dedupeStatusQueue;
    private volatile boolean terminateApp;

    public DataProcessorWorker(LinkedBlockingQueue<Integer> workerQueue,
                               Dedupe dedupe,
                               ConcurrentLinkedQueue<DedupeStatus> dedupeStatusQueue,
                               boolean terminateApp){
        this.workerQueue=workerQueue;
        this.dedupe = dedupe;
        this.dedupeStatusQueue=dedupeStatusQueue;
        this.terminateApp = terminateApp;
    }


    public void run() {
        while (!terminateApp){
            int element = 0;
            try {
                //Wait for available elements to process
                element = workerQueue.take();
                DedupeStatus status = new DedupeStatus(element, false );
                if(!dedupe.isVisited(element)){
                    dedupe.setVisited(element);
                    status.setUnique(true);
                }
                dedupeStatusQueue.offer(status);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}

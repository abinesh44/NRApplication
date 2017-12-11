package com.abinesh.app.processor;

import com.abinesh.app.message.DedupeStatus;
import com.abinesh.app.aggregator.StatsAggregator;
import com.abinesh.app.dedupe.Dedupe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Data processor Manager co-ordinates the data processing.
 *
 * <p>
 *     For concurrent processing, parallalizes the work to 4 worker threads.
 *
 *     To prevent the synchronization on the Dedupe datastructure, each worker has its own Queue
 *     And the numbers are directed based on the modulo, thereby preventing concurrent access on a particular index although the entire array is shared.
 * </p>
 */
public class DataProcessorManager implements Runnable {

    private final int NUMBER_OF_WORKERS = 4;

    private LinkedBlockingDeque<Integer> lineManagerQueue;

    private List<LinkedBlockingQueue<Integer>> workerDistributionQueueList =
            new ArrayList<LinkedBlockingQueue<Integer>>();

    private ConcurrentLinkedQueue<DedupeStatus> dedupeStatusQueue;


    private Dedupe dedupe;
    ExecutorService workerPool = Executors.newFixedThreadPool(NUMBER_OF_WORKERS);

    private volatile boolean terminateApp;

    public DataProcessorManager(LinkedBlockingDeque<Integer> lineManagerQueue, boolean terminateApp){
        this.lineManagerQueue=lineManagerQueue;
        this.dedupeStatusQueue = new ConcurrentLinkedQueue<>();
        dedupe = Dedupe.getInstance();
        this.terminateApp = terminateApp;
        initWorkers();
        createStatsAggregator();
    }

    public void run() {
        boolean started = false;
        while (!terminateApp){
            int element = 0;
            try {
                //Block until it has something to process
                element = lineManagerQueue.take();
                selectQueueToPublish(element).put(element);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Select the Queue to be published based on the element.
     * Simple modulo to find the right Queue.
     * This will ensure the values will be evenly distributed and
     * we don't have to handle concurrency issues within the worker
     *
     * @param n
     * @return
     */
    public LinkedBlockingQueue<Integer> selectQueueToPublish(int n){
        return workerDistributionQueueList.get(n% NUMBER_OF_WORKERS);
    }


    public void initWorkers(){
        for(int i=0; i< NUMBER_OF_WORKERS; i++){
            workerDistributionQueueList.add(new LinkedBlockingQueue<>());
            DataProcessorWorker worker=new DataProcessorWorker(workerDistributionQueueList.get(i), dedupe,
                    dedupeStatusQueue, terminateApp);

            Thread wThread = new Thread(worker);
            wThread.start();
        }
    }

    public void createStatsAggregator(){
        StatsAggregator statsAggregator = new StatsAggregator(dedupeStatusQueue);
        Thread aggThread = new Thread(statsAggregator);
        aggThread.start();
    }
}

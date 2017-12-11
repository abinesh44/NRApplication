package com.abinesh.app.listener;

import com.abinesh.app.processor.DataProcessorManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

import static com.abinesh.app.utility.LineValidator.isStopCommand;
import static com.abinesh.app.utility.LineValidator.isValidNumber;

/**
 * Socket server that listens on the port 4000.
 * Spans 5 threads and thereby limiting the number of concurrent connections.
 */
public class NumberListenerService implements Runnable {

    private int port;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;

    //Volatile variable for the threads to act as a flag.
    private volatile boolean terminateApp = false;

    private static final int NUMBER_OF_CONCURRENT_CLIENTS = 5;
    private LinkedBlockingDeque<Integer> lineManagerQueue;


    public NumberListenerService(int port) {
        this.port = port;
        threadPool = Executors.newFixedThreadPool(NUMBER_OF_CONCURRENT_CLIENTS);
        lineManagerQueue = new LinkedBlockingDeque<Integer>();
        startDataProcessingPipeline();
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
            while (!terminateApp) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setReuseAddress(true);
                LineClientHandler clientHandler = new LineClientHandler(clientSocket, lineManagerQueue, terminateApp);
                threadPool.submit(clientHandler);
            }
            stop();
        } catch (IOException ioEX) {
            ioEX.printStackTrace();
        }finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void stop() throws IOException {
        threadPool.shutdown();
    }


    /*
        Start the pipeline and actors required for the data processing pipeline.

     */
    public void startDataProcessingPipeline() {
        DataProcessorManager dataProcessorManager = new DataProcessorManager(lineManagerQueue, terminateApp );
        Thread dpThread = new Thread(dataProcessorManager);
        dpThread.start();
    }


}

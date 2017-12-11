package com.abinesh.app;

import com.abinesh.app.listener.NumberListenerService;

/**
 * Application starting point, starting at port 4000

 */
public class Application {
    public static final int PORT= 4000;

    public static void main(String[] args){
        NumberListenerService listenerService = new NumberListenerService(PORT);
        Thread listenerThread = new Thread(listenerService);
        listenerThread.start();
    }
}

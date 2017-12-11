package com.abinesh.app.listener;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingDeque;

import static com.abinesh.app.utility.LineValidator.isStopCommand;
import static com.abinesh.app.utility.LineValidator.isValidNumber;

/**
 * Handler for the incoming client socket connections.
 *
 */
public class LineClientHandler implements Runnable {

    private static final int NUMBER_OF_DIGITS =9;
    private String STOP_COMMAND ="terminate";

    private volatile boolean terminateApp = false;
    LinkedBlockingDeque<Integer> lineManagerQueue;

    Socket socket;

    public LineClientHandler(Socket socket, LinkedBlockingDeque<Integer> lineManagerQueue, boolean terminateApp){
        this.socket= socket;
        this.lineManagerQueue=lineManagerQueue;
        this.terminateApp = terminateApp;
    }

    @Override
    public void run() {
        InputStreamReader iStreamReader = null;
        BufferedReader in = null;
        try {
            iStreamReader = new InputStreamReader(socket.getInputStream());
            in = new BufferedReader(iStreamReader);
            String inputLine = in.readLine();
            if(isValidNumber(inputLine, NUMBER_OF_DIGITS)){
                lineManagerQueue.add(Integer.valueOf(inputLine));
            }else if(isStopCommand(inputLine, STOP_COMMAND)){
                terminateApp=true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                iStreamReader.close();
                in.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

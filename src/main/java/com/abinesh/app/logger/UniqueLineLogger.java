package com.abinesh.app.logger;

import java.io.*;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by abine on 12/10/2017.
 */
public class UniqueLineLogger implements Runnable{

    private LinkedBlockingDeque<Integer> lineLoggerQueue;
    private static final String DIRECTORY = File.separator+"tmp";
    private static final String FILE_NAME = "numbers.log";


    public UniqueLineLogger(LinkedBlockingDeque<Integer> lineLoggerQueue) throws IOException {
        this.lineLoggerQueue = lineLoggerQueue;
        initLogFile();
    }


    @Override
    public void run() {
        while(true){
            try {
                int element = lineLoggerQueue.take();
                writeToLogFile(element);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initLogFile()  throws IOException{
        new File(DIRECTORY).mkdir();
        File file = new File(DIRECTORY+File.separator+FILE_NAME);
        //Clear the contents of the file
        PrintWriter writer = new PrintWriter(file);
        writer.print("");
        writer.close();

    }

    public void writeToLogFile(int element) throws IOException {
        File file = new File(DIRECTORY+File.separator+FILE_NAME);
        FileWriter fw = new FileWriter(file, true);
        BufferedWriter bw = new BufferedWriter(fw);
        PrintWriter pw = new PrintWriter(bw);
        pw.println(element);
        pw.flush();
        pw.close();
    }
}

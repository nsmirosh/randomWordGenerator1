package com.example.nsmirosh.androidmultithreadingpractice;

import java.util.Random;

/**
 * Created by nsmirosh on 4/20/17.
 */

public class WorkerThread extends Thread {


    ExecutionListener mListener;

    int mThreadNumber;


    public WorkerThread(ExecutionListener executionListener, int ThreadNumber) {
        mListener = executionListener;
        mThreadNumber = ThreadNumber;
    }


    @Override
    public void run() {
        super.run();

        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(5000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mListener.onExecuted(mThreadNumber);
    }


    interface ExecutionListener {

        void onExecuted(int threadNumber);
    }
}

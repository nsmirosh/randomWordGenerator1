package com.example.nsmirosh.androidmultithreadingpractice;

import java.util.Random;

/**
 * Created by nsmirosh on 4/20/17.
 */

public class WorkerThread extends Thread {


    ExecutionListener mListener;

    int mThreadNumber;

    boolean mIsProducer;


    public WorkerThread(boolean isProducer, ExecutionListener executionListener, int ThreadNumber) {

        mIsProducer = isProducer;
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
        if (mIsProducer) {
            mListener.onProducerExecuted(mThreadNumber);
        }
        else {
            mListener.onConsumerExecuted(mThreadNumber);
        }
    }



    interface ExecutionListener {

        void onConsumerExecuted(int threadNumber);
        void onProducerExecuted(int threadNumber);
    }
}

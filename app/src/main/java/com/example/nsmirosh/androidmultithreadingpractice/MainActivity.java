package com.example.nsmirosh.androidmultithreadingpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity implements WorkerThread.ExecutionListener{


    TextView mToUpdateTextView;
    TextView mAmountOfThreadsInWork;
    TextView mGeneratedTextTV;

    Button mStartProducingBtn;

    Button mStartConsumingBtn;

    Button mClearBtn;
    String mGenereratedText = "";


    String[] words = new String[] {"Fun", "Stuff", "Dude"};

    int producerPermits = 5;
    int consumerPermits = 5;

    Semaphore producerSemaphore = new Semaphore(producerPermits);

    Semaphore consumerSemaphore = new Semaphore(consumerPermits);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToUpdateTextView = (TextView) findViewById(R.id.current_working_thread_tv);

        mAmountOfThreadsInWork = (TextView) findViewById(R.id.producer_threads_in_work_tv);

        mGeneratedTextTV = (TextView) findViewById(R.id.generated_text_tv);

        mStartProducingBtn = (Button) findViewById(R.id.start_producing_btn);

        mStartProducingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProducing();
            }
        });


        mStartConsumingBtn = (Button) findViewById(R.id.start_consuming_btn);

        mStartConsumingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProducing();
            }
        });

        mClearBtn = (Button) findViewById(R.id.clear_btn);
        mClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGenereratedText = "";
                mGeneratedTextTV.setText("");
            }
        });
    }

    private void startProducing() {

        for (int i = 0; i < 20; i++) {
            Thread workerThread = new WorkerThread(true, MainActivity.this, i);
            workerThread.start();

        }
    }

    private void startConsuming() {

        for (int i = 0; i < 20; i++) {
            Thread workerThread = new WorkerThread(false, MainActivity.this, i);
            workerThread.start();
        }
    }


    @Override
    public void onConsumerExecuted(int threadNumber) {
        updateTextView1(" thread " + threadNumber);

    }

    @Override
    public void onProducerExecuted(int threadNumber) {
        updateTextView1(" thread " + threadNumber);


    }

    private void updateTextView1(final String threadName) {
        try {
            producerSemaphore.acquire();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    mGenereratedText = mGenereratedText.concat(" " + words[random.nextInt(3)]);
                    mGeneratedTextTV.setText(mGenereratedText);
                    mAmountOfThreadsInWork.setText("Amount of producer threads in work " + (5 - producerSemaphore.availablePermits()));
                    mToUpdateTextView.setText("updated by: " + threadName);
                }
            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            producerSemaphore.release();
        }
    }
}



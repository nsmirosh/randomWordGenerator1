package com.example.nsmirosh.androidmultithreadingpractice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Random;
import java.util.TooManyListenersException;
import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity implements WorkerThread.ExecutionListener {


    TextView mToUpdateTextView;
    TextView mAmountOfThreadsInWork;
    TextView mGeneratedTextTV;

    String mGenereratedText = "";


    String[] words = new String[] {"Fun", "Stuff", "Dude"};

    int threadNumber = 0;

    Semaphore semaphore = new Semaphore(5);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mToUpdateTextView = (TextView) findViewById(R.id.start_btn);

        mAmountOfThreadsInWork = (TextView) findViewById(R.id.amount_of_threads_in_work_tv);

        mGeneratedTextTV = (TextView) findViewById(R.id.generated_text_tv);

        mToUpdateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });
    }

    private void start() {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 20; i++) {
                    WorkerThread workerThread = new WorkerThread(MainActivity.this, i);
                    workerThread.start();

                }
            }
        });

        thread.start();
    }


    @Override
    public void onExecuted(int threadNumber) {
        updateTextView1(" thread " + threadNumber);
    }


    private void updateTextView1(final String threadName) {
        try {

            Log.d("MainActivity", threadName  + " acquiring a permit");
            semaphore.acquire();



            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Random random = new Random();
                    mGenereratedText = mGenereratedText.concat(" " + words[random.nextInt(3)]);
                    mGeneratedTextTV.setText(mGenereratedText);
                    mAmountOfThreadsInWork.setText("Amount of threads in work" + (5 - semaphore.availablePermits()));
                    mToUpdateTextView.setText("updated by: " + threadName);
                }
            });

            Thread.sleep(1000);


        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {

            Log.d("MainActivity", threadName  + " releasing a permit");

            semaphore.release();
        }
    }
}



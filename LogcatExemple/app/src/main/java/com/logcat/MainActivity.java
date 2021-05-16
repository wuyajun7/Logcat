package com.logcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ethan.logcat.LogcatBus;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startLogcat(View view) {
        LogcatBus.getDefault().startLogcat();
    }

    public void overLogcat(View view) {
        LogcatBus.getDefault().overLogcat();
    }

    public void printLog(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    LogcatBus.getDefault().post(String.valueOf(System.currentTimeMillis()));
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}
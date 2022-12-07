package com.logcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ethan.logcat.LogCatItemTouchListener;
import com.ethan.logcat.LogcatBus;
import com.ethan.logcat.LogcatView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivityLog";

    private LogcatView logcatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logcatView = findViewById(R.id.logcatView);
        logcatView.setItemTouchListener(new LogCatItemTouchListener() {
            public void onClick(String text) {
                Log.d(TAG, text);
            }

            public void onLongClick(String text) {
                Log.d(TAG, text);
            }
        });
    }

    public void startLogcat(View view) {
        LogcatBus.getDefault().startLogcat();
    }

    public void overLogcat(View view) {
        LogcatBus.getDefault().overLogcat();
    }

    public void printLog(View view) {
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                String log = "测试日志输出" + System.currentTimeMillis();
                LogcatBus.getDefault().post(log);
                logcatView.addLogToView(log);

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public void printLog1(View view) {
        String log = "测试日志输出Log_1";
        LogcatBus.getDefault().post(log);
        logcatView.addLogToView(log);
    }

    public void printLog2(View view) {
        String log = "测试日志输出Log_2";
        LogcatBus.getDefault().post(log);
        logcatView.addLogToView(log);
    }

    public void printLog3(View view) {
        String log = "测试日志输出Log_3";
        LogcatBus.getDefault().post(log);
        logcatView.addLogToView(log);
    }
}
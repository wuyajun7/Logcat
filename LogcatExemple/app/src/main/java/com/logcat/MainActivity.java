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
                for (int i = 0; i < 100; i++) {
                    LogcatBus.getDefault().post("测试日志输出"+String.valueOf(System.currentTimeMillis()));
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    public void printLog1(View view) {
        LogcatBus.getDefault().post("测试日志输出Test_1");
    }

    public void printLog2(View view) {
        LogcatBus.getDefault().post("测试日志输出Test_2");
    }

    public void printLog3(View view) {
        LogcatBus.getDefault().post("测试日志输出Test_3");
    }
}
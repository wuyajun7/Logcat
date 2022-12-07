package com.ethan.logcat;

import android.content.Intent;

public class LogcatBus {

    static volatile LogcatBus defaultInstance;

    private LogcatBus() {
    }

    public static LogcatBus getDefault() {
        if (defaultInstance == null) {
            synchronized (LogcatBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new LogcatBus();
                }
            }
        }
        return defaultInstance;
    }

    public void startLogcat() {
        Intent intent = new Intent(LogcatProvider.getAppContext(), LogcatService.class);
        LogcatProvider.getAppContext().startService(intent);
    }

    public void overLogcat() {
        Intent intent = new Intent(LogcatProvider.getAppContext(), LogcatService.class);
        LogcatProvider.getAppContext().stopService(intent);
    }

    public void post(String log) {
        LogcatBroadcastManager.getInstance(LogcatProvider.getAppContext())
                .sendBroadcast(LogcatBroadcastManager.BM_PRINT_LOG, log);
    }
}

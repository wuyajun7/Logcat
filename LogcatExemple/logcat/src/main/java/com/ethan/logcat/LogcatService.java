package com.ethan.logcat;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ethan
 */
public class LogcatService extends Service {

    private static final String TAG = "LogcatService";

    private LogcatView mFloatingView;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate");
        LogcatBroadcastManager.getInstance(this).addAction(LogcatBroadcastManager.BM_PRINT_LOG, new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String command = intent.getAction();
                if (LogcatBroadcastManager.BM_PRINT_LOG.equals(command)) {
                    String log = intent.getStringExtra("result");
                    if (mFloatingView != null) {
                        mFloatingView.refreshLogView(log);
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        if (mFloatingView == null) {
            mFloatingView = new LogcatView(this);

            if (!mFloatingView.isShowing()) {
                mFloatingView.show();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");

        LogcatBroadcastManager.getInstance(this).destroy(LogcatBroadcastManager.BM_PRINT_LOG);

        if (mFloatingView != null) {
            mFloatingView.hide();
            mFloatingView = null;
        }
    }
}
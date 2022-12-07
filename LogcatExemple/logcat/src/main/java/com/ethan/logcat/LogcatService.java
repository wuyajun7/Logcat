package com.ethan.logcat;

import android.app.AppOpsManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by ethan
 */
public class LogcatService extends Service {

    private static final String TAG = "LogcatService";

    private LogcatFloatView mFloatingView;

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

        if (checkFloatPermission(this)) {
            if (mFloatingView == null) {
                mFloatingView = new LogcatFloatView(this);
                if (!mFloatingView.isShowing()) {
                    mFloatingView.show();
                }
            }
        } else {
            requestSettingCanDrawOverlays(this);
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

    /**
     * 判断是否开启悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean checkFloatPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
            return true;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            try {
                Class cls = Class.forName("android.content.Context");
                Field declaredField = cls.getDeclaredField("APP_OPS_SERVICE");
                declaredField.setAccessible(true);
                Object obj = declaredField.get(cls);
                if (!(obj instanceof String)) {
                    return false;
                }
                String str2 = (String) obj;
                obj = cls.getMethod("getSystemService", String.class).invoke(context, str2);
                cls = Class.forName("android.app.AppOpsManager");
                Field declaredField2 = cls.getDeclaredField("MODE_ALLOWED");
                declaredField2.setAccessible(true);
                Method checkOp = cls.getMethod("checkOp", Integer.TYPE, Integer.TYPE, String.class);
                int result = (Integer) checkOp.invoke(obj, 24, Binder.getCallingUid(), context.getPackageName());
                return result == declaredField2.getInt(cls);
            } catch (Exception e) {
                return false;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED;
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                AppOpsManager appOpsMgr = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                if (appOpsMgr == null)
                    return false;
                int mode = appOpsMgr.checkOpNoThrow("android:system_alert_window", android.os.Process.myUid(), context
                        .getPackageName());
                return mode == AppOpsManager.MODE_ALLOWED || mode == AppOpsManager.MODE_IGNORED;
            } else {
                return Settings.canDrawOverlays(context);
            }
        }
    }

    private void requestSettingCanDrawOverlays(Context context) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= Build.VERSION_CODES.O) {// 8.0以上
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // context.startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
            context.startActivity(intent);
        } else if (sdkInt >= Build.VERSION_CODES.M) {// 6.0-8.0
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setData(Uri.parse("package:" + getPackageName()));
            // context.startActivityForResult(intent, REQUEST_DIALOG_PERMISSION);
            context.startActivity(intent);
        } else {
            // 4.4-6.0以下
            // 无需处理了
        }
    }
}
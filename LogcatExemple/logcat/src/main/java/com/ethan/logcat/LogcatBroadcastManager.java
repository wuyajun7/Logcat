package com.ethan.logcat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong on 2016/2/18.
 * @ClassName：BroadcastManager
 * @描述:广播管理者
 * @使用方法： （1）在任何地方发送广播
 * BroadcastManager.getInstance(mContext).sendBroadcast(FindOrderActivity.ACTION_RECEIVE_MESSAGE);
 * （2）页面在onCreate()中初始化广播
 * BroadcastManager.getInstance(mContext).addAction(ACTION_RECEIVE_MESSAGE, new BroadcastReceiver(){
 * @Override public void onReceive(Context arg0, Intent intent) {
 * String command = intent.getAction();
 * if(!TextUtils.isEmpty(command)){
 * if((ACTION_RECEIVE_MESSAGE).equals(command)){
 * //获取json结果
 * String json = intent.getStringExtra("result");
 * //做你该做的事情
 * }
 * }
 * }
 * });
 * (3)页面在ondestory销毁广播
 * BroadcastManager.getInstance(mContext).destroy(ACTION_RECEIVE_MESSAGE);
 */
public class LogcatBroadcastManager {

    // 微信code回调
    public static String BM_PRINT_LOG = "bm_print_log";

    // ---------------------------------------------------------------------------------------------

    private Context mContext;
    private static LogcatBroadcastManager instance;
    private Map<String, BroadcastReceiver> receiverMap;

    /**
     * 构造方法
     *
     * @param context
     */
    private LogcatBroadcastManager(Context context) {
        this.mContext = context;
        receiverMap = new HashMap<String, BroadcastReceiver>();
    }

    /**
     * [获取BroadcastManager实例，单例模式实现]
     *
     * @param context
     * @return
     */
    public static LogcatBroadcastManager getInstance(Context context) {
        if (instance == null) {
            synchronized (LogcatBroadcastManager.class) {
                if (instance == null) {
                    instance = new LogcatBroadcastManager(context);
                }
            }
        }
        return instance;
    }

    /**
     * 添加Action,做广播的初始化
     *
     * @param
     */
    public void addAction(String action, BroadcastReceiver receiver) {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(action);
            mContext.registerReceiver(receiver, filter);
            receiverMap.put(action, receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加Action,做广播的初始化
     *
     * @param
     */
    public void addAction(String[] actions, BroadcastReceiver receiver) {
        try {
            IntentFilter filter = new IntentFilter();
            for (String action : actions) {
                filter.addAction(action);
            }
            mContext.registerReceiver(receiver, filter);
            for (String action : actions) {
                receiverMap.put(action, receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     */
    public void sendBroadcast(String action) {
        sendBroadcast(action, "");
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param obj    参数
     */
    public void sendBroadcast(String action, Object obj) {
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra("result", obj.toString());
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送广播
     *
     * @param action 唯一码
     * @param obj    参数
     */
    public void sendBroadcastSerializable(String action, Object obj) {
        try {
            Intent intent = new Intent();
            intent.setAction(action);
            intent.putExtra("result", (Serializable) obj);
            mContext.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 销毁广播
     *
     * @param actions
     */
    public void destroy(String... actions) {
        try {
            if (receiverMap != null) {
                for (String action : actions) {
                    BroadcastReceiver receiver = receiverMap.get(action);
                    if (receiver != null) {
                        mContext.unregisterReceiver(receiver);
                    }
                }
            }
        } catch (IllegalArgumentException e) {
            Log.d("Broadcastmanager", e.toString());
        }
    }
}
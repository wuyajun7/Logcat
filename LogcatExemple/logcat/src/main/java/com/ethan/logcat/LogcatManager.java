package com.ethan.logcat;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;

/**
 * Created by ethan
 */
public class LogcatManager {

    public static final String VERSION_INFO = "Android Logcat [版本 1.1.0.2]";

    private WindowManager mWindowManager;
    private static LogcatManager mInstance;
    private Context mContext;

    public static LogcatManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LogcatManager(context);
        }
        return mInstance;
    }

    private LogcatManager(Context context) {
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);//获得WindowManager对象
    }

    /**
     * 添加悬浮窗
     *
     * @param view
     * @param params
     * @return
     */
    protected boolean addView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.addView(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 移除悬浮窗
     *
     * @param view
     * @return
     */
    protected boolean removeView(View view) {
        try {
            mWindowManager.removeView(view);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 更新悬浮窗参数
     *
     * @param view
     * @param params
     * @return
     */
    protected boolean updateView(View view, WindowManager.LayoutParams params) {
        try {
            mWindowManager.updateViewLayout(view, params);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
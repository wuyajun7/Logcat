package com.ethan.logcat;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright (C), 2015-2020, Powered by Ethan
 *
 * @Description: 悬浮窗view
 * @Author: Ethan
 * @Date: 2020-08-01 20:20
 */
public class LogcatView extends FrameLayout {

    private final String VERSION_INFO = "Android Logcat [版本 1.1.0.1]";

    private WindowManager.LayoutParams mRootViewParams;
    private LogcatManager mWindowManager;

    private View mRootView;
    private ListView lv_log;

    private List<String> logList;
    private ArrayAdapter<String> logAdapter;

    public LogcatView(Context context) {
        super(context);

        initData();
        initView();
    }

    private void initData() {
        mWindowManager = LogcatManager.getInstance(getContext());

        logList = new ArrayList<>();
        logList.add(VERSION_INFO);
        logAdapter = new ArrayAdapter<>(getContext(), R.layout.layout_logcat_item, logList);
    }

    private void initView() {
        mRootView = View.inflate(getContext(), R.layout.layout_logcat, null);

        lv_log = mRootView.findViewById(R.id.lv_log);
        lv_log.setAdapter(logAdapter);

        mRootView.findViewById(R.id.move_view).setOnTouchListener(mOnTouchListener);
        mRootView.findViewById(R.id.close_view).setOnClickListener(v -> LogcatBus.getDefault().overLogcat());
        mRootView.findViewById(R.id.clear).setOnClickListener(v -> {
            if (logList != null && logAdapter != null) {
                logList.clear();
                logList.add(VERSION_INFO);
                logAdapter.notifyDataSetChanged();
            }
            if (lv_log != null) {
                lv_log.setStackFromBottom(false);
            }
        });
        mRootView.findViewById(R.id.switch_plan).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        mRootView.findViewById(R.id.switch_limit).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private boolean isShowing;

    public boolean isShowing() {
        return isShowing;
    }

    public void show() {
        mRootViewParams = new WindowManager.LayoutParams();
        mRootViewParams.gravity = Gravity.TOP | Gravity.LEFT;
        mRootViewParams.x = 0;
        mRootViewParams.y = 230;

        // 设置为TYPE_SYSTEM_ALERT类型，才能悬浮在其它页面之上
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            // 注意TYPE_SYSTEM_ALERT从Android8.0开始被舍弃了
            mRootViewParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            // 从Android8.0开始悬浮窗要使用TYPE_APPLICATION_OVERLAY
            mRootViewParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        //设置图片格式，效果为背景透明
        mRootViewParams.format = PixelFormat.RGBA_8888;
        mRootViewParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        mRootViewParams.width = LayoutParams.MATCH_PARENT;
        mRootViewParams.height = LayoutParams.WRAP_CONTENT;
        mWindowManager.addView(mRootView, mRootViewParams);

        isShowing = true;
    }

    public void hide() {
        mWindowManager.removeView(mRootView);
        isShowing = false;
    }

    public void refreshLogView(String log) {
        if (logList != null && logAdapter != null) {
            logList.add(log);
            logAdapter.notifyDataSetChanged();
        }
        if (lv_log != null) {
            if (logList.size() > 50 && !lv_log.isStackFromBottom()) {
                lv_log.setStackFromBottom(true);
                lv_log.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
            } else {
                lv_log.setSelection(lv_log.getBottom());
            }
        }
    }

    private OnTouchListener mOnTouchListener = new OnTouchListener() {
        // 记录上次移动的位置
        private float lastX = 0;
        private float lastY = 0;
        // 是否是移动事件
        boolean isMoved = false;

        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    isMoved = true;
                    // 记录按下位置
                    lastX = event.getRawX();
                    lastY = event.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    isMoved = true;
                    // 记录移动后的位置
                    float moveX = event.getRawX();
                    float moveY = event.getRawY();

                    mRootViewParams.x += (int) (moveX - lastX);
                    mRootViewParams.y += (int) (moveY - lastY);
                    mWindowManager.updateView(mRootView, mRootViewParams);
                    lastX = moveX;
                    lastY = moveY;
                case MotionEvent.ACTION_CANCEL:
                    isMoved = false;
                    break;
            }

            // 如果是移动事件, 则消费掉; 如果不是, 则由其他处理, 比如点击
            return isMoved;
        }
    };
}
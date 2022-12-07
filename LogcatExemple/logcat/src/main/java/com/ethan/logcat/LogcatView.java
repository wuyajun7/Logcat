package com.ethan.logcat;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Copyright (C), 2013-2022,
 *
 * @Description:
 * @Author:
 * @Date: 2022/12/7 10:39 上午
 */
public class LogcatView extends FrameLayout {

    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public LogcatView(@NonNull Context context) {
        super(context);
        initData();
        initView();
    }

    public LogcatView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initData();
        initView();
    }

    public LogcatView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
        initView();
    }

    private List<String> logList;
    private ArrayAdapter<String> logAdapter;

    private void initData() {
        logList = new ArrayList<>();
        logAdapter = new ArrayAdapter<>(getContext(), R.layout.layout_logcat_view_item, logList);
    }

    private ListView logLv;

    private void initView() {
        View logView = View.inflate(getContext(), R.layout.layout_logcat_view, null);
        logLv = logView.findViewById(R.id.logLv);
        logLv.setAdapter(logAdapter);
        logLv.setOnItemClickListener((parent, view, position, id) -> {
            if (position < logList.size() && itemTouchListener != null) {
                itemTouchListener.onClick(logList.get(position));
            }
        });
        logLv.setOnItemLongClickListener((parent, view, position, id) -> {
            if (position < logList.size() && itemTouchListener != null) {
                itemTouchListener.onLongClick(logList.get(position));
            }
            return false;
        });
        addView(logView);

        addLogToView(LogcatManager.VERSION_INFO);
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss.SS");

    public void addLogToView(String log) {
        uiHandler.post(() -> {
            if (logList != null && logAdapter != null) {
                logList.add(dateFormat.format(new Date()) + " " + log);
                logAdapter.notifyDataSetChanged();
            }
            if (logLv != null) {
                if (logList.size() > 50 && !logLv.isStackFromBottom()) {
                    logLv.setStackFromBottom(true);
                    logLv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                } else {
                    logLv.setSelection(logLv.getBottom());
                }
            }
        });
    }

    public void clearLogs() {
        if (logList != null && logAdapter != null) {
            logList.clear();
            logAdapter.notifyDataSetChanged();
        }
        if (logLv != null) {
            logLv.setStackFromBottom(false);
        }
        addLogToView(LogcatManager.VERSION_INFO);
    }

    private LogCatItemTouchListener itemTouchListener;

    public void setItemTouchListener(LogCatItemTouchListener itemTouchListener) {
        this.itemTouchListener = itemTouchListener;
    }

}
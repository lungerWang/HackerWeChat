package com.yizhong.hackerwechat;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import java.util.List;

public class HackerService extends AccessibilityService {

    private AccessibilityNodeInfo mRootNode;
    private InfiniteTask mTask = new InfiniteTask();

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("wbl", "onstart");
        Looper.prepare();


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("wbl", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String currentWindowActivity = event.getClassName().toString();
        Log.d("wbl", "eventType = " + eventType + ",currentWindowActivity = " + currentWindowActivity);
        if (currentWindowActivity.contains("com.tencent.mm.ui.LauncherUI")) {
            //获取当前聊天页面的根布局
            new Thread(new Runnable() {
                @Override
                public void run() {
                    SystemClock.sleep(5000);
                    mTask.start();

                }
            }).start();

        }
    }


    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
        Toast.makeText(this, "我快被终结了啊-----", Toast.LENGTH_SHORT).show();
    }

    /**
     * 服务开始连接
     */
    @Override
    protected void onServiceConnected() {
        Toast.makeText(this, "服务已开启", Toast.LENGTH_SHORT).show();
        super.onServiceConnected();
    }

    /**
     * 服务断开
     *
     * @param intent
     * @return
     */
    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "服务已被关闭", Toast.LENGTH_SHORT).show();
        return super.onUnbind(intent);
    }


    public void getText() {
        Log.d("wbl", "开始获取大数据");
        if(mRootNode == null){
            return;
        }
        List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = mRootNode.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/a57");
        if (accessibilityNodeInfosByViewId == null || accessibilityNodeInfosByViewId.size() == 0) {
            return;
        }
        for (int i = 0; i < accessibilityNodeInfosByViewId.size(); i++) {
            AccessibilityNodeInfo accessibilityNodeInfo = accessibilityNodeInfosByViewId.get(i);
            String s = accessibilityNodeInfo.getText().toString();
            if (MatchUtil.match4number(s)) {
                Log.d("wbl", "获取到大数据：" + s);
            }

        }

    }


    class InfiniteTask extends Handler implements Runnable {

        public InfiniteTask() {

        }

        @Override
        public void run() {
            mRootNode = getRootInActiveWindow();
            getText();
            this.postDelayed(this, 3000);
        }

        public void start() {
            this.removeCallbacks(this);
            this.postDelayed(this, 1000);
        }

        public void stop() {
            this.removeCallbacks(this);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mTask.stop();
    }
}

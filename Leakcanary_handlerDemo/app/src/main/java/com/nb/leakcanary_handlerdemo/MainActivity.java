package com.nb.leakcanary_handlerdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private TextView textView;
    private MyHandler mMyHandler;

    //直接在Activity中创建匿名内部类的Handler可能会造成内存泄漏
    //当你发送的消息没有处理时,占据着Activity的应用,当Activity页面销毁时,其引用还无法销毁,产生了内存泄漏

    //    private Handler mHandler = new Handler() {
    //        @Override
    //        public void handleMessage(Message msg) {
    //            super.handleMessage(msg);
    //            if (msg.what == 0) {
    //                textView.setText("gogogogo");
    //            }
    //        }
    //    };
    //    private void loadData() {
    //        mHandler.sendEmptyMessageDelayed(0,20000);
    //    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyHandler = new MyHandler(this);
        initView();
        loadData();
    }

    private void loadData() {
        mMyHandler.sendEmptyMessageDelayed(0, 20000);
    }


    private void initView() {
        button = (Button) findViewById(R.id.button);
        textView = (TextView) findViewById(R.id.textView);
    }

    //解决办法就是:1,创建一个静态的内部类Handler
    //            2,在静态内部类中弱引用MainActivity
    //            3,在onDestory方法中小回到Handler的消息队列

    static class MyHandler extends Handler {

        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity mainActivity) {
            weakReference = new WeakReference<>(mainActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (weakReference != null && weakReference.get() != null) {
                weakReference.get().textView.setText("whatthefuck");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyHandler.removeCallbacksAndMessages(null);
    }
}

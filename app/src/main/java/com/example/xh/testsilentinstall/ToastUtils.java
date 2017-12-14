package com.example.xh.testsilentinstall;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;


/**
 * 可根据Config.DEBUG开关show 可在子线程show
 */
public class ToastUtils {

    private Toast toast;

    private Handler handler;

    private Runnable showRunable;

    private String text;

    private ToastUtils(final Context context) {
        handler = new Handler(Looper.getMainLooper());
        showRunable = new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
                }
                toast.setText(ToastUtils.this.text);
                toast.show();
            }
        };
    }

    public static ToastUtils getInstance(Context context) {
        return new ToastUtils(context);
    }

    public boolean showText(String text) {
        this.text = text;

        handler.post(showRunable);
        return true;

    }
}

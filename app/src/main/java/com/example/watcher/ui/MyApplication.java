package com.example.watcher.ui;

import android.app.Application;
import android.content.Context;
import android.util.TypedValue;

import com.example.watcher.Retrofit.RetrofitUtil;

public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        RetrofitUtil.getInstance();
    }

    public static Context getContext() {
        return context;
    }

    //dp转换成像素
    public static int dp2px(int value)
    {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value,context.getResources().getDisplayMetrics() );
    }

    //像素转换成dp
    public static int px2dp(int value)
    {
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value,context.getResources().getDisplayMetrics() );
    }

}

package cn.cestco.titlebarlibrary;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

/**
 * Created by RockQ on 2017/11/7.
 * 功能：
 */

public class TitleBarApplication extends Application {

    private static Context mContext;
    private static Handler mHandler;
    private static int mMainThreadId;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        mHandler = new Handler();
        mMainThreadId = android.os.Process.myTid();
    }

    public static Context getContext() {
        return mContext;
    }

    public static Handler getHandler() {
        return mHandler;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }
}

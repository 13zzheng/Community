package com.bmob.lostfound;

import android.app.Application;
import android.content.Context;

import com.bmob.lostfound.mainactivity.MainActivity;

/**
 * Created by Administrator on 2016/9/11.
 */
public class MyApplication extends Application {
    private static Context context;
    @Override
    public void onCreate() {
        context=getApplicationContext();
    }
    public static Context getContext(){
        return context;
    }

}

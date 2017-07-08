package com.caro.smartmodule.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.caro.smartmodule.ConfigureManager;
import com.caro.smartmodule.helpers.ExceptionHelpers.ExceptionHandler;
import com.karumi.dexter.Dexter;


/**
 * Created by caro on 16/1/21.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
//        if (ConfigureManager.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
//        }
        super.onCreate();
        setContext(getApplicationContext());
        Dexter.initialize(this);//dex拆分
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

    }

    /**
     * Email Debug 内部异常报告统计
     * @param email devloper debug email.not null
     * @param password email password.not null
     * @param tips tips. etc:app name,username
     */
    public void setEmalDebugCaughtExceptionHandler(String email,String password,String tips){
        Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.getInstance(getApplicationContext(), email, password, tips));
    }


    /**
     * 设置context参数
     */
    private static Context context;
    public void setContext(Context mContext){
        this.context=mContext;
    }

    public static  Context getContext(){
        return context;
    }



}

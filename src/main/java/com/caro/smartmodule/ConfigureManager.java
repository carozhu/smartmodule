package com.caro.smartmodule;

/**
 * Created by caro on 16/6/28.
 *
 * just config you application setup
 */
public class ConfigureManager {
    public static final boolean DEVELOPER_MODE = false;
    private int appThemeColor = 0xFF469a84;//default themeColor
    private static ConfigureManager instance=null;
    private boolean okhttpCache = false;

    public static  ConfigureManager getConfigureManager(){
        if (instance == null){
            instance = new ConfigureManager();
        }

        return instance;
    }

    public void setAppThemeColor(int appThemeColor){
        this.appThemeColor = appThemeColor;
    }
    public int getAppThemeColor(){
        return appThemeColor;
    }



    public boolean isOkhttpCache() {
        return okhttpCache;
    }

    public void setOkhttpCache(boolean okhttpCache) {
        this.okhttpCache = okhttpCache;
    }
}

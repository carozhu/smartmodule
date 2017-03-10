package com.caro.smartmodule;

/**
 * Created by caro on 16/6/28.
 *
 * just config you application setup
 */
public class ConfigureManager {

    private int appThemeColor = 0xFF469a84;//default themeColor
    private static ConfigureManager instance=null;

    public static  ConfigureManager OnConfigureManager(){
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

}

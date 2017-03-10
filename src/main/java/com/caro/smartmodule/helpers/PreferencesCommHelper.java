package com.caro.smartmodule.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import com.caro.smartmodule.utils.AppInfoUtil;

/**
 * Created by caro on 16/3/8.
 * <p>
 * 用于保存用户登录账号密码和登录状态控制的 sp 简单工具类
 * 你还可以保存你想要保存的字段
 * xml名字:AppInfoUtil.getAppName(mContext),以context的app name为key
 * 所有需要有用户名和密码操作的都可以使用或集成该类
 */
public class PreferencesCommHelper {


    /**
     * 获得当前用户账户
     *
     * @param mContext
     * @return
     */
    public static String getCurUserCount(Context mContext) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        String s = sp.getString("userCount", "");
        return s;
    }


    /**
     * 保存的用户名是没有经过服务器处理的,是用户输入的简单的用户账户名
     *
     * 保存当前用户账户
     * @param mContext
     * @return
     */
    public static void saveCurUserCount(Context mContext, String username) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putString("userCount", username).commit();

    }

    /**
     * get string from sp
     *
     * @param mContext
     * @return
     */
    public static String getCurUsernickname(Context mContext) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        String s = sp.getString("nickname", "");
        return s;
    }


    /**
     * 保存的用户名是没有经过服务器处理的,是用户输入的简单的用户账户名
     *
     * @param mContext
     * @return
     */
    public static void saveCurUsernickname(Context mContext, String nickname) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putString("nickname", nickname).commit();

    }



    /**
     * @param mContext
     * @return
     */
    public static void saveCurUserpwd(Context mContext, String passwd) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        String userAcount = getCurUserCount(mContext);
        sp.edit().putString(userAcount+"_"+"pwd", passwd).commit();

    }


    /**
     * get string from sp
     *
     * @param mContext
     * @return
     */
    public static String getCurUserPwd(Context mContext) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        String userAcount = getCurUserCount(mContext);
        String s = sp.getString(userAcount+"_"+"pwd", "");
        return s;
    }


    /**
     * save user login historey to sp
     *
     * @param mContext
     * @param regstate    bln login success
     */
    public static void saveCurUsreRegistionState(Context mContext, Boolean regstate) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putBoolean(getCurUserCount(mContext) + "loginStatte", regstate).commit();

    }


    /**
     * get boolean from sp
     *
     * @param mContext
     * @return
     */
    public static boolean getCurUsreRegistionState(Context mContext) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        String username = getCurUserCount(mContext);
        //if (username.equals("")){
        //    throw new RuntimeException("please first save your usename param in sp");
        //}
        boolean i = sp.getBoolean(username + "loginStatte", false);
        return i;
    }

    /**
     * save stringFiled refre field to sp
     * <p>
     * 当前仅用来保存sid
     *
     * @param mContext
     * @param field
     * @param value
     */
    public static void savefieldtoSharePre(Context mContext, String field, String value) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putString(field, value).commit();
    }


    /**
     * get stringFiled from sp
     *
     * @param mContext
     * @return
     */
    public static String getValuewithField(Context mContext, String filed) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        String stringFiled = sp.getString(filed, "");
        return stringFiled;
    }


    /**
     * save stringFiled refre field to sp
     * <p>
     * 当前仅用来保存sid
     *
     * @param mContext
     * @param field
     * @param value
     */
    public static void savefieldtoSharePre(Context mContext, String field, boolean value) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putBoolean(field, value).commit();
    }


    /**
     * get stringFiled from sp
     *
     * @param mContext
     * @return
     */
    public static boolean getBooleanValuewithField(Context mContext, String filed) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        Boolean stringFiled = sp.getBoolean(filed, false);
        return stringFiled;
    }

    //////////////////////////////////////////
    public static void saveLongfieldtoSharePre(Context mContext, String field, long value) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putLong(field, value).commit();
    }


    /**
     * get stringFiled from sp
     *
     * @param mContext
     * @return
     */
    public static long getLongValuewithField(Context mContext, String filed) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        long longFiled = sp.getLong(filed, 0);
        return longFiled;
    }


    public static void saveIntfieldtoSharePre(Context mContext, String field, int value) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        sp.edit().putInt(field, value).commit();
    }


    /**
     * get stringFiled from sp
     *
     * @param mContext
     * @return
     */
    public static int getIntValuewithField(Context mContext, String filed) {
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        int longFiled = sp.getInt(filed, 0);
        return longFiled;
    }

    public static void cleanSpFileld(Context mContext,String filed){
        SharedPreferences sp = (SharedPreferences) mContext.getSharedPreferences(AppInfoUtil.getAppName(mContext), 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(filed);
        editor.commit();
    }

}

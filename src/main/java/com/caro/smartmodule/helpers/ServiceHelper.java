package com.caro.smartmodule.helpers;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by caro on 16/9/26.
 */
public class ServiceHelper {

    /**
     * 判断服务是否启动, 注意只要名称相同, 会检测任何服务.
     *
     * @param context      上下文
     * @param serviceClass 服务类
     * @return 是否启动服务
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        if (context == null) {
            return false;
        }

        Context appContext = context.getApplicationContext();
        ActivityManager manager = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            List<ActivityManager.RunningServiceInfo> infos = manager.getRunningServices(Integer.MAX_VALUE);
            if (infos != null && !infos.isEmpty()) {
                for (ActivityManager.RunningServiceInfo service : infos) {
                    // 添加Uid验证, 防止服务重名, 当前服务无法启动
                    if (getUid(context) == service.uid) {
                        if (serviceClass.getName().equals(service.service.getClassName())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 获取应用的Uid, 用于验证服务是否启动
     * @param context 上下文
     * @return uid
     */
    public static int getUid(Context context) {
        if (context == null) {
            return -1;
        }

        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        if (manager != null) {
            List<ActivityManager.RunningAppProcessInfo> infos = manager.getRunningAppProcesses();
            if (infos != null && !infos.isEmpty()) {
                for (ActivityManager.RunningAppProcessInfo processInfo : infos) {
                    if (processInfo.pid == pid) {
                        return processInfo.uid;
                    }
                }
            }
        }
        return -1;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName 是包名+服务的类名
     * @return true代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }


}

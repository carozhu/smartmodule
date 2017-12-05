package com.caro.smartmodule.helpers;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;

public class ActivityManageHelper {

    private LinkedList<Activity> activityLinkedList = new LinkedList<>();
    private static class ActivityManageHelperHolder{
        public  static   final  ActivityManageHelper INSTANCE = new ActivityManageHelper();
    }

    private ActivityManageHelper() {
    }


    public static ActivityManageHelper getInstance(){
        return ActivityManageHelperHolder.INSTANCE;
    }

    /**
     * 向list中添加Activity
     *
     * @param activity
     * @return
     */
    public ActivityManageHelper addActivity(Activity activity){
        activityLinkedList.add(activity);
        return this;
    }

    /**
     *     结束特定的Activity(s)
     */
    public ActivityManageHelper finshActivities(Class<? extends Activity>... activityClasses){
        for (Activity activity : activityLinkedList) {
            if( Arrays.asList(activityClasses).contains( activity.getClass() ) ){
                activity.finish();
            }
        }
        return this;
    }

    /**
     * 结束所有的Activities
     */
    public ActivityManageHelper finshAllActivities() {
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
        return this;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        if (activityLinkedList.size() == 0)
            return null;
        Activity activity=activityLinkedList.get(activityLinkedList.size()-1);
        return activity;
    }

    /**
     * 是否包含当前activity
     * @param activityClasses
     * @return
     */
    public boolean haveActivity(Class<? extends Activity>... activityClasses){
        for (Activity activity : activityLinkedList) {
            if( Arrays.asList(activityClasses).contains( activity.getClass() ) ){
                return true;
            }
        }
        return false;
    }
}

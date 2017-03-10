package com.caro.smartmodule.helpers;

import android.app.Activity;

import java.util.Arrays;
import java.util.LinkedList;

public class ActivityManageHelper {

    private LinkedList<Activity> activityLinkedList = new LinkedList<Activity>();

    private ActivityManageHelper() {
    }

    private static ActivityManageHelper instance;

    public static ActivityManageHelper getInstance(){
        if(null == instance){
            instance = new ActivityManageHelper();
        }
        return instance;
    }

    //向list中添加Activity
    public ActivityManageHelper addActivity(Activity activity){
        activityLinkedList.add(activity);
        return instance;
    }

    //结束特定的Activity(s)
    public ActivityManageHelper finshActivities(Class<? extends Activity>... activityClasses){
        for (Activity activity : activityLinkedList) {
            if( Arrays.asList(activityClasses).contains( activity.getClass() ) ){
                activity.finish();
            }
        }
        return instance;
    }

    //结束所有的Activities
    public ActivityManageHelper finshAllActivities() {
        for (Activity activity : activityLinkedList) {
            activity.finish();
        }
        return instance;
    }
}

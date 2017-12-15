package com.caro.smartmodule.helpers;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

/**
 * 参考来自 http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2017/0629/8124.html
 */
public class ActivityManageHelper {
    private static class ActivityManageHelperHolder{
        public  static   final  ActivityManageHelper INSTANCE = new ActivityManageHelper();
    }
    private ActivityManageHelper() {}
    public static ActivityManageHelper getInstance(){
        return ActivityManageHelperHolder.INSTANCE;
    }

    private Stack<WeakReference<Activity>> mActivityStack;

    /**
     * 向list中添加Activity
     * @param activity
     * @return
     */
    public ActivityManageHelper addActivity(Activity activity){
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(new WeakReference<>(activity));
        return this;
    }

    /**
     *     结束特定的Activity(s)
     */
    //public ActivityManageHelper finshActivities(Class<? extends Activity>... activityClasses)
    public ActivityManageHelper finshActivities(Activity activity) {

        if (activity != null && mActivityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity temp = activityReference.get();
                // 清理掉已经释放的activity
                if (temp == null) {
                    it.remove();
                    continue;
                }
                if (temp == activity) {
                    it.remove();
                }
            }
            activity.finish();
        }
        return this;
    }

    /**
     * 关闭指定类名的Activity
     * @param cls
     */
    public void finshActivities(Class<?> cls) {
        if (mActivityStack != null && cls!=null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity activity = activityReference.get();
                // 清理掉已经释放的activity
                if (activity == null) {
                    it.remove();
                    continue;
                }
                if (activity.getClass().equals(cls)) {
                    it.remove();
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束所有的Activities
     */
    public ActivityManageHelper finshAllActivities() {
        if (mActivityStack != null) {
            for (WeakReference<Activity> activityReference : mActivityStack) {
                Activity activity = activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivityStack.clear();
        }
        return this;
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity(){
        checkWeakReference();
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            return mActivityStack.lastElement().get();
        }
        return null;
    }

    /**
     * 检查弱引用是否释放，若释放，则从栈中清理掉该元素
     */
    private void checkWeakReference() {
        if (mActivityStack != null) {
            // 使用迭代器进行安全删除
            for (Iterator<WeakReference<Activity>> it = mActivityStack.iterator(); it.hasNext(); ) {
                WeakReference<Activity> activityReference = it.next();
                Activity temp = activityReference.get();
                if (temp == null) {
                    it.remove();
                }
            }
        }
    }

    /**
     * 是否包含当前activity
     * @param activityClasses
     * @return
     */
    public boolean haveActivity(Class<? extends Activity>... activityClasses){
        if (mActivityStack != null && activityClasses!=null) {
            for (WeakReference<Activity> activityReference : mActivityStack) {
                Activity activity = activityReference.get();
                if( Arrays.asList(activityClasses).contains( activity.getClass() ) ){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 退出应用程序
     */
    public void exitApp() {
        try {
            finishAllActivity();
            // 退出JVM,释放所占内存资源,0表示正常退出
            System.exit(0);
            // 从系统中kill掉应用程序
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (mActivityStack != null) {
            for (WeakReference<Activity> activityReference : mActivityStack) {
                Activity activity = activityReference.get();
                if (activity != null) {
                    activity.finish();
                }
            }
            mActivityStack.clear();
        }
    }
}

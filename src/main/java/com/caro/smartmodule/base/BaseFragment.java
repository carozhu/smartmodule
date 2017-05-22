package com.caro.smartmodule.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.caro.smartmodule.R;
import com.caro.smartmodule.helpers.WeakHandler;


import java.lang.reflect.Field;

/**
 * Created by caro on 16/1/21.
 * 声明:对以下可见与不可见的处理只适合于Fragment的父容器是Activity的情况下.不适合于父容器是Fragment的
 *
 */
public abstract class BaseFragment extends Fragment{
    private String TAG = getClass().getSimpleName();
    public Activity activity;
    public Context context;
    protected LayoutInflater inflater;
    private View contentView;
    private ViewGroup container;
    private Dialog LoadingDialog;


    /**
     * Notice 只限于在viewpager时使用
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onUserVisible();
        }else{
            onUserInVisible();
        }
    }


    /**
     * can lazy load view
     */
    protected  void onUserVisible(){};
    protected  void onUserInVisible(){};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        context = getActivity();


    }



    @Override
    public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        this.container = container;
        onLifeCreateView(savedInstanceState);
        if (contentView == null){
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        Log.d(TAG, "BaseFragment,onCreated: ");
        return contentView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "BaseFragment,onActivityCreated: ");

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "fragment onResume: ");

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }
    @Override
    public void onStop(){
        super.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    /**
     *
     * @param backResID
     */
    public void setCanBack(int backResID) {
        View back_btn = (View) findViewById(backResID);
        if (back_btn != null) {
            back_btn.setVisibility(View.VISIBLE);
            back_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    activity.finish();
                }
            });
        }
    }
    /**
     * just inflate your layout
     * @param savedInstanceState
     */
    protected abstract void onLifeCreateView(Bundle savedInstanceState);

    public void setContentView(int layoutResID) {
        try {
            setContentView((ViewGroup) inflater.inflate(layoutResID, container, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentView(View view) {
        contentView = view;
    }

    public View getContentView() {
        return contentView;
    }

    public View findViewById(int id) {
        if (contentView != null)
            return contentView.findViewById(id);
        return null;
    }

    /**
     *
     * @param id
     * @param <VT> View
     * @return
     */
    protected <VT extends View> VT getViewById(@IdRes int id) {
        return (VT) contentView.findViewById(id);
    }

    // http://stackoverflow.com/questions/15207305/getting-the-error-java-lang-illegalstateexception-activity-has-been-destroyed
    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public WeakHandler getWeakHandler() {
        return weakHandler;
    }

    /**
     * Avoid memory leaks
     * WeakHandler
     */
    public WeakHandler weakHandler = new WeakHandler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            onHandleMessage(msg);

        }
    };


    /**
     * Events Message Queue
     *
     * @param msg
     */
    protected void onHandleMessage(final Message msg) {

    }

    public void showLoadingDialog(String tips) {
        if (LoadingDialog != null) {
            LoadingDialog.show();
        } else {
            LoadingDialog = com.caro.smartmodule.common.loadingmanager.LoadingDialog.createLoadingDialog(context, tips);
            LoadingDialog.show();
        }
    }

    /**
     *
     */
    public void dimissLoadingDialog() {
        if (LoadingDialog != null) {
            LoadingDialog.dismiss();
            LoadingDialog = null;
        }
    }

    /**
     * just jump a sample activity
     * @param context
     * @param distClass
     */
    public void startActivity(Context context, Class<?> distClass) {
        Intent in = new Intent(context, distClass);
        startActivity(in);
    }

    /** 含有Bundle通过Class跳转界面 **/
    public static void startActivity(Context mContext,Class<?> toCls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, toCls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);

    }


    public View getRootView(){

        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void showDefaultSnackbar(String tips){
        Snackbar mysnackbar  =    Snackbar.make(getRootView(),tips,Snackbar.LENGTH_SHORT);
        View view = mysnackbar.getView();
        if (view!=null){
            view.setBackgroundColor(ContextCompat.getColor(context,R.color.md_blue_grey_500));
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(context,R.color.white));
        }
        mysnackbar.show();
    }


    /**
     * Android M运行时权限请求封装
     * @param permissionDes 权限描述
     * @param runnable 请求权限回调
     * @param permissions 请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
     */
    public void performCodeWithPermission(@NonNull String permissionDes,BaseSimpleAppCompatActivity.PermissionCallback runnable,@NonNull String... permissions){
        if(getActivity()!=null && getActivity() instanceof BaseSimpleAppCompatActivity){
            ((BaseSimpleAppCompatActivity) getActivity()).performCodeWithPermission(permissionDes,runnable,permissions);
        }
    }
}

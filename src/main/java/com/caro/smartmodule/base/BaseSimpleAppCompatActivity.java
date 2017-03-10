package com.caro.smartmodule.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import com.caro.smartmodule.R;
import com.caro.smartmodule.helpers.ActivityManageHelper;
import com.caro.smartmodule.helpers.WeakHandler;
import com.caro.smartmodule.utils.StateBarTranslucentUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.logging.Level;

import library.StatusBarUtil;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.Utils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityBase;
import me.imid.swipebacklayout.lib.app.SwipeBackActivityHelper;

import static com.caro.smartmodule.common.loadingmanager.LoadingDialog.createLoadingDialog;


/**
 * 使用事项注意:如果需要viewpager中的fragment布局嵌套到hint里面...setStatusBar
 */
public abstract class BaseSimpleAppCompatActivity extends AppCompatActivity implements SwipeBackActivityBase {
    // 右滑返回
    private SwipeBackLayout mSwipeBackLayout;
    private SwipeBackActivityHelper mHelper;
    public SystemBarTintManager mTintManager;
    public Activity activity;
    public Context context;
    private Dialog LoadingDialog;
    private Toolbar mToolbar;
    protected ActionBar actionBar = null;
    private String themeColor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        context = this;
        /*must init helper*/
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        setStatusBar();
        swipeback(false);
        ActivityManageHelper.getInstance().addActivity(this);

    }

    public void setRightTitle(String rtitle){
        TextView rightTitle = (TextView)findViewById(R.id.rightTitle);
        if (rightTitle!=null){
            rightTitle.setText(rtitle);
            rightTitle.setVisibility(View.VISIBLE);
        }
    }

    public TextView getRightTitleView(){
        TextView rightTitle = (TextView)findViewById(R.id.rightTitle);
        if (rightTitle!=null){
            return  rightTitle;
        }

        return null;
    }

    public void swipeback(boolean swipebace) {
        initViewStateBar(R.color.default_theme_app_color);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEnableGesture(swipebace);
    }

    public void swipeback(boolean swipebace, String primarycolor) {
        initViewGuideStateBar(primarycolor);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
        mSwipeBackLayout = getSwipeBackLayout();
        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
        mSwipeBackLayout.setEnableGesture(swipebace);

        themeColor = primarycolor;
    }

    /**
     * you must set the state bar
     * if you what to use swipe finish activity function.
     *
     * @param color
     */
    public void initViewStateBar(int color) {

        //设置状态栏透明
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        //状态栏着色
        StateBarTranslucentUtils.setStateBarColor(this, color);


    }

    public void initViewGuideStateBar(String color) {

        //设置状态栏透明
        StateBarTranslucentUtils.setStateBarTranslucent(this);
        //状态栏着色
        StateBarTranslucentUtils.setStateBarColor(this, color);

    }

    protected void setStatusBar() {
        StatusBarUtil.setColor(this, Color.parseColor("#146e6e"));
    }

    protected void setStatusBar(String color) {
        StatusBarUtil.setColor(this, Color.parseColor(color));
    }

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }


    /**
     * Events Message Queue
     *
     * @param msg
     */
    protected void onHandleMessage(final Message msg) {

    }

    public void resetFragmentView(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View contentView = findViewById(android.R.id.content);
            if (contentView != null) {
                ViewGroup rootView;
                rootView = (ViewGroup) ((ViewGroup) contentView).getChildAt(0);
                if (rootView.getPaddingTop() != 0) {
                    rootView.setPadding(0, 0, 0, 0);
                }
            }
            if (fragment.getView() != null)
                fragment.getView().setPadding(0, getStatusBarHeight(this), 0, 0);
        }
    }

    private static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * @param resColorID
     */
    public void setStatusBarTintResource(int resColorID) {

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        activity = this;
        context = this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        if (mTintManager == null) {
            mTintManager = new SystemBarTintManager(this);
            mTintManager.setStatusBarTintEnabled(true);

        }
        mTintManager.setStatusBarTintResource(resColorID);

    }


    /**
     * @param enabled
     */
    public void setStatusBarTintEnabled(boolean enabled) {
        if (mTintManager == null) {
            mTintManager = new SystemBarTintManager(this);
        }

        mTintManager.setStatusBarTintEnabled(enabled);
    }


    /**
     * @param statusBarTintDrawable
     */
    public void setStatusBarTintDrawable(Drawable statusBarTintDrawable) {
        if (mTintManager == null) {
            mTintManager = new SystemBarTintManager(this);
        }

        mTintManager.setStatusBarTintDrawable(statusBarTintDrawable);
    }


    public void showLoadingDialog(String tips) {
        if (LoadingDialog != null) {
            LoadingDialog.show();
        } else {
            LoadingDialog = createLoadingDialog(this, tips);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManageHelper.getInstance().finshActivities(BaseSimpleAppCompatActivity.class);
    }

    @Override
    public void onBackPressed() {
        onSwitchBackPressed();

    }

    /**
     * if you will handle back key,plsease override this fuction
     */
    public void onSwitchBackPressed() {
        finish();

    }

    /**
     * just jump a sample activity
     *
     * @param context
     * @param distClass
     */
    public void startActivity(Context context, Class<?> distClass) {
        Intent in = new Intent(context, distClass);
        startActivity(in);
    }

    /**
     * 含有Bundle通过Class跳转界面
     **/
    public static void startActivity(Context mContext, Class<?> toCls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mContext, toCls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        mContext.startActivity(intent);


    }

    public View getRootView() {

        return activity.getWindow().getDecorView().findViewById(android.R.id.content);
    }

    public void showDefaultSnackbar(String tips) {

        Snackbar mysnackbar = Snackbar.make(getRootView(), tips, Snackbar.LENGTH_SHORT);
        View view = mysnackbar.getView();
        if (view != null) {
            view.setBackgroundColor(ContextCompat.getColor(context, R.color.md_blue_grey_500));
            ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        mysnackbar.show();
    }


    /******
     * Android M Permission (Android 6.0权限控制代码封装)
     * 参考:http://www.jianshu.com/p/d3a998ec04ad
     * http://droidyue.com/blog/2016/01/17/understanding-marshmallow-runtime-permission/index.html
     * https://yanlu.me/android-6-0-permission-library/
     * http://jijiaxin89.com/2015/08/30/Android-s-Runtime-Permission/
     * http://blog.metova.com/android-marshmallow-permissions/
     *****/
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public String READ_EXTERNAL_STORAGE_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    public String WRITE_EXTERNAL_STORAGE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public String CAMETA_PERMISSION = Manifest.permission.CAMERA;
    private int permissionRequestCode = 88;
    private PermissionCallback permissionRunnable;

    public interface PermissionCallback {
        void hasPermission();

        void noPermission();
    }

    /**
     * Android M运行时权限请求封装
     *
     * @param permissionDes 权限描述
     * @param runnable      请求权限回调
     * @param permissions   请求的权限（数组类型），直接从Manifest中读取相应的值，比如Manifest.permission.WRITE_CONTACTS
     */
    public void performCodeWithPermission(@NonNull String permissionDes, PermissionCallback runnable, @NonNull String... permissions) {
        if (permissions == null || permissions.length == 0) return;
        this.permissionRunnable = runnable;
        if ((Build.VERSION.SDK_INT < Build.VERSION_CODES.M) || checkPermissionGranted(permissions)) {
            if (permissionRunnable != null) {
                permissionRunnable.hasPermission();
                permissionRunnable = null;
            }
        } else {
            //permission has not been granted.
            requestPermission(permissionDes, permissionRequestCode, permissions);
        }

    }

    private boolean checkPermissionGranted(String[] permissions) {
        boolean flag = true;
        for (String p : permissions) {
            if (!selfPermissionGranted(p)) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    public boolean selfPermissionGranted(String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                final PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                int targetSdkVersion = info.applicationInfo.targetSdkVersion;
                if (targetSdkVersion >= Build.VERSION_CODES.M) {
                    // targetSdkVersion >= Android M, we can
                    // use Context#checkSelfPermission
                    result = context.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
                } else {
                    // targetSdkVersion < Android M, we have to use PermissionChecker
                    result = PermissionChecker.checkSelfPermission(context, permission) == PermissionChecker.PERMISSION_GRANTED;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }

        return result;
    }

    private void requestPermission(String permissionDes, final int requestCode, final String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.

            /*Snackbar.make(getWindow().getDecorView(), requestName,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.common_ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat.requestPermissions(BaseAppCompatActivity.this,
                                    permissions,
                                    requestCode);
                        }
                    })
                    .show();*/
            //如果用户之前拒绝过此权限，再提示一次准备授权相关权限
            new AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage(permissionDes)
                    .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseSimpleAppCompatActivity.this, permissions, requestCode);
                        }
                    }).show();

        } else {
            // Contact permissions have not been granted yet. Request them directly.
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == permissionRequestCode) {
            if (verifyPermissions(grantResults)) {
                if (permissionRunnable != null) {
                    permissionRunnable.hasPermission();
                    permissionRunnable = null;
                }
            } else {
                //showToast("暂无权限执行相关操作！");
                if (permissionRunnable != null) {
                    permissionRunnable.noPermission();
                    permissionRunnable = null;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**********************
     * END Android M Permission
     ****************************************/


    /**
     *  针对是Custom title bar 的布局
     * @param resid
     * @param indicator
     * @param toolbarcolor
     */
    public void initializeToolbar(@Nullable int resid, Drawable indicator,String toolbarcolor) {
        mToolbar = (Toolbar) findViewById(resid);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            mToolbar.setBackgroundColor(Color.parseColor(toolbarcolor));
        }
        this.actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(indicator);
            actionBar.setDisplayShowHomeEnabled(true); // show or hide the default home button
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
            actionBar.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
            setBackTextClickable();
            setTitle(getTitle());

        }
    }

    /**
     * 针对不是Custom title bar 的布局
     * @param resid
     * @param indicator
     */
    public void initializeToolbar(@Nullable int resid, Drawable indicator) {
        mToolbar = (Toolbar) findViewById(resid);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        this.actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (indicator!=null) {
                actionBar.setHomeAsUpIndicator(indicator);
                actionBar.setDisplayShowHomeEnabled(true); // show or hide the default home button
                actionBar.setDisplayHomeAsUpEnabled(true);
            }else {
                actionBar.setDisplayShowHomeEnabled(false); // show or hide the default home button
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
            actionBar.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
            actionBar.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
            setBackTextClickable();
            setTitle(getTitle());

        }
    }



    /**
     * 设置Title
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        TextView titleView = (TextView) findViewById(R.id.toolbar_title);
        if (titleView != null) {
            titleView.setText(title);
        }
    }

    /**
     * If the back text is clicked then it should react as back button.
     */
    private void setBackTextClickable() {

        TextView backView = (TextView) findViewById(R.id.toolbar_back);
        if (backView != null) {
            backView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    /**
     * Set custom back button text.
     *
     * @param backText value to be set.
     */
    public void setBackText(CharSequence backText) {

        TextView backTv = (TextView) findViewById(R.id.toolbar_back);
        if (backTv != null) {
            backTv.setText(backText);
        }
    }


}

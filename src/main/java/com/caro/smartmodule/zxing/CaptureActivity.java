package com.caro.smartmodule.zxing;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.caro.smartmodule.ConfigureManager;
import com.caro.smartmodule.R;
import com.caro.smartmodule.base.BaseSimpleAppCompatActivity;
import com.caro.smartmodule.helpers.ActivityManageHelper;
import com.caro.smartmodule.utils.DisplayMetricsUtil;
import com.caro.smartmodule.zxing.camera.BeepManager;
import com.caro.smartmodule.zxing.camera.CameraManager;
import com.caro.smartmodule.zxing.decode.CaptureActivityHandler;
import com.caro.smartmodule.zxing.decode.DecodeThread;
import com.caro.smartmodule.zxing.decode.FinishListener;
import com.caro.smartmodule.zxing.decode.InactivityTimer;
import com.caro.smartmodule.zxing.view.ViewfinderView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Map;
import java.util.Vector;


/**
 * 改进优化,caro zhu
 * 条码二维码扫描功能实现
 * <p>
 * usage:
 * Intent intent = new Intent();
 * intent.setClass(context,CaptureActivity.class);
 * context.startActivityForResult(intent,200);
 */
public class CaptureActivity extends BaseSimpleAppCompatActivity implements SurfaceHolder.Callback {
    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static String QRCODETYPE = "qrcode";//default
    public static String ONECODETYPE = "onecode";

    private boolean hasSurface;
    private BeepManager beepManager;// 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
    public SharedPreferences mSharedPreferences;// 存储二维码条形码选择的状态
    public static String currentState;// 条形码二维码选择状态
    private String characterSet;

    private ViewfinderView viewfinderView;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private TextView scantipsTv;
    private TextView titleTv;
    private ImageView onecode;
    private ImageView qrcode;

    private String title;//titlebar 标题
    private String tips;//用户提示
    private Class<? extends Activity> destClass;

    /**
     * 活动监控器，用于省电，如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
     * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
     */
    private InactivityTimer inactivityTimer;
    private CameraManager cameraManager;
    private Vector<BarcodeFormat> decodeFormats;// 编码格式
    private CaptureActivityHandler mHandler;// 解码线程

    private static final Collection<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet
            .of(ResultMetadataType.ISSUE_NUMBER,
                    ResultMetadataType.SUGGESTED_PRICE,
                    ResultMetadataType.ERROR_CORRECTION_LEVEL,
                    ResultMetadataType.POSSIBLE_COUNTRY);


    private ActivityManageHelper activityManager;


    /**
     * @param activity
     * @param requestCode
     * @param scanType
     * @param title
     * @param tips
     * @param destClass,maybe null.if not null.please let your destClass maybe implents Serializable
     */
    public static void startCaptureActivity(Activity activity,
                                            int requestCode,
                                            String scanType,
                                            String title,
                                            String tips,
                                            int stateBarColorID,
                                            String stateBarColorstr,
                                            Class<? extends Activity> destClass) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("scanType", scanType);
        bundle.putString("title", title);
        bundle.putString("tips", tips);
        bundle.putInt("stateBarColorID", stateBarColorID);
        bundle.putString("stateBarColorstr", stateBarColorstr);
        bundle.putSerializable("destClass", destClass);
        intent.putExtras(bundle);
        intent.setClass(activity, CaptureActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_captrure);

        initViewStateBar(R.color.theme_delivery_color);
        swipeback(false, getResources().getString(R.string.theme_delivery_color));
        activityManager = ActivityManageHelper.getInstance();
        activityManager.addActivity(CaptureActivity.this);
        setStatusBar(getResources().getString(R.string.theme_delivery_color));

        setCanBack(R.id.back);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        scantipsTv = (TextView) findViewById(R.id.scan_tips);
        titleTv = (TextView) findViewById(R.id.titlebar_title);
        onecode = (ImageView) findViewById(R.id.onecode_id);
        qrcode = (ImageView) findViewById(R.id.qrcode_id);
        qrcode.setBackgroundResource(R.drawable.scan_qr_hl);

        initEvent();
        initComponent();
    }


    /**
     * 主要对相机进行初始化工作
     */
    @Override
    protected void onResume() {
        super.onResume();
        inactivityTimer.onActivity();
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        int width = DisplayMetricsUtil.getScreenWidth(context);
        viewfinderView.setCameraManager(cameraManager);
        surfaceHolder = surfaceView.getHolder();
        setScanType();
        resetStatusView();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            // 如果SurfaceView已经渲染完毕，会回调surfaceCreated，在surfaceCreated中调用initCamera()
            surfaceHolder.addCallback(this);
        }
        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();
        // 恢复活动监控器
        inactivityTimer.onResume();

        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            qrcodeSetting();
        } else {
            String scantype = bundle.getString("scanType");
            if (scantype.equalsIgnoreCase(ONECODETYPE)) {
                viewfinderView.setVisibility(View.VISIBLE);
                currentState = "onecode";
                onecodeSetting();
            } else {
                currentState = QRCODETYPE;
            }

            title = bundle.getString("title");
            tips = bundle.getString("tips");

            if (!TextUtils.isEmpty(title)) {
                titleTv.setText(title);
            }
            if (!TextUtils.isEmpty(tips)) {
                scantipsTv.setText(tips);
            }


            int stateColorId = bundle.getInt("stateBarColorID");
            if (stateColorId != 0) {
                initViewStateBar(stateColorId);
                findViewById(R.id.titleparent).setBackgroundResource(stateColorId);
            }

            String stateColorStr = bundle.getString("stateBarColorstr");
            if (!TextUtils.isEmpty(stateColorStr)) {
                swipeback(false, stateColorStr);
            }
            destClass = (Class<? extends Activity>) bundle.getSerializable("destClass");


        }
    }

    /**
     * 初始化窗口设置
     */
    private void initSetting() {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 保持屏幕处于点亮状态
        // window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 隐藏标题栏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 竖屏
    }

    /**
     * 初始化功能组件
     */
    private void initComponent() {
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        currentState = this.mSharedPreferences.getString("currentState", "qrcode");
        cameraManager = new CameraManager(getApplication());
    }


    /**
     * 初始化点击切换扫描类型事件
     */
    private void initEvent() {
        onecode.setOnClickListener(this.onecodeImageListener);
        qrcode.setOnClickListener(this.qrcodeImageListener);
        qrcode.setSelected(true);
    }

    /**
     * 初始设置扫描类型（最后一次使用类型）
     */
    private void setScanType() {
        do {
            if ((CaptureActivity.currentState != null)
                    && (CaptureActivity.currentState.equals("onecode"))) {
                qrcode.setBackgroundResource(R.drawable.scan_qr);
                onecode.setBackgroundResource(R.drawable.scan_store_hl);
                qrcode.setSelected(false);
                onecode.setSelected(true);
                viewfinderView.setVisibility(View.VISIBLE);
                onecodeSetting();
                //statusView.setText(R.string.scan_qrcode);
                return;
            }
        }

        while ((CaptureActivity.currentState == null) || (!CaptureActivity.currentState.equals("qrcode")));
        onecode.setBackgroundResource(R.drawable.scan_store);
        qrcode.setBackgroundResource(R.drawable.scan_qr_hl);
        qrcode.setSelected(true);
        onecode.setSelected(false);
        viewfinderView.setVisibility(View.VISIBLE);
        qrcodeSetting();
        //statusView.setText(R.string.scan_qrcode);
    }

    /**
     * 暂停活动监控器,关闭摄像头
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (mHandler != null) {
            mHandler.quitSynchronously();
            mHandler = null;
        }
        // 暂停活动监控器
        inactivityTimer.onPause();
        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    /**
     * 展示状态视图和扫描窗口，隐藏结果视图
     */
    private void resetStatusView() {
        scantipsTv.setVisibility(View.VISIBLE);
        viewfinderView.setVisibility(View.VISIBLE);
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    /**
     * 初始化摄像头。打开摄像头，检查摄像头是否被开启及是否被占用
     *
     * @param surfaceHolder
     */
    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the mHandler starts the preview, which can also throw a
            // RuntimeException.
            if (mHandler == null) {
                mHandler = new CaptureActivityHandler(this, decodeFormats,
                        characterSet, cameraManager);
            }
            // decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 若摄像头被占用或者摄像头有问题则跳出提示对话框
     */
    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon(R.drawable.launcher_icon);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }


    /**
     * 停止活动监控器,保存最后选中的扫描类型
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 停止活动监控器
        inactivityTimer.shutdown();
        saveScanTypeToSp();
    }


    /**
     * 保存退出进程前选中的二维码条形码的状态
     */
    private void saveScanTypeToSp() {
        SharedPreferences.Editor localEditor = this.mSharedPreferences.edit();
        localEditor.putString("currentState", CaptureActivity.currentState);
        localEditor.commit();
    }

    /**
     * 获取扫描结果
     *
     * @param rawResult
     * @param barcode
     * @param scaleFactor
     */
    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();

        boolean fromLiveScan = barcode != null;
        if (fromLiveScan) {

            // Then not from history, so beep/vibrate and we have an image to
            // draw on
            beepManager.playBeepSoundAndVibrate();
            drawResultPoints(barcode, scaleFactor, rawResult);
        }
        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT);
        Map<ResultMetadataType, Object> metadata = rawResult
                .getResultMetadata();
        StringBuilder metadataText = new StringBuilder(20);
        if (metadata != null) {
            for (Map.Entry<ResultMetadataType, Object> entry : metadata
                    .entrySet()) {
                if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
                    metadataText.append(entry.getValue()).append('\n');
                }
            }
            if (metadataText.length() > 0) {
                metadataText.setLength(metadataText.length() - 1);
            }
        }
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", barcode);
        bundle.putString("barcodeFormat", rawResult.getBarcodeFormat().toString());
        bundle.putString("decodeDate", formatter.format(new Date(rawResult.getTimestamp())));
        bundle.putCharSequence("metadataText", metadataText);
        bundle.putString("resultString", rawResult.getText());//scan result
        intent.putExtras(bundle);
        if (destClass != null) {
            intent.setClass(context, destClass);
            startActivity(intent);
            finish();
        } else {
            setResult(200, intent);
            finish();
        }

    }

    /**
     * 在扫描图片结果中绘制绿色的点
     *
     * @param barcode
     * @param scaleFactor
     * @param rawResult
     */
    private void drawResultPoints(Bitmap barcode, float scaleFactor,
                                  Result rawResult) {
        ResultPoint[] points = rawResult.getResultPoints();
        if (points != null && points.length > 0) {
            Canvas canvas = new Canvas(barcode);
            Paint paint = new Paint();
            paint.setColor(ConfigureManager.getConfigureManager().getAppThemeColor());
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
            } else if (points.length == 4
                    && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
                    .getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                drawLine(canvas, paint, points[0], points[1], scaleFactor);
                drawLine(canvas, paint, points[2], points[3], scaleFactor);
            } else {
                paint.setStrokeWidth(10.0f);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(scaleFactor * point.getX(),
                                scaleFactor * point.getY(), paint);
                    }
                }
            }
        }
    }

    /**
     * 在扫描图片结果中绘制绿色的线
     *
     * @param canvas
     * @param paint
     * @param a
     * @param b
     * @param scaleFactor
     */
    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
                                 ResultPoint b, float scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(scaleFactor * a.getX(), scaleFactor * a.getY(),
                    scaleFactor * b.getX(), scaleFactor * b.getY(), paint);
        }
    }


    /**
     * 点击响应条形码扫描
     */
    private OnClickListener onecodeImageListener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {

            qrcode.setBackgroundResource(R.drawable.scan_qr);
            onecode.setBackgroundResource(R.drawable.scan_store_hl);
            qrcode.setSelected(false);
            onecode.setSelected(true);
            //statusView.setText(R.string.scan_qrcode);
            viewfinderView.setVisibility(View.VISIBLE);
            currentState = "onecode";
            onecodeSetting();

        }
    };

    private void onecodeSetting() {
        decodeFormats = new Vector<BarcodeFormat>(7);
        decodeFormats.clear();
        decodeFormats.addAll(DecodeThread.ONE_D_FORMATS);
        // TODO: 16/1/29 set select qrcode mode title
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }

        viewfinderView.refreshDrawableState();
        //cameraManager.setManualFramingRect(360, 222);
        int width = DisplayMetricsUtil.getScreenWidth(context);
        cameraManager.setManualFramingRect((width / 2) + 120, ((width / 2) + 120) / 2);
        //int width = DisplayMetricsUtil.getScreenWidth(context);
        //cameraManager.setManualFramingRect((width / 2) + (width / 2)/2, (width / 2) + (width / 2)/2);
        viewfinderView.refreshDrawableState();

    }

    /**
     * 点击响应二维码扫描
     */
    private OnClickListener qrcodeImageListener = new OnClickListener() {
        public void onClick(View paramAnonymousView) {

            onecode.setBackgroundResource(R.drawable.scan_store);
            qrcode.setBackgroundResource(R.drawable.scan_qr_hl);
            qrcode.setSelected(true);
            onecode.setSelected(false);
            //statusView.setText(R.string.scan_qrcode);
            viewfinderView.setVisibility(View.VISIBLE);
            currentState = "qrcode";
            qrcodeSetting();

        }
    };

    private void qrcodeSetting() {
        decodeFormats = new Vector<BarcodeFormat>(2);
        decodeFormats.clear();
        decodeFormats.add(BarcodeFormat.QR_CODE);
        decodeFormats.add(BarcodeFormat.DATA_MATRIX);
        // TODO: 16/1/29 set select qrcode mode title
        if (null != mHandler) {
            mHandler.setDecodeFormats(decodeFormats);
        }
        int width = DisplayMetricsUtil.getScreenWidth(context);
        viewfinderView.refreshDrawableState();
        //cameraManager.setManualFramingRect(300, 300);
        //cameraManager.setManualFramingRect((width / 2) + (width / 2)/2, (width / 2) + (width / 2)/2);
        //cameraManager.setManualFramingRect((width / 2) + 30, (width / 2) + 30);//old
        cameraManager.setManualFramingRect((width / 2) + 120, (width / 2) + 120);
        viewfinderView.refreshDrawableState();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getCaptrueHandler() {
        return mHandler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    /**
     * 在经过一段延迟后重置相机以进行下一次扫描。 成功扫描过后可调用此方法立刻准备进行下次扫描
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (mHandler != null) {
            mHandler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /*
        switch (keyCode) {
		case KeyEvent.KEYCODE_BACK: // 拦截返回键

			restartPreviewAfterDelay(0L);
			return true;
		}*/
        return super.onKeyDown(keyCode, event);
    }


    public static class ResultScan {
        String result;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }


    }

}

package com.caro.smartmodule.helpers.AppUpdateHelper;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.RemoteViews;


import com.caro.smartmodule.R;
import com.caro.smartmodule.base.BaseApplication;
import com.caro.smartmodule.helpers.FileHelper;
import com.caro.smartmodule.utils.AppInfoUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by caro on 16/3/21.
 */
public class ApkUpdateService extends Service {
    private static final int TIMEOUT = 10 * 1000;// 超时

    private static final int DOWN_OK = 1;
    private static final int DOWN_ERROR = 0;

    private String app_name;
    private String url_up;
    private Context context;

    private NotificationManager notificationManager;
    private Notification notification;

    private Intent updateIntent;
    private PendingIntent pendingIntent;

    private final int notification_id = 0;
    File updateDir = null;
    File updateFile = null;
    String downloadDir = "/sdcard/Down";// 安装目录
    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (intent != null) {
            Bundle bundle = intent.getExtras();
            url_up = intent.getStringExtra("dwnUrl");
            BaseApplication acontext= (BaseApplication)getApplicationContext();
            context = acontext.getContext();
            app_name = AppInfoUtil.getAppName(context);// 创建文件
            createInstallFile(app_name);

            createNotification();

            createThread();
        }

        return super.onStartCommand(intent, flags, startId);

    }

    /***
     * 开线程下载
     */
    public void createThread() {
        /***
         * 更新UI
         */
        final Handler handler = new Handler() {
            @SuppressWarnings("deprecation")
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case DOWN_OK:
                        // 下载完成，点击安装
                        Uri uri = Uri.fromFile(updateFile);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(uri, "application/vnd.android.package-archive");
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        notificationManager.cancel(notification_id);
                        stopService(updateIntent);
                        break;
                    case DOWN_ERROR:

                        Notification.Builder noti = new Notification.Builder(ApkUpdateService.this)
                                .setContentTitle(app_name)
                                .setContentText("下载失败")
                                .setContentInfo("请检查网络后重试")
                                .setSmallIcon(R.drawable.icon_default_download);
                                //.setLargeIcon(aBitmap);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            notificationManager.notify(notification_id, noti.build());
                        }
                        break;

                    default:
                        stopService(updateIntent);
                        break;
                }

            }

        };

        final Message message = new Message();

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    long downloadSize = downloadUpdateFile(url_up, updateFile.toString());
                    if (downloadSize > 0) {
                        // 下载成功
                        message.what = DOWN_OK;
                        handler.sendMessage(message);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    message.what = DOWN_ERROR;
                    handler.sendMessage(message);
                }

            }
        }).start();
    }

    /***
     * 创建通知栏
     */
    RemoteViews contentView;

    public void createNotification() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification();

        /***
         * 在这里我们用自定的view来显示Notification
         */
        contentView = new RemoteViews(getPackageName(), R.layout.apkup_ntf_prg_dwn_item);
        contentView.setTextViewText(R.id.notificationTitle, "正在下载"+AppInfoUtil.getAppName(context)+"V-"+AppInfoUtil.getVerName(context));
        contentView.setTextViewText(R.id.notificationPercent, "0%");
        contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);
        notification.icon = R.drawable.icon_default_download;
        notification.contentView = contentView;

        updateIntent = new Intent(this,context.getClass());
        updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);

        notification.contentIntent = pendingIntent;

        notificationManager.notify(notification_id, notification);

    }

    /***
     * 下载文件
     *
     * @return
     * @throws java.net.MalformedURLException
     */
    public long downloadUpdateFile(String down_url, String file)
            throws Exception {
        int down_step = 5;// 提示step
        int totalSize;// 文件总大小
        int downloadCount = 0;// 已经下载好的大小
        int updateCount = 0;// 已经上传的文件大小
        InputStream inputStream;
        OutputStream outputStream;

        URL url = new URL(down_url);
        Log.e(getClass().toString() + "::url", " 共: " + url + "123" + down_url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url
                .openConnection();
        httpURLConnection.setConnectTimeout(TIMEOUT);
        httpURLConnection.setReadTimeout(TIMEOUT);
        // 获取下载文件的size
        totalSize = httpURLConnection.getContentLength();
        if (httpURLConnection.getResponseCode() == 404) {
            throw new Exception("fail!");
        }
        inputStream = httpURLConnection.getInputStream();
        Log.e(getClass().toString() + "::inputStream", " 共: " + inputStream);
        // outputStream=openFileOutput(file, Context.MODE_PRIVATE);
        outputStream = new FileOutputStream(file, false);// 文件存在则覆盖掉
        byte buffer[] = new byte[1024];
        int readsize = 0;

        while ((readsize = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, readsize);
            downloadCount += readsize;// 时时获取下载到的大小
            /**
             * 每次增张5%
             */
            if (updateCount == 0 || (downloadCount * 100 / totalSize - down_step) >= updateCount) {
                updateCount += down_step;
                // 改变通知栏
                // notification.setLatestEventInfo(this, "正在下载...", updateCount
                // + "%" + "", pendingIntent);
                contentView.setTextViewText(R.id.notificationPercent,updateCount + "%");
                contentView.setProgressBar(R.id.notificationProgress, 100, updateCount, false);
                // show_view
                notificationManager.notify(notification_id, notification);

            }

        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        inputStream.close();
        outputStream.close();

        return downloadCount;

    }

    /***
     * 创建文件
     */

    public  void createInstallFile(String name){

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
        {
            updateDir = new File(Environment.getExternalStorageDirectory() + "/" + downloadDir);
            updateFile = new File(updateDir + "/" + name + ".apk");

            if (!updateDir.exists())
            {
                updateDir.mkdirs();
            }
            if (!updateFile.exists())
            {
                try
                {
                    updateFile.createNewFile();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }


}

package com.caro.smartmodule.http;

import android.Manifest;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.util.HashMap;

import rx.functions.Action1;

/**
 * Created by caro on 2017/3/29.
 * 参考：
 * http://blog.csdn.net/xietansheng/article/details/52513624
 * http://www.cnblogs.com/zhaoyanjun/p/4591960.html
 * http://www.jianshu.com/p/7ad92b3d9069
 */
public class DownloadService extends Service {
    private static final String TAG = DownloadService.class.getSimpleName();
    private boolean isRunning = false;
    private static DownloadManager mDownloadManager;
    private ConnectivityManager mConnectMgr;
    private HashMap<Integer, String> mStatusMap = new HashMap<Integer, String>();
    private static long mTaskId = 0;
    private String fileName;
    //private static String dirType = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator;//dirType==/storage/emulated/0/Download/
    private static String appDowunPath;
    private static Context context;
    private boolean blnPermission = false;
    private static DownloadManager.Request request;

    /**
     * 开启服务
     *
     * @param context
     * @param downloadUrl
     * @param fileName
     */
    public static void startDownloadService(Context context, String downloadUrl, String fileName) {
        Intent intent = new Intent(context, DownloadService.class);
        Bundle bundle = new Bundle();
        bundle.putString("downloadUrl", downloadUrl);
        bundle.putString("fileName", fileName);
        intent.putExtras(bundle);
        context.startService(intent);
    }


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "Service onCreate");
        isRunning = true;


        //检查存储权限
        //批量获取权限测试-->定位，录音，摄像头，存储读写,获取device_id
        RxPermissions.getInstance(this).request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            blnPermission = true;
                        } else {
                            blnPermission = false;
                        }
                    }
                });
        if (!blnPermission) {
            return;
        }

        mConnectMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        mDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        mStatusMap.put(DownloadManager.STATUS_PENDING, "挂起");
        mStatusMap.put(DownloadManager.STATUS_RUNNING, "运行中");
        mStatusMap.put(DownloadManager.STATUS_PAUSED, "暂停");
        mStatusMap.put(DownloadManager.STATUS_SUCCESSFUL, "成功");
        mStatusMap.put(DownloadManager.STATUS_FAILED, "失败");
        context = this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!blnPermission) {
            return super.onStartCommand(intent, flags, startId);
        }
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            String downloadUrl = bundle.getString("downloadUrl");
            fileName = bundle.getString("fileName");
            appDowunPath =  getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
            //Log.i("dirType","onStartCommand appDowunPath == "+appDowunPath +  " fileName == "+fileName);
            File downloadFile = new File(appDowunPath+"/"+fileName);
            if (downloadFile.exists()){
                installAPK(downloadFile);
                return super.onStartCommand(intent, flags, startId);
           }

            //创建下载任务,downloadUrl就是下载链接
            request = new DownloadManager.Request(Uri.parse(downloadUrl));
            //设置下载网络现在方式
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
            request.setAllowedOverRoaming(true);//漫游网络是否可以下载
            /*
             * 设置允许使用的网络类型, 可选值:
             * NETWORK_MOBILE:      移动网络
             * NETWORK_WIFI:        WIFI网络
             * NETWORK_BLUETOOTH:   蓝牙网络
             * 默认为所有网络都允许
             */
            // request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
            //设置带通知栏下载
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setVisibleInDownloadsUi(true);
            //更多路径下载设置参考：http://www.cnblogs.com/zhaoyanjun/p/4591960.html
            //设置路径有多种方式 指定下载路径和下载文件名(设置下载文件的保存位置)
            // 1 TYPE
            //request.setDestinationInExternalFilesDir(this, null, mApkDir);//自己制定下载路径
            // 2 TYPE
            //File saveFile = new File(Environment.getExternalStorageDirectory(),fileName+".apk");
            // 3 TYPE
            //request.setDestinationUri(Uri.fromFile(saveFile));
            // 4 TYPE
            //request.setDestinationInExternalPublicDir(dirType, fileName);
            // 5 TYpe 设置到app下载目录
            String  appDownloadPath = Environment.DIRECTORY_DOWNLOADS;
            request.setDestinationInExternalFilesDir( this , appDownloadPath , fileName );//目录: Android -> data -> com.app -> files -> Download

            /*在默认的情况下，通过Download Manager下载的文件是不能被Media Scanner扫描到的 。
            进而这些下载的文件（音乐、视频等）就不会在Gallery 和  Music Player这样的应用中看到。
            为了让下载的音乐文件可以被其他应用扫描到，我们需要调用Request对象的
            */
            request.allowScanningByMediaScanner();

            //设置请求的Mime http://www.jianshu.com/p/7ad92b3d9069
            MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
            request.setMimeType(mimeTypeMap.getMimeTypeFromExtension(downloadUrl));

            //将下载任务加入下载队列，否则不会进行下载
            mTaskId = mDownloadManager.enqueue(request);

           /*
           参考来自：http://www.cnblogs.com/zhaoyanjun/p/4591960.html
           下载管理器中有很多下载项，怎么知道一个资源已经下载过，避免重复下载呢？
           我的项目中的需求就是apk更新下载，用户点击更新确定按钮，第一次是直接下载，
           后面如果用户连续点击更新确定按钮，就不要重复下载了。
           可以看出来查询和操作数据库查询一样的
           */
           /*验证没查询到，待解决
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(mTaskId);
            Cursor cursor = mDownloadManager.query(query);
            if (!cursor.moveToFirst()) {// 没有记录

            } else {

            }
            */

            //注册广播接收者，监听下载状态,通知栏点击状态
            registerReceiver(new DownloadManagerReceiver(),
                    new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        }


        //return super.onStartCommand(intent, flags, startId);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
        Log.i(TAG, "Service onDestroy");
    }


    //接收下载完成后的intent ,这里不管下载失败暂停或是延迟，都是完成
    public static class DownloadManagerReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                //Log.d(TAG, " download complete! id : " + downId + ", mDownloadId=" + mTaskId);
                //" 编号"+downId+"的下载任务已完成"
                //检查下载状态
                checkDownloadStatus();

            } else if (intent.getAction().equals(DownloadManager.ACTION_NOTIFICATION_CLICKED)) {
                long[] downIds = intent.getLongArrayExtra(DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS);
                for (long downId : downIds) {
                    //Log.d(TAG, " notify click! id : " + downId + ", mDownloadId=" + mTaskId);
                    if (downId == mTaskId) {
                        //下载进度条被点击了一下
                    }
                }
            }
        }
    }


    //检查下载状态
    private static void checkDownloadStatus() {
        // 创建一个查询对象
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        // 还可以根据状态过滤结果
        // query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
        Cursor cursor = mDownloadManager.query(query);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        // 下载ID
        long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
        // 下载请求的状态
        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        // 下载文件在本地保存的路径（Android 7.0 以后 COLUMN_LOCAL_FILENAME 字段被弃用, 需要用 COLUMN_LOCAL_URI 字段来获取本地文件路径的 Uri）
        String localFilename = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
        // 已下载的字节大小
        long downloadedSoFar = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
        // 下载文件的总字节大小
        long totalSize = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
        cursor.close();
        System.out.println("下载进度: " + downloadedSoFar + "/" + totalSize);

        switch (status) {
            case DownloadManager.STATUS_PAUSED:
                //MLog.i(">>>下载暂停");
            case DownloadManager.STATUS_PENDING:
                //MLog.i(">>>下载延迟");
            case DownloadManager.STATUS_RUNNING:
                //MLog.i(">>>正在下载");
                break;
            case DownloadManager.STATUS_SUCCESSFUL:
                /*
                 * 特别注意: 查询获取到的 localFilename 才是下载文件真正的保存路径，在创建
                 * 请求时设置的保存路径不一定是最终的保存路径，因为当设置的路径已是存在的文件时，
                 * 下载器会自动重命名保存路径，例如: .../demo-1.apk, .../demo-2.apk
                 *  具体路径如会保存到 /storage/emulated/0/storage/emulated/0/Download/就在这1.0-1.7
                 */
                //下载的保存路径在这了
                //下载成功, 打开文件, 文件路径: /storage/emulated/0/Android/data/com.wallta.smartlife/files/Download/就在这1.0.7
                //Log.i("dirType","下载成功, 打开文件, 文件路径: "+localFilename);
                //String downloadPath =dirType+localFilename;
                installAPK(new File(localFilename));
                break;
            case DownloadManager.STATUS_FAILED:
                //MLog.i(">>>下载失败");
                break;
        }

    }


    //下载到本地后执行安装
    protected static void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //清除通知栏下载
        mDownloadManager.remove(mTaskId);

    }
}

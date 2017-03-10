package com.notifyWrapper;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.caro.smartmodule.R;


/**
 * Created by caro on 16/7/25.
 */
public class NotificationManagerWrapper {

    private Context context;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    private static NotificationManagerWrapper instance;

    /**
     * single
     * @param context
     * @return
     */
    public static  NotificationManagerWrapper getInstance(Context context){

        if (instance == null){
            instance = new NotificationManagerWrapper(context);
        }

        return instance;

    }

    public NotificationManagerWrapper(Context context) {

        this.context = context;
        builder = new NotificationCompat.Builder(context);
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }


    /**
     *
     * Android 5.0（API 21）引入浮动通知的展现形式，想让通知能够以浮动形式展现，
     * 需要设置 Notification 的优先级为 PRIORITY_HIGH 或者 PRIORITY_MAX 并且使用铃声或振动
     * @param notifyID
     */

    public void notifyBasicMessage(int notifyID,int smarllIconResId) {

        // 设置通知的基本信息：icon、标题、内容
        builder.setSmallIcon(smarllIconResId);
        builder.setContentTitle("My notification");
        builder.setContentText("Hello World!");
        // 设置通知的优先级--浮动通知的展现形式
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // 设置通知的提示音
        builder.setSound(alarmSound);
        builder.setAutoCancel(false);
        builder.setWhen(System.currentTimeMillis());
        // 设置通知的点击行为：这里启动一个 Activity,可用于查看消息
        // Intent intent = new Intent(this, ResultActivity.class);
        // PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // builder.setContentIntent(pendingIntent);
        // 发送通知 id 需要在应用内唯一

        notificationManager.notify(notifyID, builder.build());
    }

    public void cancelNotify(int notifyID){
        notificationManager.cancel(notifyID);
    }

    public void cancelAllNotify(){
        notificationManager.cancelAll();
    }
}

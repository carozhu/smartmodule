package com.caro.smartmodule.common.networkstatemanager;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

/**
 * 网络状态切换监听
 * bug:为什么第一次打开会回调到wifi或是3/4G已经连接的状态.(并没做切换)
 */
public class NetworkStateService extends Service {
    private final String TAG = NetworkStateService.class.getSimpleName();
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                Log.i(TAG, "CONNECTIVITY_ACTION");
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null) { // connected to the internet
                    if (activeNetwork.isConnected()) {
                        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                            // connected to wifi
                            Log.e(TAG, "当前WiFi连接可用 ");
                        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                            // connected to the mobile provider's data plan
                            Log.e(TAG, "当前移动网络连接可用 ");
                        }
                    } else {
                        Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                    }
                } else {   // not connected to the internet
                    Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub  
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }


}  

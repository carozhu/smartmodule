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

public class NetworkStateService extends Service{
    private final String  TAG = "NetworkStateService";
    private static final String tag="NetworkStateServicetag";  
    private ConnectivityManager connectivityManager;  
    private NetworkInfo info;  

    private Intent NetworkChangedIntent= new Intent("networkChanged");

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {  
  
        @Override  
        public void onReceive(Context context, Intent intent) {  
            String action = intent.getAction();  
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo.State wifiState = null;
                NetworkInfo.State mobileState = null;
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                wifiState = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
                mobileState = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();

                if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState && NetworkInfo.State.CONNECTED == mobileState) {
                    // 手机网络连接成功 mobbile
                    Log.i(TAG, "手机网络连接成功");
                    NetworkChangedIntent.putExtra("netAvailable", true);
                    NetworkChangedIntent.putExtra("netName", "MOBBILE");//mobbile
                    sendBroadcast(NetworkChangedIntent);
                } else if (wifiState != null && mobileState != null && NetworkInfo.State.CONNECTED != wifiState
                        && NetworkInfo.State.CONNECTED != mobileState) {
                    // 手机没有任何的网络 无线 3g 4g
                    Log.i(TAG, "手机没有任何的网络");
                    NetworkChangedIntent.putExtra("netAvailable", false);
                    NetworkChangedIntent.putExtra("netName", "Nothing");//mobbile
                    sendBroadcast(NetworkChangedIntent);
                } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {
                    // 无线网络连接成功
                    Log.i(TAG, "无线网络连接成功");
                    NetworkChangedIntent.putExtra("netAvailable", true);
                    NetworkChangedIntent.putExtra("netName", "WIFI");//wifi
                    sendBroadcast(NetworkChangedIntent);
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
      
    public void startService(){
    	this.startService(NetworkChangedIntent);

    }
}  

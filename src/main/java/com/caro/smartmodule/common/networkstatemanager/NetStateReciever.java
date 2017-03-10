package com.caro.smartmodule.common.networkstatemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 
 * @author caro
 *usage:
 *在需要消息的页面动态注册，并接收handler消息
 *		// add action IntentFilter
		NetStateReciever netStateReciever = new NetStateReciever(handler);
		intentFilter = new IntentFilter();
		intentFilter.addAction(Consts.NETWORKCHANGED);
		mContext.registerReceiver(netStateReciever, intentFilter);
	
	Tips:
	记得在程序退出前或当前注册页面onPasue onDetsroy 里解除netStateReciever 注册
 */
public class NetStateReciever extends BroadcastReceiver{
	private final String  TAG = "NetStateReciever";
	private NetStateReciever netStateReciever;
      private Handler handler;
      private String intentFilter;
      private int msgType;
	public NetStateReciever(){
	
	}
	//such:intentFilter = Consts.NETWORKCHANGED;  msgType = Consts.NETSTATECHANGED
	public NetStateReciever(Handler mhandler,int msgType,String intentFilter){
		this.handler = mhandler;
		this.msgType = msgType;
		this.intentFilter=intentFilter;
	}
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.i(TAG, "ACTION IS :" + intent.getAction());
		String action = intent.getAction();
		if (action.equalsIgnoreCase(intentFilter)){
			boolean netAvailable =(boolean) intent.getBooleanExtra("netAvailable", false);
			String netName = intent.getStringExtra("netName");
			Bundle netBundle = new Bundle();
			netBundle.putBoolean("netAvailable", netAvailable);
			netBundle.putString("netName", netName);
			Message msg = new Message();
			msg.what=msgType;
			msg.setData(netBundle);
			handler.sendMessage(msg);
		}
	}
	
	/*动态注册*/
	public  void  registNetStateReciever(Context context,Handler handler){
		if(intentFilter != null || msgType !=  0){
			netStateReciever = new NetStateReciever(handler,msgType,intentFilter);
			IntentFilter mintentFilter = new IntentFilter();
			mintentFilter.addAction(intentFilter);
			context.registerReceiver(netStateReciever,mintentFilter);
		}
	
	}
	
	public  void  unregistNetStateReciever(Context context){
		if(netStateReciever != null){
			context.unregisterReceiver(netStateReciever);
		}
	}

}

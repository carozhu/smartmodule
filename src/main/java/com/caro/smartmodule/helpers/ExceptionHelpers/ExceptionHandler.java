package com.caro.smartmodule.helpers.ExceptionHelpers;

import android.content.Context;
import android.os.Looper;

import java.lang.Thread.UncaughtExceptionHandler;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 * 
 * @author caro
 * 注意配置sendEmailBUGForDevloper方法中的邮箱地址和参数
 * 在Application中的onCreate()后加上以下代码即可.
 *Thread.setDefaultUncaughtExceptionHandler(ExceptionHandler.getInstance(getApplicationContext(),emailAds,emialPassword));
 */
public class ExceptionHandler implements UncaughtExceptionHandler {

	private static ExceptionHandler mHandler;
	private static Context mContext;
	private String emailADS;
	private String emailPasd;
	private String formApp;
	private ExceptionHandler(String emailAds,String emialPassword,String mformApp){
		this.emailADS = emailAds;
		this.emailPasd = emialPassword;
		this.formApp=mformApp;
	}
	public synchronized static ExceptionHandler getInstance(Context context,String emailAds,String emialPassword,String mformApp){
		if(mHandler==null){
			mHandler = new ExceptionHandler(emailAds,emialPassword,mformApp);
			mContext = context;
		}
		return mHandler;
	}
	@Override
	public void uncaughtException(Thread thread, final Throwable ex) {
		// TODO Auto-generated method stub
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					JavaEmail.sendEmailBUGForDevloper(formApp,ex,emailADS,emailPasd);
				} catch (AddressException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MessagingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		}).start();
		
		new Thread(){public void run() {
			Looper.prepare();
			Looper.loop();
			
		};}.start();
		new Thread(){
			public void run() {
				try {
					Thread.sleep(2000);//多少秒后退出程序
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				android.os.Process.killProcess(android.os.Process.myPid());
			};
		}.start();
		
	}
	

}


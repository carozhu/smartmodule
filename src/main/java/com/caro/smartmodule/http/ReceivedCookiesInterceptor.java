package com.caro.smartmodule.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class ReceivedCookiesInterceptor implements Interceptor {
    private final ThreadLocal<Context> context = new ThreadLocal<>();
    private Context mcontext;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context.set(context);
        this.mcontext =context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Response originalResponse = chain.proceed(chain.request());
        //这里获取请求返回的cookie
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            //这里用了RxJava的相关API大家可以忽略,用自己逻辑实现即可.大家可以用别的方法保存cookie数据
            Observable.from(originalResponse.headers("Set-Cookie"))
                    .map(new Func1<String, String>() {
                        @Override
                        public String call(String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Action1<String>() {
                        @Override
                        public void call(String cookie) {
                            cookieBuffer.append(cookie).append(";");
                        }
                    });
            SharedPreferences sharedPreferences = mcontext.getSharedPreferences("cookie", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String[] sessionArry = cookieBuffer.toString().split("=");
            editor.putString("cookie", sessionArry[1]);
            editor.commit();
            Log.d("ReceivedCookiesIntec","get cookie ------->:"+sessionArry[1]);
        }else {
            Log.d("ReceivedCookiesIntec","headers Set-Cookie.isEmpty()");
        }

        return originalResponse;
    }
}
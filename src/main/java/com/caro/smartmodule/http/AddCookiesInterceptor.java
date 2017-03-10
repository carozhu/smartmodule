package com.caro.smartmodule.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action1;

public class AddCookiesInterceptor implements Interceptor {
    private Context context;

    public AddCookiesInterceptor(Context context) {
        super();
        this.context = context;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
                //RxJava的API
                Observable.just(sharedPreferences.getString("cookie", ""))
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String cookie) {
                        //添加cookie
                        builder.addHeader("Cookie", cookie);
//                        builder.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
//                        builder.addHeader("Accept-Encoding", "gzip, deflate");
//                        builder.addHeader("Connection", "keep-alive");
//                        builder.addHeader("Accept", "*/*");
//                        builder.addHeader("Cookie", "add cookies here");

                        Log.d("AddCookiesInterceptor","addHeader ------->:"+cookie);
                    }
                });
        return chain.proceed(builder.build());
    }
}
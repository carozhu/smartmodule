package com.caro.smartmodule.http;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;


import com.caro.smartmodule.utils.FileUtil;
import com.caro.smartmodule.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttpClient
 *
 */
public class OkHttpUtils {
    private final static String TAG = OkHttpUtils.class.getSimpleName();
    private static OkHttpClient.Builder singleton;
    public static final String RESPONSE_CACHE= "okhttpCache";
	public static final long RESPONSE_CACHE_SIZE = 100*1024*1024;//RESPONSE_CACHE_SIZE 100M.hehe
	public static final long HTTP_CONNECT_TIMEOUT = 60;
	public static final long HTTP_READ_TIMEOUT = 60;
      public static OkHttpClient.Builder getInstance(final Context context) {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    File cacheDir = new File(context.getCacheDir(), RESPONSE_CACHE);
                    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                    singleton = new OkHttpClient().newBuilder();

                    singleton.cache(new Cache(cacheDir, RESPONSE_CACHE_SIZE));
                    singleton.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    singleton.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);

                    //singleton.interceptors().add(new ReceivedCookiesInterceptor(context));
                    //singleton.interceptors().add(new AddCookiesInterceptor(context));

                    singleton.retryOnConnectionFailure(true);
                    singleton.addInterceptor(interceptor);


                    //设置缓存路径
                    File httpCacheDirectory = new File(context.getCacheDir(), "httpCacheResponses");
                    //设置缓存 100M
                    Cache cache = new Cache(httpCacheDirectory, 100 * 1024 * 1024);
                    Interceptor myinterceptor = new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Log.i(TAG,"request="+request);
                            if (!NetworkUtil.isNetworkAvailable(context)) {
                                request = request.newBuilder()
                                        .cacheControl(CacheControl.FORCE_CACHE)
                                        .build();
                                Log.i(TAG,"no network");

                            }

                            Response response = chain.proceed(request);
                            if (NetworkUtil.isNetworkAvailable(context)) {
                                int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
                                Log.i(TAG,"has network maxAge="+maxAge);
                                response.newBuilder()
                                        .header("Cache-Control", "public, max-age=" + maxAge)
                                        .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                        .build();
                            } else {
                                Log.i(TAG,"network error");
                                int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                                Log.i(TAG,"has maxStale="+maxStale);
                                response.newBuilder()
                                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                        .removeHeader("Pragma")
                                        .build();
                                Log.i(TAG,"response build maxStale="+maxStale);
                            }
                            return response;
                        }
                    };
                    singleton.addInterceptor(myinterceptor).cache(cache);




                }
            }
        }
        return singleton;
    }


}

package com.caro.smartmodule.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.caro.smartmodule.ConfigureManager;
import com.caro.smartmodule.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * OkHttpClient
 * carozhu
 * 参考：Retrofit 2.0 + OkHttp 3.0 配置
 * https://drakeet.me/retrofit-2-0-okhttp-3-0-config
 */
public class OkHttpUtils {
    private final static String TAG = OkHttpUtils.class.getSimpleName();
    private static OkHttpClient.Builder singleton;
    public static final String RESPONSE_CACHE = "okhttpCache";
    public static final long RESPONSE_CACHE_SIZE = 100 * 1024 * 1024;//RESPONSE_CACHE_SIZE 100M.hehe
    public static final long HTTP_CONNECT_TIMEOUT = 10;//连接超时时间
    public static final long HTTP_READ_TIMEOUT = 10;//

    public static OkHttpClient.Builder getInstance(final Context context) {
        if (singleton == null) {
            synchronized (OkHttpUtils.class) {
                if (singleton == null) {
                    singleton = new OkHttpClient().newBuilder();
                    singleton.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.SECONDS);
                    singleton.readTimeout(HTTP_READ_TIMEOUT, TimeUnit.SECONDS);
                    singleton.retryOnConnectionFailure(true);//okhttp设置错误重新连接

                    //singleton.addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR);
                    //singleton.addInterceptor(REWRITE_RESPONSE_INTERCEPTOR_OFFLINE);

                    File cacheDir = new File(context.getCacheDir(), RESPONSE_CACHE);
                    singleton.cache(new Cache(cacheDir, RESPONSE_CACHE_SIZE));
                    //or use
                    //Cache cache = new Cache(context.getCacheDir(), RESPONSE_CACHE_SIZE);
                    //singleton.cache(cache);

                    //singleton.interceptors().add(new ReceivedCookiesInterceptor(context));
                    //singleton.interceptors().add(new AddCookiesInterceptor(context));
                    //singleton.addNetworkInterceptor(mTokenInterceptor)
                    //addNetworkInterceptor 让所有网络请求都附上你的拦截器，
                    //如这里设置了一个 token 拦截器，就是在所有网络请求的 header 加上 token 参数，下面会稍微讲一下这个内容

                    if ( ConfigureManager.getConfigureManager().isOkhttpLogger()){
                        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        singleton.addInterceptor(interceptor);
                    }

                    if (ConfigureManager.getConfigureManager().isOkhttpCache()) {
                        //设置缓存路径
                        File httpCacheDirectory = new File(context.getCacheDir(), "httpCacheResponses");
                        //设置缓存 100M
                        Cache cache = new Cache(httpCacheDirectory, RESPONSE_CACHE_SIZE);
                        Interceptor myinterceptor = new Interceptor() {
                            @Override
                            public Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();
                                Log.i(TAG, "request=" + request);
                                if (!NetworkUtil.isNetworkAvailable(context)) {
                                    request = request.newBuilder()
                                            .cacheControl(CacheControl.FORCE_CACHE)
                                            .build();
                                    Log.i(TAG, "no network");

                                }

                                Response response = chain.proceed(request);
                                if (NetworkUtil.isNetworkAvailable(context)) {
                                    int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
                                    Log.i(TAG, "has network maxAge=" + maxAge);
                                    response.newBuilder()
                                            .header("Cache-Control", "public, max-age=" + maxAge)
                                            .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                            .build();
                                } else {
                                    Log.i(TAG, "network error");
                                    int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
                                    Log.i(TAG, "has maxStale=" + maxStale);
                                    response.newBuilder()
                                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                                            .removeHeader("Pragma")
                                            .build();
                                    Log.i(TAG, "response build maxStale=" + maxStale);
                                }
                                return response;
                            }
                        };
                        singleton.addInterceptor(myinterceptor).cache(cache);


                    }
                }
            }
        }
        return singleton;
    }


    /**
     * 拦截异常。直接抛出offline模式
     */
//    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) {
//            Response originalResponse = null;
//            try {
//                originalResponse = chain.proceed(chain.request());
//                String cacheControl = originalResponse.header("Cache-Control");
//                if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
//                        cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
//                    if (NetworkUtil.isNetworkAvailable(mcontext)) {
//                        int maxAge = 0 * 60; // 有网络时 设置缓存超时时间0个小时
//                        Log.i(TAG, "有网络---> maxAge=" + maxAge);
//                        return originalResponse.newBuilder()
//                                .header("Cache-Control", "public, max-age=" + maxAge)
//                                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
//                                .build();
//                    } else {
//                        try {
//                            int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
//                            Log.i(TAG, "network error | has maxStale=" + maxStale);
//                            originalResponse.newBuilder()
//                                    //.header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
//                                    .header("Cache-Control", "public, max-stale=" + maxStale)
//                                    .removeHeader("Pragma")
//                                    .build();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            Log.i(TAG, "无网络--->缓存Error");
//                        }
//                    }
//                } else {
//                    return originalResponse;
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                Request request = chain.request();
//                request = request.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached")
//                        .build();
//                try {
//                    return chain.proceed(request);
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//            }
//
//            return null;
//        }
//    };

//    private static final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
//        @Override
//        public okhttp3.Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//            if (!NetworkUtil.isNetworkAvailable(mcontext)) {
//                request = request.newBuilder()
//                        .header("Cache-Control", "public, only-if-cached")
//                        .build();
//            }
//            return chain.proceed(request);
//        }
//    };


//    public class LoggingInterceptor implements Interceptor {
//        public Response intercept(Chain chain) throws IOException {
//            Request request = chain.request();
//
//            long t1 = System.nanoTime();
//            System.out.println(String.format("发送请求: [%s] %s%n%s",
//                    request.url(), chain.connection(), request.headers()));
//
//            Response response = chain.proceed(request);
//
//            long t2 = System.nanoTime();
//            System.out.println(String.format("接收响应: [%s] %.1fms%n%s",
//                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//
//            return response;
//        }
//    }


}

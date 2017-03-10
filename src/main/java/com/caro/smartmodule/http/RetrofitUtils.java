package com.caro.smartmodule.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;

import com.caro.smartmodule.utils.NetworkUtil;

import java.io.File;
import java.io.IOException;
import java.net.NetworkInterface;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * 其他问题可参:
 * 给okhttpclient 统一设置header
 * http://stackoverflow.com/questions/32605711/adding-header-to-all-request-with-retrofit-2
 * <p/>
 * 参考:https://gank.io/post/56e80c2c677659311bed9841 RxJava 与 Retrofit 结合的最佳实践
 * <p/>
 * <p/>
 * 缓存策略参考:http://www.jianshu.com/p/3e13e5d34531    (Retrofit2.0使用总结)
 */
public class RetrofitUtils {
    private final static String TAG = RetrofitUtils.class.getSimpleName();
    private static String BASE_HOST;
    private static RetrofitUtils mRetrofitUtils;
    private static Retrofit singleton;

    /**
     * init base_host value for all
     *
     * @param BASE_HOST
     */
    public RetrofitUtils(String BASE_HOST) {
        this.BASE_HOST = BASE_HOST;
    }

    /**
     * you maybe init in application
     *
     * @param BASE_HOST
     * @return
     */
    public static RetrofitUtils initRetrofit(String BASE_HOST) {

        if (mRetrofitUtils == null) {
            mRetrofitUtils = new RetrofitUtils(BASE_HOST);

        }

        return mRetrofitUtils;
    }

    /**
     * default time 60s
     *
     * @param context
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createApi(final Context context, Class<T> clazz) {

        if (TextUtils.isEmpty(BASE_HOST)) {
            throw new RuntimeException("RetrofitUtils not instantiated yet,please init your BASE_HOST address");
        }
        if (singleton == null) {
            synchronized (RetrofitUtils.class) {
                if (singleton == null) {
                    singleton = new Retrofit.Builder()
                            .baseUrl(BASE_HOST)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                            .client(OkHttpUtils.getInstance(context).build())
                            .build();


                }
            }

        }
        return singleton.create(clazz);
    }


    public static <T> T createPrivApi(final Context context, Class<T> clazz, String urlhead) {
        synchronized (RetrofitUtils.class) {
            Retrofit PrivApiingSinleton = new Retrofit.Builder()
                    .baseUrl(urlhead)
                    .addConverterFactory(GsonConverterFactory.create())//JacksonConverterFactory
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(OkHttpUtils.getInstance(context).build())
                    .build();
            return PrivApiingSinleton.create(clazz);
        }


    }


    /**
     * createApkUpdateApi
     *
     * @param context
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T createDebugPrivApi(final Context context, Class<T> clazz, String urlhead) {
        Retrofit msingleton;
        synchronized (RetrofitUtils.class) {

            //设置缓存路径
            /*
            File httpCacheDirectory = new File(context.getCacheDir(), "httpCacheResponses");
            //设置缓存 100M
            Cache cache = new Cache(httpCacheDirectory, 100 * 1024 * 1024);
            Interceptor myinterceptor = new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
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
                        int maxAge =  60*60; // read from cache for 60 minute
                        Log.i(TAG,"has network maxAge="+maxAge);
                        response.newBuilder()
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                                .build();
                    } else {
                        Log.i(TAG,"network error");
                        int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周 tolerate 4-weeks stale
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

            final Interceptor REWRITE_RESPONSE_INTERCEPTOR_OFFLINE = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    if (!NetworkUtil.isNetworkAvailable(context)) {
                        request = request.newBuilder()
                                .header("Cache-Control", "public, only-if-cached")
                                .build();
                    }
                    return chain.proceed(request);
                }
            };
            final Interceptor REWRITE_RESPONSE_INTERCEPTOR = new Interceptor() {
                @Override
                public okhttp3.Response intercept(Chain chain) throws IOException {
                    okhttp3.Response originalResponse = chain.proceed(chain.request());
                    String cacheControl = originalResponse.header("Cache-Control");
                    if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                            cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
                        return originalResponse.newBuilder()
                                .header("Cache-Control", "public, max-age=" + 5000)
                                .build();
                    } else {
                        return originalResponse;
                    }
                }
            };
            */

            //上下2种方法测试均出现retrofit2.adapter.rxjava.HttpException: HTTP 504 Unsatisfiable Request (only-if-cached)
            /**************************************************************************************/
            //以下参考:https://github.com/maning0303/RetrofitOKHttpCache 可正确缓存github的get缓存请求
//            final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
//
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    //方案一：有网和没有网都是先读缓存
//
//                    Request request = chain.request();
//                    Log.i(TAG, "request=" + request);
//                    Response response = chain.proceed(request);
//                    Log.i(TAG, "response=" + response);
//
//                    String cacheControl = request.cacheControl().toString();
//                    if (TextUtils.isEmpty(cacheControl)) {
//                        cacheControl = "public, max-age=60*60";
//                    }
//                    return response.newBuilder()
//                            .header("Cache-Control", cacheControl)
//                            .removeHeader("Pragma")
//                            .build();
//
//
//                    //方案二：无网读缓存，有网根据过期时间重新请求
////                    boolean netWorkConection = NetworkUtil.isNetworkAvailable(context);
////                    Request request = chain.request();
////                    if (!netWorkConection) {
////                        request = request.newBuilder()
////                                .cacheControl(CacheControl.FORCE_CACHE)
////                                .build();
////                    }
////
////                    Response response = chain.proceed(request);
////                    if (netWorkConection) {
////                        //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
////                        String cacheControl = request.cacheControl().toString();
////                        Log.i(TAG, "cacheControl:" + cacheControl);
////                        response.newBuilder()
////                                .removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
////                                .header("Cache-Control", cacheControl)
////                                .build();
////                    } else {
////                        int maxStale = 60 * 60 * 24 * 7;
////                        response.newBuilder()
////                                .removeHeader("Pragma")
////                                .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
////                                .build();
////                    }
////                    return response;
//                }
//            };
//
//            final Interceptor LoggingInterceptor = new Interceptor() {
//                @Override
//                public Response intercept(Interceptor.Chain chain) throws IOException {
//                    Request request = chain.request();
//                    long t1 = System.nanoTime();
//                    Log.i(TAG, String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
//                    Response response = chain.proceed(request);
//                    long t2 = System.nanoTime();
//                    Log.i(TAG, String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
//                    return response;
//                }
//            };
//
//            OkHttpClient.Builder client = new OkHttpClient.Builder();
//            client.writeTimeout(30 * 1000, TimeUnit.MILLISECONDS);
//            client.readTimeout(20 * 1000, TimeUnit.MILLISECONDS);
//            client.connectTimeout(15 * 1000, TimeUnit.MILLISECONDS);
//            //设置缓存路径
//            File httpCacheDirectory = new File(context.getCacheDir(), "okhttpCache");
//            //设置缓存 10M
//            Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
//            client.cache(cache);
//            //设置拦截器
//            client.addInterceptor(LoggingInterceptor);
//            client.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
//            client.addInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);


            msingleton = new Retrofit.Builder()
                    .baseUrl(urlhead)
                    .addConverterFactory(GsonConverterFactory.create())//JacksonConverterFactory
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(OkHttpUtils.getInstance(context).build())
                    //.client(client.build())
                    .build();
        }

        return msingleton.create(clazz);
    }


}
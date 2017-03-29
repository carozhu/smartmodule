package com.caro.smartmodule.http;

import android.content.Context;
import android.text.TextUtils;

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
     * @return RetrofitUtils instance
     */
    public static RetrofitUtils getRetrofitUtilsInstance() {

        if (mRetrofitUtils == null) {
            throw new RuntimeException("RetrofitUtils have not initRetrofit");
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


}
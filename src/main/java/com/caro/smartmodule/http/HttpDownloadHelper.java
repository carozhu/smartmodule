package com.caro.smartmodule.http;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by caro on 2017/3/29.
 * retrofit rxjava 文件下载，返回进度。
 * retrofit参考github上一些开源项目
 * https://drakeet.me/retrofit-2-0-okhttp-3-0-config
 * https://github.com/TimAimee/RetrofitDownFileWithProgress
 * https://github.com/Learn2Crack/android-retrofit-file-download
 * 在此感谢
 * <p>
 * TODO:目前尚未解决异常中断如网络切换带来的下载问题
 * <p>
 * 用法参考：
 * 参考末尾注释
 */
public class HttpDownloadHelper {
    private Context context;
    private static HttpDownloadHelper instance;
    OkHttpClient.Builder httpClient;
    Retrofit.Builder builder;

    public static HttpDownloadHelper getInstance(Context context) {
        if (instance == null) {
            instance = new HttpDownloadHelper(context);
        }
        return instance;
    }

    public HttpDownloadHelper(Context context) {
        this.context = context;
        httpClient = OkHttpUtils.getInstance(context);
        builder = new Retrofit.Builder()
                //.baseUrl(BASE_HOST) retrofit 2.x 版本可以直接写全url地址
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(httpClient.build());
    }

    public void down(String url, ProgressListener progressListener, Subscriber<Response<ResponseBody>> responseBodySubscriber) {
        creatService(HttpDownloadService.class, progressListener)
                .downloadFileUseStream(url)
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .subscribe(responseBodySubscriber);


    }

    public <S> S creatService(Class<S> serviceClass, final ProgressListener progressListener) {
        //给httpClient添加拦截器
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response originalResponse = chain.proceed(chain.request());
                ProgressResponseBody responseBody = new ProgressResponseBody(originalResponse.body(), progressListener);
                return originalResponse.newBuilder()
                        .body(responseBody)
                        .build();
            }
        });
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}

//    private void downFile() {
//
//        final ProgressListener progressListener = new ProgressListener() {
//            @Override
//            public void update(final long bytesRead, final long contentLength, boolean done) {
//                Logger.t(TAG).d(" %d%% done\n", (100 * bytesRead) / contentLength);
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        textView.setText((100 * bytesRead) / contentLength + "%" + "(" + bytesRead + "/" + contentLength + ")");
//                    }
//                });
//
//                //更新通知。写一个轻量级的下载更新。通知状态栏
//            }
//        };
//
//        final File file = new File(filepath);
//        HttpDownUtil.getInstance().down(URL, progressListener, new Subscriber<Response<ResponseBody>>() {
//
//
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//                for (int i = 0; i < e.getStackTrace().length; i++) {
//
//                    Logger.t(TAG).e("down onNext err=" + e.getStackTrace()[i].toString());
//                }
//            }
//
//            @Override
//            public void onNext(Response<ResponseBody> responseBody) {
//                Logger.t(TAG).d("down onNext");
//                if (responseBody.isSuccess()) {
//
//
//                    boolean writeSuccess = writeResponseBodyToDisk(responseBody.body(), file);
//                    if (writeSuccess) {
//                        Logger.t(TAG).d("writesucess");
//                    }
//                }
//            }
//        });
//
//    }
//
//
//    private boolean writeResponseBodyToDisk(ResponseBody body, File file) {
//        try {
//            // todo change the file location/name according to your needs
//
//            InputStream inputStream = null;
//            OutputStream outputStream = null;
//
//            try {
//                byte[] fileReader = new byte[4096];
//
//                long fileSize = body.contentLength();
//                long fileSizeDownloaded = 0;
//
//                inputStream = body.byteStream();
//                outputStream = new FileOutputStream(file);
//
//                while (true) {
//                    int read = inputStream.read(fileReader);
//
//                    if (read == -1) {
//                        break;
//                    }
//
//                    outputStream.write(fileReader, 0, read);
//
//                    fileSizeDownloaded += read;
//
//                    Logger.t(TAG).d("file download: " + fileSizeDownloaded + " of " + fileSize);
//                }
//
//                outputStream.flush();
//
//                return true;
//            } catch (IOException e) {
//                return false;
//            } finally {
//                if (inputStream != null) {
//                    inputStream.close();
//                }
//
//                if (outputStream != null) {
//                    outputStream.close();
//                }
//            }
//        } catch (IOException e) {
//            return false;
//        }
//    }

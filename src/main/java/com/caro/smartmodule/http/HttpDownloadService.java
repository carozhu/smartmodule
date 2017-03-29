package com.caro.smartmodule.http;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by caro on 2017/3/29.
 */

public interface HttpDownloadService {
    @Streaming
    @GET
    Observable<Response<ResponseBody>> downloadFileUseStream(@Url String url);

}

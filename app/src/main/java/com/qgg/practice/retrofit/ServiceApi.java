package com.qgg.practice.retrofit;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/7
 * @describe :
 */

public interface ServiceApi  {

    @GET("v2/movie/top250")
    Observable<Response<ResponseBody>> getTop250Rx(@Query("start") int start, @Query("count") int count);

    @GET("v2/movie/top250")
    Call<DoubanTop250Movie> getTop250(@Query("start") int start, @Query("count") int count);
}

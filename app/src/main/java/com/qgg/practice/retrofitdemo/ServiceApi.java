package com.qgg.practice.retrofitdemo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/7
 * @describe :
 */

public interface ServiceApi  {

//    @GET("v2/movie/top250")
//    Observable<Response<ResponseBody>> getTop250Rx(@Query("start") int start, @Query("count") int count);

    @GET("v2/movie/top250")
    Call<DoubanTop250Movie> getTop250(@Query("start") int start, @Query("count") int count);
}

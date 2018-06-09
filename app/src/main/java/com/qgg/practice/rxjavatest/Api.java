package com.qgg.practice.rxjavatest;

import com.qgg.practice.rxjavatest.entity.LoginRequest;
import com.qgg.practice.rxjavatest.entity.LoginResponse;
import com.qgg.practice.rxjavatest.entity.RegisterRequest;
import com.qgg.practice.rxjavatest.entity.RegisterResponse;
import com.qgg.practice.rxjavatest.entity.UserBaseInfoRequest;
import com.qgg.practice.rxjavatest.entity.UserBaseInfoResponse;
import com.qgg.practice.rxjavatest.entity.UserExtraInfoRequest;
import com.qgg.practice.rxjavatest.entity.UserExtraInfoResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {

    @GET("idcard/index")
    Observable<LoginResponse> login(@Body LoginRequest request);

    @GET
    Observable<RegisterResponse> register(@Body RegisterRequest request);

    @GET
    Observable<UserBaseInfoResponse> getUserBaseInfo(@Body UserBaseInfoRequest request);

    @GET
    Observable<UserExtraInfoResponse> getUserExtraInfo(@Body UserExtraInfoRequest request);

    @GET("v2/movie/top250")
    Observable<Response<ResponseBody>> getTop250(@Query("start") int start, @Query("count") int count);
}

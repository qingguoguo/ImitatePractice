package com.qgg.practice.retrofitdemo;

import com.google.gson.Gson;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

class ServiceMethod {

    private Method mMethod;
    private Retrofit mRetrofit;
    private Annotation[][] mParameterAnnotations;
    private Annotation[] mAnnotations;
    private ParameterHandler[] parameterHandlers;
    private String httpMethod;
    private String relativeUrl;

    public ServiceMethod(Builder builder) {
        this.mMethod = builder.mMethod;
        this.mRetrofit = builder.mRetrofit;
        mAnnotations = builder.mAnnotations;
        mParameterAnnotations = builder.mParameterAnnotations;
        parameterHandlers = builder.parameterHandlers;
        this.relativeUrl = builder.relativeUrl;
        this.httpMethod = builder.httpMethod;
    }

    public okhttp3.Call createNewCall(Object[] args) {
        // 拼接参数
        RequestBuilder requestBuilder = new RequestBuilder(mRetrofit.getUrl(), relativeUrl, parameterHandlers, args);
        return mRetrofit.getOkHttpClient().newCall(requestBuilder.build());
    }

    public <T> T parseBody(ResponseBody responseBody) {
        // 获取方法返回值得类型
        Type type = mMethod.getGenericReturnType();
        Class <T> dataClass = (Class <T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        // 解析工厂去转换
        Gson gson = new Gson();
        T body = gson.fromJson(responseBody.charStream(),dataClass);
        return body;
    }

    public static class Builder {
        Method mMethod;
        Retrofit mRetrofit;
        Annotation[][] mParameterAnnotations;
        Annotation[] mAnnotations;
        ParameterHandler[] parameterHandlers;
        String httpMethod;
        String relativeUrl;

        public Builder(Method method, Retrofit retrofit) {
            this.mMethod = method;
            this.mRetrofit = retrofit;
            mAnnotations = mMethod.getAnnotations();
            mParameterAnnotations = mMethod.getParameterAnnotations();
            parameterHandlers = new ParameterHandler[mParameterAnnotations.length];
        }

        public ServiceMethod build() {
            // 解析网络请求类型
            for (Annotation annotation : mAnnotations) {
                parseAnnotationMethod(annotation);
            }

            // 解析参数注解
            for (int i = 0; i < mParameterAnnotations.length; i++) {
                Annotation parameter = mParameterAnnotations[i][0];
                // Query 等 模板和策略 设计模式
                if (parameter instanceof Query) {
                    // 封装成 ParameterHandler ，不同的参数注解选择不同的策略
                    parameterHandlers[i] = new ParameterHandler.Query(((Query) parameter).value());
                }
            }
            return new ServiceMethod(this);
        }

        private void parseMethodAndPath(String method, String value) {
            this.httpMethod = method;
            this.relativeUrl = value;
        }

        private void parseAnnotationMethod(Annotation annotation) {
            if (annotation instanceof GET) {
                parseMethodAndPath("GET", ((GET) annotation).value());
            } else if (annotation instanceof POST) {
                parseMethodAndPath("POST", ((POST) annotation).value());
            }
        }
    }
}

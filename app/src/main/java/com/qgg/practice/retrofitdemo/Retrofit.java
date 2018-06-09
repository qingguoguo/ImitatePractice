package com.qgg.practice.retrofitdemo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.OkHttpClient;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

public class Retrofit {

    private String mBaseUrl;
    private okhttp3.Call.Factory mCallFactory;
    private Map<Method, ServiceMethod> serviceMethodMapCache = new ConcurrentHashMap<>();

    public Retrofit(Builder builder) {
        this.mBaseUrl = builder.url;
        this.mCallFactory = builder.mCallFactory;
    }

    public String getUrl() {
        return mBaseUrl;
    }

    public okhttp3.Call.Factory getOkHttpClient() {
        return mCallFactory;
    }

    public <T> T create(Class<T> tClass) {
        Utils.validateServiceInterface(tClass);

        return (T) Proxy.newProxyInstance(tClass.getClassLoader(), new Class[]{tClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //Log.e("TAG", method.getName());
                // 判断是不是 Object 的方法
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(this, args);
                }

                // 解析参数注解
                ServiceMethod serviceMethod = loadServiceMethod(method);
                // 封装 OkHttpCall
                OkHttpCall<T> okHttpCall = new OkHttpCall(serviceMethod,args);

                return okHttpCall;
            }
        });
    }

    private ServiceMethod loadServiceMethod(Method method) {
        // 缓存 ServiceMethod
        ServiceMethod serviceMethod = serviceMethodMapCache.get(method);
        if (serviceMethod == null) {
            serviceMethod = new ServiceMethod.Builder(method, this).build();
            serviceMethodMapCache.put(method, serviceMethod);

        }
        return serviceMethod;
    }

    public static class Builder {
        private String url;
        private okhttp3.Call.Factory mCallFactory;

        public Builder baseUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder client(okhttp3.Call.Factory factory) {
            this.mCallFactory = factory;
            return this;
        }

        public Retrofit build() {
            if (mCallFactory==null){
                mCallFactory = new OkHttpClient();
            }
            return new Retrofit(this);
        }
    }
}

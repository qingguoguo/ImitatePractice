package com.qgg.practice.retrofitdemo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

public interface ParameterHandler<T> {

    void apply(RequestBuilder requestBuilder, T value);


    class Query<T> implements ParameterHandler<T> {

        private String key;

        public Query(String key) {
            this.key = key;
        }

        @Override
        public void apply(RequestBuilder requestBuilder, T value) {
            requestBuilder.addQueryName(key, value.toString());
        }
    }
}

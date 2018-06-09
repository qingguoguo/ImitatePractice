package com.qgg.practice.retrofitdemo;

/**
 * @author :qingguoguo
 * @datetime ：2018/6/8
 * @describe :
 */

public class Utils {

    static <T> void validateServiceInterface(Class<T> service) {
        if (!service.isInterface()) {
            throw new IllegalArgumentException("API declarations must be interfaces.");
        }
        if (service.getInterfaces().length > 0) {
            throw new IllegalArgumentException("API interfaces must not extend other interfaces.");
        }
    }
}

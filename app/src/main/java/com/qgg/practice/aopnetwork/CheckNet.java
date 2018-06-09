package com.qgg.practice.aopnetwork;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/16
 * @describe :
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CheckNet {
//    int value();
//
//    String tableName() default "className";
}

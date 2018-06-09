package com.qgg.practice.aopnetwork;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/16
 * @describe :处理切点
 */

@Aspect
public class SectionAspect {
    /**
     * 找到处理的切点
     * * *(..)  可处理所有的方法
     */
    @Pointcut("execution(@com.hencoder.hencoderpracticedraw7.aopnetwork.CheckNet * *(..))")
    public void checkNetBehavior() {
    }

    /**
     * 处理切面
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("checkNetBehavior()")
    public Object check(ProceedingJoinPoint joinPoint) throws Throwable {
        Log.e("TAG", "CheckNet");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        CheckNet checkNet = signature.getMethod().getAnnotation(CheckNet.class);
        if (checkNet != null) {
            Object aThis = joinPoint.getThis();
            Context context = getContext(aThis);
            if (context != null) {
                if (!isNetworkAvailable(context)) {
                    Toast.makeText(context, "请检查网络设置", Toast.LENGTH_SHORT).show();
                    return null;
                }
            }
        }
        return joinPoint.proceed();
    }

    private Context getContext(Object object) {
        if (object instanceof Activity) {
            return (Activity) object;
        } else if (object instanceof Fragment) {
            return ((Fragment) object).getActivity();
        } else if (object instanceof View) {
            return ((View) object).getContext();
        }
        return null;
    }

    /**
     * 检查当前网络是否可用
     *
     * @return
     */
    private static boolean isNetworkAvailable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (int i = 0; i < networkInfo.length; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

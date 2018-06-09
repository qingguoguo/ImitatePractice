package com.qgg.practice.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author :qingguoguo
 * @datetime ：2018/5/25
 * @describe :
 */

public class EventBus {

    static volatile EventBus defaultInstance;

    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    private EventBus() {
        subscriptionsByEventType = new HashMap<>();
        typesBySubscriber = new HashMap<>();
    }

    public static EventBus getDefault() {
        if (defaultInstance == null) {
            synchronized (EventBus.class) {
                if (defaultInstance == null) {
                    defaultInstance = new EventBus();
                }
            }
        }
        return defaultInstance;
    }

    public void register(Object subscriber) {
        Class<?> aClass = subscriber.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();
        List<SubscriberMethod> SubscriberMethods = new ArrayList<>();
        for (Method declaredMethod : declaredMethods) {
            Subscribe annotation = declaredMethod.getAnnotation(Subscribe.class);
            if (annotation != null) {
                Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                if (parameterTypes.length == 1) {
                    SubscriberMethod subscriberMethod = new SubscriberMethod(declaredMethod, parameterTypes[0],
                            annotation.threadMode(), annotation.priority(), annotation.sticky());
                    SubscriberMethods.add(subscriberMethod);
                }
            }
        }
        for (SubscriberMethod subscriberMethod : SubscriberMethods) {
            subscribe(subscriber, subscriberMethod);
        }
    }

    private void subscribe(Object subscriber, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptionList = subscriptionsByEventType.get(eventType);
        if (subscriptionList == null) {
            subscriptionList = new CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType, subscriptionList);
        }
        subscriptionList.add(new Subscription(subscriber, subscriberMethod));

        List<Class<?>> classes = typesBySubscriber.get(subscriber);
        if (classes == null) {
            classes = new ArrayList<>();
            typesBySubscriber.put(subscriber, classes);
        }
        if (!classes.contains(subscriber)) {
            classes.add(eventType);
        }
    }

    public void unregister(Object subscriber) {
        List<Class<?>> classes = typesBySubscriber.get(subscriber);
        if (classes != null) {
            for (Class<?> aClass : classes) {
                unsubscribeByEventType(subscriber, aClass);
            }
            typesBySubscriber.remove(classes);
        }
    }

    private void unsubscribeByEventType(Object subscriber, Class<?> classes) {
        List<Subscription> subscriptions = subscriptionsByEventType.get(classes);
        if (subscriptions != null) {
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if (subscription.subscriber == subscriber) {
                    subscription.active = false;
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    public void post(Object event) {
        Class<?> eventType = event.getClass();
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                executeMethod(subscription, event);
            }
        }
    }

    private void executeMethod(final Subscription subscription, final Object event) {
        ThreadMode threadMode = subscription.subscriberMethod.threadMode;
        boolean isMainThread = Looper.getMainLooper() == Looper.myLooper();
        switch (threadMode) {
            case POSTING:
                invokeMethod(subscription, event);
                break;
            case MAIN:
                if (isMainThread) {
                    invokeMethod(subscription, event);
                } else {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(subscription, event);
                        }
                    });
                }
                break;
            case BACKGROUND:
                if (!isMainThread) {
                    invokeMethod(subscription, event);
                } else {

                }
                break;
            case ASYNC:
                AsyncPoster.enqueue(subscription,event);
                break;
            default:
                throw new IllegalArgumentException("参数不正确");
        }
    }

    private void invokeMethod(Subscription subscription, Object event) {
        SubscriberMethod subscriberMethod = subscription.subscriberMethod;
        try {
            subscriberMethod.method.invoke(subscription.subscriber, event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}

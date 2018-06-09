/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.qgg.practice.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class AsyncPoster implements Runnable {

    private static ExecutorService execute = Executors.newCachedThreadPool();
    private Subscription subscription;
    private Object event;

    public AsyncPoster(Subscription subscription, Object event) {
        this.subscription = subscription;
        this.event = event;
    }

    static public void enqueue(Subscription subscription, Object event) {
        execute.execute(new AsyncPoster(subscription, event));
    }

    @Override
    public void run() {
        invokeMethod(subscription, event);
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

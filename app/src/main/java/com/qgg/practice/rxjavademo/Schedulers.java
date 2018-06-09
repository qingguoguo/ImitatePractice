/**
 * Copyright (c) 2016-present, RxJava Contributors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package com.qgg.practice.rxjavademo;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;


public abstract class Schedulers {

    private static final Schedulers IO;
    private static final Schedulers MAIN_THREAD;

    static {
        MAIN_THREAD = new MainThreadSchedulers();
        IO = new IOSchedulers();
    }

    public static Schedulers io() {
        return IO;
    }

    public static Schedulers mainThread() {
        return MAIN_THREAD;
    }

    public abstract void scheduleDirect(Runnable runnable);

    private static class IOSchedulers extends Schedulers {
        private ExecutorService mExecutorService;

        public IOSchedulers() {
            mExecutorService = Executors.newScheduledThreadPool(
                    1, new ThreadFactory() {
                        @Override
                        public Thread newThread(@NonNull Runnable r) {
                            return new Thread(r);
                        }
                    }
            );
        }

        @Override
        public void scheduleDirect(Runnable runnable) {
            mExecutorService.execute(runnable);
        }
    }

    private static class MainThreadSchedulers extends Schedulers {

        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void scheduleDirect(Runnable runnable) {
            Message message = Message.obtain(mHandler, runnable);
            mHandler.sendMessage(message);
        }
    }
}

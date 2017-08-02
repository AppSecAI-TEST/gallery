package com.lam.gallery.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lenovo on 2017/7/28.
 */

public class ThreadTask {
    private static LinkedBlockingDeque<Runnable> mLinkedBlockingDeque;
    private static ExecutorService mExecutorService;

    public static void addTask(Runnable runnable) {
        if(mLinkedBlockingDeque == null || mExecutorService == null) {
            synchronized (LinkedBlockingDeque.class) {
                if(mLinkedBlockingDeque == null || mExecutorService == null) {
                    mLinkedBlockingDeque = new LinkedBlockingDeque();
                    mExecutorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.SECONDS, mLinkedBlockingDeque);
                    ((ThreadPoolExecutor) mExecutorService).prestartCoreThread();
                }
            }
        }
        mLinkedBlockingDeque.addFirst(runnable);
    }

    public static void clear() {
        mLinkedBlockingDeque.clear();
    }

    public static void shutDown() {
        if(mExecutorService != null) {
            mExecutorService.shutdown();
            mLinkedBlockingDeque = null;
            mExecutorService = null;
        }
    }
}
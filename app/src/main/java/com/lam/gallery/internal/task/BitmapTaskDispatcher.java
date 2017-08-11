package com.lam.gallery.internal.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.lam.gallery.internal.entity.ConfigSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by lenovo on 2017/8/4.
 */

public class BitmapTaskDispatcher {
    private static final String TAG = "BitmapTaskDispatcher";
    private static BitmapTaskDispatcher sLIFOTaskDispatcher;
    private static BitmapTaskDispatcher sFIFOTaskDispatcher;
    //任务调度队列
    private static List<TaskRunnable> sTaskRunnableList;
    private static ExecutorService mThreadPool;
    //轮询线程
    private static HandlerThread mPollThread;
    //轮询线程中的Handler
    private static Handler mPollHandler;
    //信号量
    private static volatile Semaphore mPollSemaphore;
    private int mType;
    private HashMap<Object, TaskRunnable> mCurrentMap;

    private static final int LIFO = 1;
    private static final int FIFO = 2;
    private static final int DEFAULT_PERMITS_SIZE = ConfigSpec.getInstance().mSemaphoreSubmitSize;
    private static final int DISPATCHER = 0X852;

    private BitmapTaskDispatcher(int permitSize, int type) {
        init(permitSize, type);
    }

    public static BitmapTaskDispatcher getLIFOTaskDispatcher() {
        if(sLIFOTaskDispatcher == null) {
            synchronized (BitmapTaskDispatcher.class) {
                if(sLIFOTaskDispatcher == null) {
                    sLIFOTaskDispatcher = new BitmapTaskDispatcher(DEFAULT_PERMITS_SIZE, LIFO);
                }
            }
        }
        return sLIFOTaskDispatcher;
    }

    public static BitmapTaskDispatcher getFIFOTaskDispatcher() {
        if(sFIFOTaskDispatcher == null) {
            synchronized (BitmapTaskDispatcher.class) {
                if(sFIFOTaskDispatcher == null) {
                    sFIFOTaskDispatcher = new BitmapTaskDispatcher(DEFAULT_PERMITS_SIZE, FIFO);
                }
            }
        }
        return sFIFOTaskDispatcher;
    }


    /**
     * 初始化线程池、信号量、任务调度队列
     * @param permitSize  信号量大小
     * @param type  任务调度队列的进出模式
     */
    private void init(int permitSize, int type) {
        mThreadPool = Executors.newCachedThreadPool();
        ((ThreadPoolExecutor) mThreadPool).prestartCoreThread();
        mPollSemaphore = new Semaphore(permitSize);
        sTaskRunnableList = new ArrayList<>();
        mType = type;
        mPollThread = new HandlerThread("Poll Thread");
        mPollThread.start();
        mPollHandler = new Handler(mPollThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                try {
                    mPollSemaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TaskRunnable runnable = getTask();
                if(runnable != null) {
                    mThreadPool.execute(runnable);
                } else {
                    mPollSemaphore.release();
                }
            }
        };
    }

    /**
     * 按照设定的队列调度方式获得需要执行的runnable
     * @return
     */
    private synchronized TaskRunnable getTask() {
        if(mType == LIFO && sTaskRunnableList.size() != 0) {
            TaskRunnable taskRunnable = sTaskRunnableList.get(sTaskRunnableList.size() - 1);
            sTaskRunnableList.remove(sTaskRunnableList.size() - 1);
            return taskRunnable;
        } else if(mType == FIFO && sTaskRunnableList.size() != 0) {
            TaskRunnable taskRunnable = sTaskRunnableList.get(0);
            sTaskRunnableList.remove(0);
            return taskRunnable;
        }
        return null;
    }

    /**
     * 添加一个runnable到任务调度队列
     * @param runnable
     */
    public synchronized void addTask(TaskRunnable runnable) {
        sTaskRunnableList.add(runnable);
        mPollHandler.sendEmptyMessage(DISPATCHER);
    }


    public static void clear() {
        if(sTaskRunnableList != null) {
            sTaskRunnableList.clear();
        }
    }

    public static void shutDown() {
        if(mThreadPool != null) {
            mThreadPool.shutdown();
            sLIFOTaskDispatcher = null;
        }
        if(sTaskRunnableList != null) {
            sTaskRunnableList.clear();
        }
    }

    public abstract static class TaskRunnable<T> implements Runnable {

        T mT;

        @Override
        public void run() {
            try {
                mT = doTask();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPollSemaphore.release();
            }
        }

        public abstract T doTask();
    }

}

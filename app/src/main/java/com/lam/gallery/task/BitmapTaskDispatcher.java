package com.lam.gallery.task;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import java.util.LinkedList;
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
    private static LinkedList<TaskRunnable> mRunnableLinkedList;
    private static ExecutorService mThreadPool;
    //轮询线程
    private static HandlerThread mPollThread;
    //轮询线程中的Handler
    private static Handler mPollHandler;
    //信号量
    private static volatile Semaphore mPollSemaphore;
    private int mType;

    public static final int LIFO = 1;
    public static final int FIFO = 2;
    public static final int DEFAULT_PERMITS_SIZE = 5;
    public static final int DISPATCHER = 0X852;

    public BitmapTaskDispatcher(int permitSize, int type) {
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
        mRunnableLinkedList = new LinkedList<>();
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
        if(mType == LIFO && mRunnableLinkedList.size() != 0) {
            return mRunnableLinkedList.removeLast();
        } else if(mType == FIFO && mRunnableLinkedList.size() != 0) {
            return mRunnableLinkedList.removeFirst();
        }
        return null;
    }

    /**
     * 添加一个runnable到任务调度队列
     * @param runnable
     */
    public static synchronized void addTask(TaskRunnable runnable) {
        mRunnableLinkedList.add(runnable);
        mPollHandler.sendEmptyMessage(DISPATCHER);
    }


    public static void clear() {
        if(mRunnableLinkedList != null) {
            mRunnableLinkedList.clear();
        }
    }

    public static void shutDown() {
        if(mThreadPool != null) {
            mThreadPool.shutdown();
        }
    }

    public abstract static class TaskRunnable implements Runnable {

        @Override
        public void run() {
            try {
                doTask();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mPollSemaphore.release();
            }
        }

        public abstract void doTask();
    }
}

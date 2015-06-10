package com.thesaan.gameengine.android.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.thesaan.gameengine.android.Settings;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by mknoe on 17.04.2015.
 */
public class TaskManager extends ThreadPoolExecutor {

    //TODO {@link https://developer.android.com/training/multiple-threads/run-code.html#StopThread}
    private BlockingQueue<Runnable> decodeQueue;

    private ThreadPoolExecutor executor;

    private Handler surfaceHandler;
    /**
     * All possible Runnables
     */
    private Runnable
            // draws the actual surface
            surfaceDrawingTask;
    /**
     *
     * @param corePoolSize
     * @param maximumPoolSize
     * @param keepAliveTime
     * @param unit
     * @param workQueue
     */
    private TaskManager(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);

        /*
        *   This object recieves all incoming Runnables with
        *   the "first-in first-out" method.
        * */
        decodeQueue = new LinkedBlockingQueue<Runnable>();

        surfaceHandler = new Handler(Looper.getMainLooper()){

            @Override
            public void handleMessage(Message inputMessage){

            }
        };
        /**
         * The actual thread manager
         */
        executor = new TaskManager(
                Settings.NUMBER_OF_CORES,
                Settings.NUMBER_OF_CORES,
                Settings.KEEP_ALIVE_TIME,
                Settings.KEEP_ALIVE_TIME_UNIT,
                decodeQueue
                );
    }

    /**
     * Handles the runnable which is added to the working queue
     */
    public void handleRunnableState(TaskManager manager, int state){
        //TODO Alle Runnable ids einbinden

        switch (state){
            case Settings.RUNNABLE_DRAW_CANVAS:{
                manager.getSurfaceDrawRunnable();
            }
        }
    }

    /**
     *
     * @param runnable
     * Has to be the runnable task where the surface will be drawn onto.
     */
    public void setSurfaceDrawRunnable(Runnable runnable){
        surfaceDrawingTask = runnable;
    }

    private Runnable getSurfaceDrawRunnable(){
        return surfaceDrawingTask;
    }
}

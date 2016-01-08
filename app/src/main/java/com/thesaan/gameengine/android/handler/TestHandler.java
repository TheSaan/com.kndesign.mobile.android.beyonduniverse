package com.thesaan.gameengine.android.handler;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.SystemClock;

import javax.net.ssl.ManagerFactoryParameters;

/**
 * Provides some testing methods to check the performance of your code
 */
public class TestHandler{

    long start;

    //shows the index of the test
    private int testcase;

    public TestHandler(){
        testcase = 0;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void startTimer(){
        start = 0;
        testcase++;
        start = SystemClock.elapsedRealtimeNanos();
    }

    /**
     * Just returns the elapsed time
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public double stopTimer(){
        long end = SystemClock.elapsedRealtimeNanos();
        return (double)(end-start)/1000000;
    }

    /**
     * Creates a Test output with a testcase counter
     * @param testname
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void stopTimer(String testname){
        System.out.println("\n|--------[Test "+testcase+"]--------| \n|" +
                testname+":\n"
                +"|Time elapsed: "+stopTimer()+" ms\n");
    }
}

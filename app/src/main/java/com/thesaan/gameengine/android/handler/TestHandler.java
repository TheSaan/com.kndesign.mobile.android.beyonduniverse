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

    //shows the index of the box_test
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
    private double stopTime(){
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

        double stop = stopTime();
        String format;

        final int milliseconds_in_second = 1000;
        final int milliseconds_in_minute = 60000;

        if(stop > milliseconds_in_second){
            stop /= milliseconds_in_second;
            format = stop+" seconds";
        }else if(stop > milliseconds_in_minute){
            stop /= milliseconds_in_minute;
            double rest_seconds = stop % milliseconds_in_minute;
            double rest_milliseconds = rest_seconds % milliseconds_in_second;

            stop -= stop%milliseconds_in_minute;
            format =
                    stop+
                            " minutes "+
                            rest_seconds+
                            " seconds "+
                            rest_milliseconds+
                            " milliseconds";
        }else{
            format = stop+" milliseconds";
        }

        System.out.println("\n|--------[Test "+testcase+"]--------| \n|" +
                testname+":\n"
                +"|Time elapsed: "+format+"\n");
    }


    /**
     * Usable in loops to print out the progress
     * @param processname a description for the output
     * @param current the counter
     * @param end the loop end
     */
    public void printLoopProgress(String processname, int current, int end){

        int progress = 100*current/end;


        if(progress == 25 ||progress == 50 ||progress == 75 ||progress == 100 ) {

            System.out.println(progress + "% of [" + processname + "] finished.");
        }else if( current == end -1 ){
            System.out.println(100 + "% of [" + processname + "] finished.");
        }
    }
}

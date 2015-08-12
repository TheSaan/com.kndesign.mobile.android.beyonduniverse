package com.thesaan.gameengine.android;

import java.util.concurrent.TimeUnit;

/**
 * Created by mknoe on 17.04.2015.
 */
public interface Settings {

    /**
     * Get the number of available processor cores but only the available once.
     * If only 2 of 4 are active the variable is 2.
     */
     int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    // Sets the amount of time an idle thread waits before terminating
     int KEEP_ALIVE_TIME = 1;
    // Sets the Time Unit to seconds
     TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;

    /**
     * The possible Runnables identifier
     */
     int RUNNABLE_DRAW_CANVAS = 1000;

    /**
     * The action selectors to choose which runnable has to be run
     */

    /**
     * moves the player char
     */
     int RUN_PLAYER_ANIM = 500;

    /**
     * Selects the amount of objects where its data will be shown at its position
     */
    int numberOfObjectsToPrintData = 6;
}

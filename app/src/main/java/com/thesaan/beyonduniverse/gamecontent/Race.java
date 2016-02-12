package com.thesaan.beyonduniverse.gamecontent;

/**
 * Created by mknoe on 01.05.2015.
 */
public class Race {

    public static final int RACE_HUMAN = 0;
    public static final int RACE_HIGHS = 1;
    public static final int RACE_CROX = 2;

    public static final String HUMAN_NAME_TEXT = "Human";
    public static final String HIGHS_NAME_TEXT = "Highs";
    public static final String CROX_NAME_TEXT = "Crox";

    /**
     * A collection of all available Races.
     *
     * {@link #RACE_HUMAN}
     * {@link #RACE_CROX}
     * {@link #RACE_HIGHS}
     */
    public final static int[] RACES = {
            RACE_HUMAN,
            RACE_HIGHS,
            RACE_CROX
    };

    /**
     * Dollar
     */
    public static String HUMAN_CURRENCY = "Credits";
    /**
     * Opas
     */
    public static String HIGHS_CURRENCY = "Opas";
    /**
     * Lear
     */
    public static String CROX_CURRENCY = "Lear";

    //TODO Rasse definieren

    private int myRaceType;

    /**
     * This class defines all properties a race has to handle in game.
     * @param raceType
     *  Set the race type from {@link #RACES}
     */
    public Race(int raceType){
        myRaceType = raceType;
    }
/*
    public File getRaceDrawableDir(){
        switch (myRaceType){
            case RACE_HUMAN:{
                MainActivity.internalDir.getAbsolutePath()
            }
        }
    }*/

    public int getType(){
        return myRaceType;
    }
}

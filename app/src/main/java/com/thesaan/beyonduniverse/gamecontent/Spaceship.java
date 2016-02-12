package com.thesaan.beyonduniverse.gamecontent;

import com.thesaan.beyonduniverse.gamecontent.economy.Item;

/**
 * Created by Michael on 09.02.2016.
 */
public class Spaceship {

    public final static int FREGATTE = 1;
    public final static int CRUISER = 2;
    public final static int FREIGHTER = 3;

    private final int MOVE_MAX_VALUE = 1000;

    //movement attributes
    int acceleration;
    int max_velocity;
    int fraction;
    int weight;

    int current_velocity = 0;



    //Added Ship equipment
    Item engine;
    Item hull;
    Item shield;
    Item[] weapons;
    Item storage;
    Item radar;

    //TODO add more ship equipment

    /**
     *
     * @param name
     * @param shiptype
     * <p>An spaceship has some default values like acceleration,
     * velocity,fraction, weight.</p>
     *
     * <p>The ship type adds certain muliplier to it to get the fly behavior</p>
     * <p>In the extended instances, the multiplier will be set</p>
     */
    public Spaceship(String name, int shiptype){
        setShipFlyBehavior(shiptype);
    }

    private void setShipFlyBehavior(int type){
        switch (type){
            case FREGATTE:
                acceleration = 9; //seconds to get full speed
                max_velocity = 450; // m/s
                fraction = 0; //not in space, changes in athmospheres
                weight = 45; //tons
                break;
            case CRUISER:
                acceleration = 20; //seconds to get full speed
                max_velocity = 300; // m/s
                fraction = 0; //in space, changes in athmospheres
                weight = 2500000; //tons
                break;
            case FREIGHTER:
                acceleration = 14; //seconds to get full speed
                max_velocity = 300; // m/s
                fraction = 0; //in space, changes in athmospheres
                weight = 15000; //tons
                break;
            default:
                acceleration = 12; //seconds to get full speed
                max_velocity = 300; // m/s
                fraction = 0; //in space, changes in athmospheres
                weight = 55; //tons
                break;
        }
    }


}

package com.thesaan.beyonduniverse.gamecontent;

import com.thesaan.gameengine.android.jpct_extend.BUObject3D;

/**
 * All data which handles the player and its interaction.
 *
 * <p>The Player class is not extended from {@link BUObject3D} because
 * the object data will be set after creating an instance, which means
 * no super() method can be called inside the contructor to give an
 * {@link com.threed.jpct.Object3D} to it</p>
 */
public class Player{

    //the players money
    private int credits = 0;
    private BUObject3D myModel;



    //Skills
    private final int MAX_SKILL_VALUE = 10000;

    /**
     * testing constructor
     */
    public Player(){

    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}

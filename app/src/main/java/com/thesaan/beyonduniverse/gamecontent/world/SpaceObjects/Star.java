package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Star extends UniverseObject {

    private float mEnergy;
    public Star(String name, MathHandler.Vector position, float mass, float radius,float degrees, int type){
        super(name,position,mass,radius,degrees,type);

        mEnergy = degrees*ENERGY_MULTIPLIER;
    }

    public float getSolarPower(){
        return mEnergy;
    }
}

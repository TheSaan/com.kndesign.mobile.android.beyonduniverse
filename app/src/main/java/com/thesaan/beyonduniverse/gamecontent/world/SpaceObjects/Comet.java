package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Comet extends UniverseObject {


    public Comet(String name, MathHandler.Vector position, float mass, float degrees, int type){
        super(name,position,mass,degrees,type);

        this.type = type;

        setRandomRadius();
    }
}

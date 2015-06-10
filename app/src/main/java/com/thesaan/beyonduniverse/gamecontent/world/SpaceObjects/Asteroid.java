package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Asteroid extends UniverseObject {


    public Asteroid(String name, MathHandler.Vector position, float mass, float radius, float degrees, int type){
        super(name, position,mass,radius,degrees,type);
    }
}

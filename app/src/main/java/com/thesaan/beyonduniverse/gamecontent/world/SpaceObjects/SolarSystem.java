package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class SolarSystem extends UniverseObject {


    double volume;

    public SolarSystem(String name,float volume, MathHandler.Vector position,Star[] stars,Planet[] planets, float radius, int type){
        super(name,position,radius,type);
        this.volume = volume;
        this.stars = stars;
        this.planets = planets;
    }
}

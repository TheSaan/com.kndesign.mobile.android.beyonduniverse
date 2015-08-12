package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class SolarSystem extends UniverseObject {


    double volume;

    public SolarSystem(String name, float volume, Galaxy parent, float radius, int type){
        super(name,parent,radius,type);
        this.volume = volume;
        this.stars = stars;
        this.planets = planets;
    }

    public void setStars(Star[] stars){
        this.stars = stars;
    }

    public void setPlanets(Planet[] planets){
        this.planets = planets;
    }
}

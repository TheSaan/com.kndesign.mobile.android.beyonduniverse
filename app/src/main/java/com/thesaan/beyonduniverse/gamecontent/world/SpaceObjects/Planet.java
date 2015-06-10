package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Planet extends UniverseObject {

    public Planet(String name, MathHandler.Vector position, float mass, float radius,Moon[] moons,City[] cities, float degrees, int type, int planetType){
        super(name,position,mass,radius,degrees,type);


        this.planetType = planetType;
        if(cities != null) {
            this.cities = cities;

            this.markets = new Market[this.cities.length];

            for (int i = 0; i < cities.length; i++) {
                this.population += cities[i].getPopulation();
                Market m = cities[i].getMarket();
                this.markets[i] = m;
            }
        }
        if(moons != null) {
            this.moons = moons;
        }
    }

    public static String getPlanetTypeName(int myPlanetType){
        switch (myPlanetType){
            case PLANET_TYPE_GRASS: return "Grass planet";
            case PLANET_TYPE_WATER: return "Water planet";
            case PLANET_TYPE_DESSERT: return "Dessert planet";
            case PLANET_TYPE_MAGMA: return "Magma planet";
            case PLANET_TYPE_DSCHUNGLE: return "Dschungle planet";
            case PLANET_TYPE_STONE: return "Stone planet";
            case PLANET_TYPE_SNOW: return "Snow planet";
            case PLANET_TYPE_ICE: return "Ice planet";
            case PLANET_TYPE_LOST: return "Planet lost";
            default: return "no planet type detected";
        }
    }

    public int getPlanetType() {
        return planetType;
    }
}

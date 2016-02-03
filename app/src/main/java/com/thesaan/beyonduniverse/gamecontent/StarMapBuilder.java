package com.thesaan.beyonduniverse.gamecontent;

import android.content.Context;

import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Moon;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Planet;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.SolarSystem;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;

@Deprecated
public class StarMapBuilder {

    Galaxy[] galaxies;
    Context context;

    public StarMapBuilder(Context context) {
        this.context = context;
    }

//    /**
//     * Creates galaxy objects from database and makes it global with {@link #getGalaxies()}
//     */
//    public void createMapOfGalaxies(int amount) {
//
//        World w = new World(context);
//
//        //create Galaxies
//        w.createGalaxies(amount, new Map());
//
//        this.galaxies = w.galaxies;
//
//
//    }


//    /**
//     * Gets all {@link Star stars} and {@link Planet planets} of this {@link SolarSystem}. They have to be casted.
//     *
//     * @param parent
//     * @return Contains two arrays
//     * <p>index 0: stars</p>
//     * <p>index 1: planets</p>
//     */
//    public UniverseObject[][] getSolarSystemChildren(SolarSystem parent) {
//        Planet[] planets = parent.getPlanets();
//        Star[] stars = parent.getStars();
//        UniverseObject[][] objects = new UniverseObject[][]{
//                stars, planets
//        };
//        return objects;
//    }

    /**
     * Gets all {@link SolarSystem solar systems} of this {@link Galaxy}. They have to be casted.
     *
     * @param parent
     * @return
     */
    public SolarSystem[] getSolarSystems(Galaxy parent) {

        return null;
    }

    public void setGalaxies(Galaxy[] galaxies) {
        this.galaxies = galaxies;
    }

    public Galaxy[] getGalaxies() {
        return galaxies;
    }

    /**
     * Gets all {@link Moon moons} and {@link com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.City cities} of this {@link Planet parent}. They have to be casted.
     *
     * @param parent
     * @return Contains two arrays
     * <p>index 0: cities</p>
     * <p>index 1: moons</p>
     */
    public UniverseObject[][] getPlanetChildren(Planet parent) {
        return null;
    }
}

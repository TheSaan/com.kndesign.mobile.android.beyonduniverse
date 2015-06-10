package com.thesaan.beyonduniverse.gamecontent;

import android.database.Cursor;

import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Moon;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Planet;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.SolarSystem;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Star;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.database.UniverseDatabase;
import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 04.05.2015.
 */
public class StarMapBuilder {

    Galaxy[] galaxies;
    SolarSystem[] solarSystems;
    Star[] stars;
    Planet[] planets;
    Moon[] moons;

    UniverseDatabase db;

    public StarMapBuilder(UniverseDatabase db){
        this.db = db;
        createMapOfGalaxies();
    }

    /**
     * Creates galaxy objects from database and makes it global with {@link #getGalaxies()}
     */
    public void createMapOfGalaxies(){

        galaxies = new Galaxy[db.getNumberOfGalaxies()];
//        System.err.println("Number of galaxies "+db.getNumberOfGalaxies());
        Cursor c;
        for(int i = 0; i< db.getNumberOfGalaxies();i++){

            c = db.getGalaxy(i+1);
            c.moveToFirst();
            MathHandler.Vector pos = new MathHandler.Vector(c.getFloat(c.getColumnIndex(DB_Settings.COL_POS_X)),c.getFloat(c.getColumnIndex(DB_Settings.COL_POS_Y)),c.getFloat(c.getColumnIndex(DB_Settings.COL_POS_Z)));
            Galaxy galaxy = new Galaxy(c.getString(c.getColumnIndex(DB_Settings.COL_NAME)),c.getFloat(c.getColumnIndex(DB_Settings.COL_VOLUME)),pos, db.getSolarSystemsOfGalaxy(c.getString(c.getColumnIndex(DB_Settings.COL_NAME))), UniverseObjectProperties.OBJECT_GALAXY);


            if(galaxy != null) {
                int adder = 0;
                float x =  galaxy.getX() + adder;
                float y =  galaxy.getY() + adder;
                float z =  galaxy.getZ() + adder;
//                System.out.println("Show " + galaxy.getName() + "@ X:" + x + " Y:" + y + "Z:" + z);

                galaxies[i] = galaxy;
            }
        }

    }

    /**
     * Gets all {@link Star stars} and {@link Planet planets} of this {@link SolarSystem}. They have to be casted.
     * @param parent
     * @return
     *  Contains two arrays
     *  <p>index 0: stars</p>
     *  <p>index 1: planets</p>
     */
    public UniverseObject[][] getSolarSystemChildren(SolarSystem parent){
        Planet[] planets = parent.getPlanets();
        Star[] stars = parent.getStars();
        UniverseObject[][] objects = new UniverseObject[][]{
                stars,planets
        };
        return objects;
    }
    /**
     * Gets all {@link SolarSystem solar systems} of this {@link Galaxy}. They have to be casted.
     * @param parent
     * @return
     */
    public SolarSystem[] getSolarSystems(Galaxy parent){

        return null;
    }

    public Galaxy[] getGalaxies() {
        return galaxies;
    }

    /**
     * Gets all {@link Moon moons} and {@link com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.City cities} of this {@link Planet plan}. They have to be casted.
     * @param parent
     * @return
     *  Contains two arrays
     *  <p>index 0: cities</p>
     *  <p>index 1: moons</p>
     */
    public UniverseObject[][] getPlanetChildren(Planet parent){
        return null;
    }
}

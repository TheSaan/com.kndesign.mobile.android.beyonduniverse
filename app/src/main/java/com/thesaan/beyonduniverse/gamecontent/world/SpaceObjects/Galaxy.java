package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.beyonduniverse.gamecontent.world.World;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Galaxy extends UniverseObject implements ObjectProperties, UniverseObjectProperties {

    SolarSystem[] solarSystems;

    public Galaxy(String name, World world, Map map, int seed) {
        super(name, OBJECT_GALAXY, world, map, seed);
    }

    /**
     * Creates {@link SolarSystem}s in the parent {@link Galaxy}
     *
     * @return
     */
    protected void createSolarSystems() {

        try {
            solarSystems = new SolarSystem[getRandomSolarSystemAmount()];

            for (int i = 0; i < solarSystems.length; i++) {

                //the radius = the width /2
                try {
                    radius = getWidth() / 2f;
                } catch (Exception e) {
                    System.err.println("CalcRadiusEx: " + e);
                }
                //the stars will be created in the middle of the solar System

                //box_test end
                SolarSystem s = new SolarSystem("", this, getMap(), random.nextInt());

                solarSystems[i] = s;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public float getWidth() {
        return (float) MathHandler.squareRootX(volume, 1.0f / 3.0f) / 2.0f;
    }
}

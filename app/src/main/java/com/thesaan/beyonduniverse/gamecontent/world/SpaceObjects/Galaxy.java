package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.handler.MathHandler;
/**
 * Created by mknoe on 28.04.2015.
 */
public class Galaxy extends UniverseObject implements ObjectProperties,UniverseObjectProperties{


    public Galaxy(String name, int type, int seed){
        super(name,type,seed);

    }

    /**
     * Creates {@link SolarSystem}s in the parent {@link Galaxy}
     *
     * @param amount
     * @param parent
     * @return
     */
    protected void createSolarSystems(int amount, Galaxy parent) {
        final int a = amount;
        final Galaxy p = parent;
        try {
            SolarSystem[] solarSystems_ = new SolarSystem[a];

            for (int i = 0; i < solarSystems.length; i++) {

                //the volume
                volume = UniverseObject.setRandomVolume(OBJECT_SOLARSYSTEM,random);
                //the radius = the width /2
                try {

                    radius = (float) MathHandler.squareRootX(volume, 1.0f / 3.0f) / 2.0f;
                } catch (Exception e) {
                    System.err.println("CalcRadiusEx: " + e);
                }
                //the stars will be created in the middle of the solar System

                //test end
                SolarSystem s = new SolarSystem("", volume, p, radius, OBJECT_SOLARSYSTEM,random.nextInt());


                solarSystems[i] = s;

                name = solarSystems[i].getName();

                //Log.v(LOG_TAG_CREATE_WORLD_INFO, "\tCreate solarsystem at position \n" + "X: " + X + "\nY: " + Y + "\nZ: " + Z + "\nwith " + planets2 + " planets and " + stars2 + " Star(s)." + " And called " + name);
            }
            try {
                solarSystems = solarSystems_;
            } catch (Exception error) {
                error.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

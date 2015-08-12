package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Galaxy extends UniverseObject implements ObjectProperties,UniverseObjectProperties{


    public Galaxy(String name, float volume, int type){
        super(name,type);

        this.solarSystems = solarSystems;
        this.volume = volume;
        this.radius = (float) Math.pow(volume, (1.0 / 3.0));

    }

    public void setSolarSystems(SolarSystem[] solarSystems){
        this.solarSystems = solarSystems;
    }
}

package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Asteroid extends UniverseObject {


    public Asteroid(String name, Map map, int seed) {
        super(name, OBJECT_ASTEROID, null, map, seed);

        setRandomRadius();
    }
}

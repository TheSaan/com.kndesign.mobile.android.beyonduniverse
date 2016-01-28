package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.Race;
import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;

/**
 * Created by mknoe on 29.04.2015.
 */
public class City extends UniverseObject implements UniverseObjectProperties {

    int race;

    Market market;

    public City(String name, UniverseObject parent, Map map, int seed) {
        super(name, OBJECT_CITY, parent, map, seed);
    }

    public long getPopulation() {
        return population;
    }

    /**
     * Creates {@link Market} with trading properties.
     *
     * @return
     */
    public void createMarket() {
        market = new Market(new Race(race),name,parent.getPlanetType());
    }

}

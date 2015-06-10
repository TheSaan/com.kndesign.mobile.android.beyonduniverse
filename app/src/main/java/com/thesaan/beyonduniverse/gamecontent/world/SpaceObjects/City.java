package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;

/**
 * Created by mknoe on 29.04.2015.
 */
public class City extends UniverseObject implements UniverseObjectProperties {

    public City(String name, long population, Market market, int type){
        super(name,population,market,type);
    }

    public Market getMarket(){
        return market;
    }
    public long getPopulation(){
        return population;
    }
}

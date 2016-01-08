package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;

/**
 * Created by mknoe on 29.04.2015.
 */
public class City extends UniverseObject implements UniverseObjectProperties {

    public City(String name, long population, Market market, int type,int seed){
        super(name,population,market,type,seed);
    }

    public Market getMarket(){
        return market;
    }
    public long getPopulation(){
        return population;
    }


    /**
     * Creates {@link Market}s for all cities.
     *
     * @param amount
     * @return
     */
    private Market[] createMarkets(int amount) {

        return new Market[amount];
    }

}

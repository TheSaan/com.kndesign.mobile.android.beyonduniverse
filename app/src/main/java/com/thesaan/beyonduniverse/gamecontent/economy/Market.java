package com.thesaan.beyonduniverse.gamecontent.economy;

import com.thesaan.beyonduniverse.gamecontent.Race;

/**
 * Created by mknoe on 29.04.2015.
 */
public class Market {

    //TODO Create market
    public final static int BUY = 2500;
    public final static int SELL = 2501;

    private String parent;
    private int planetType;
    private Race myRace;
    private MarketProductPalette products;

    /**
     * This constructor creates a random market object.
     * @param race
     *  For defining the sortiment.
     * @param parentCityName
     * @param planetType
     *  The type of planet tells the market which product palette it can sell.
     *
     *
     */
    public Market(Race race, String parentCityName, int planetType){
        
        parent = parentCityName;
        myRace = race;
        this.planetType = planetType;
//      TODO reactivate product setter
//        products = new MarketProductPalette(MainActivity.globalContext,myRace,this.planetType);
    }

    /**
     * This market becomes created from database.
     *
     * @param race
     * @param parent
     * @param planetType
     */
    public Market(int race, String parent, int planetType, MarketProductPalette palette){
        myRace = new Race(race);
        this.parent = parent;
        this.planetType = planetType;

        products = palette;
    }

    public Item[][] getProducts(){
        return products.getMySortiment();
    }


}

package com.thesaan.beyonduniverse.gamecontent.economy;

import android.content.Context;
import android.database.Cursor;

import com.thesaan.beyonduniverse.MainActivity;
import com.thesaan.beyonduniverse.gamecontent.Race;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.database.UniverseDatabase;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 01.05.2015.
 */
public class MarketProductPalette implements UniverseObjectProperties{

    private Race myRace;
    private int planetType;

    private UniverseDatabase db;


    public final static int ITEM_PASSENGERS = 198;
    public final static int ITEM_LIVESTOCK = 199;
    public final static int ITEM_RESOURCE = 200;
    public final static int ITEM_MATERIAL = 201;
    public final static int ITEM_WEAPON = 202;
    public final static int ITEM_ARMOR = 203;
    public final static int ITEM_FOOD = 204;
    public final static int ITEM_BAVERAGE = 205;
    public final static int ITEM_LUXURY = 206;
    public final static int ITEM_MEDICINE = 207;
    public final static int ITEM_DRUGS = 208;

    //ship items
    public final static int ITEM_SHIP_UPGRADE = 209;
    public final static int ITEM_SHIP_COLOR = 210;
    public final static int ITEM_SHIP_ENGINE = 211;
    public final static int ITEM_SHIP_WEAPON = 212;
    public final static int ITEM_SHIP_SHIELD = 213;
    public final static int ITEM_SHIP_HULL = 214;

    private Item[] weapons;
    private Item[] armors;
    private Item[] livestocks;
    private Item[] resources;
    private Item[] materials;
    private Item[] foods;
    private Item[] baverages;
    private Item[] luxuries;
    private Item[] medicine;
    private Item[] drugs;
    private Item[] shipUpgrades;
    private Item[] shipColors;
    private Item[] shipEngines;
    private Item[] shipWeapons;
    private Item[] shipShields;
    private Item[] shipHulls;

    Context context;

    /**
     * The types of items which are able to get produced on the parent planet.
     */
    private int[] possibleProducts;

    private Item[][] mySortiment;

    public MarketProductPalette(Context context,Race race, int planetType){
        myRace = race;
        db = MainActivity.uDb;
        this.context = context;
        this.planetType = planetType;
        mySortiment = createSortiment();
    }
    public MarketProductPalette(Cursor c, UniverseDatabase db){

        this.db = db;
        planetType = c.getInt(c.getColumnIndex(DB_Settings.COL_PlANET_TYPE));
        myRace = new Race(c.getInt(c.getColumnIndex(DB_Settings.COL_RACE)));


    }

    private Item[][] createSortimentFromCursor(Cursor c){

        Item[][] sortiment = new Item[c.getCount()][];


        return sortiment;
    }
    /**
     * The palette is an array of items of the same item type like Resource, food, ...
     * @param item_type
     * To select the max amount of the item
     * @param productType
     * Which product
     * @return
     */
    private Item[] createPalette(int item_type, int productType) throws NullPointerException {
        Random r = new Random();

        int[] drawIds = null;

        //the array of the selected item drawable ids
        if(db == null)
            db = new UniverseDatabase(context);

        drawIds = db.getDrawableId(myRace,productType);

        //get the random drawable id
        int id = RandomHandler.createIntegerFromRange(0,drawIds.length,r);

        int amount = RandomHandler.createIntegerFromRange(0, db.getMaxAmountWhereDrawable(id), r);

        Item[] items = new Item[amount];

        for(int i = 0; i < items.length;i++){

            Item item = new Item(context,myRace, db.getNameWhereDrawable(id),id);
            items[i] = item;
        }

        return items;
    }

    /**
     * creates an 2d array of different {@link #createPalette(int, int) palettes}
     * @return
     */
    private Item[][] createSortiment(){
        Random r = new Random();



        //select the possible palette on this planet
        int[] posProducts = getPossibleProducts(planetType);

        Item[][] sortiment = new Item[posProducts.length][];
        //get a random product of the possibles
        int productType = posProducts[RandomHandler.createIntegerFromRange(0,posProducts.length,r)];

        for(int i = 0; i < posProducts.length;i++){
            Item[] items = createPalette(posProducts[i],productType);
            sortiment[i] = items;
        }

        return sortiment;
    }

    /**
     * Returns an array of possible item types choosen by the type of the planet.
     * @param planetType
     * @return
     */
    public static int[] getPossibleProducts(int planetType){
        switch (planetType){
            case PLANET_TYPE_GRASS:
            case PLANET_TYPE_DSCHUNGLE:{
                return new int[]{

               ITEM_RESOURCE,
               ITEM_MATERIAL,
               ITEM_WEAPON,
               ITEM_ARMOR,
               ITEM_FOOD,
               ITEM_BAVERAGE,
               ITEM_LUXURY,
               ITEM_MEDICINE,
               ITEM_DRUGS,
                };
            }
            case PLANET_TYPE_STONE:
            case PLANET_TYPE_MAGMA:{
                return new int[]{
                        ITEM_RESOURCE,
                };
            }
            case PLANET_TYPE_WATER:{
                return new int[]{
                        ITEM_BAVERAGE,
                        ITEM_RESOURCE
                };
            }
            default: return null;
        }
    }

    public Item[][] getMySortiment(){
        return mySortiment;
    }
}

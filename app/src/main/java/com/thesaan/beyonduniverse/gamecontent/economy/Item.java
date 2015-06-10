package com.thesaan.beyonduniverse.gamecontent.economy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.thesaan.beyonduniverse.MainActivity;
import com.thesaan.beyonduniverse.gamecontent.Drawables;
import com.thesaan.beyonduniverse.gamecontent.Race;
import com.thesaan.gameengine.android.handler.XmlHandler;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;

/**
 * Created by mknoe on 29.04.2015.
 */
public class Item {

    Context context;


    //the names
    public static final String[] names = {

    };
    private final int ICON_DIMENSION = 48;
    //trade items
    public final static int ITEM_TYPE_PASSENGERS = 198;
    public final static int ITEM_TYPE_LIVESTOCK = 199;
    public final static int ITEM_TYPE_RESOURCE = 200;
    public final static int ITEM_TYPE_MATERIAL = 201;
    public final static int ITEM_TYPE_WEAPON = 202;
    public final static int ITEM_TYPE_ARMOR = 203;
    public final static int ITEM_TYPE_FOOD = 204;
    public final static int ITEM_TYPE_BAVERAGE = 205;
    public final static int ITEM_TYPE_LUXURY = 206;
    public final static int ITEM_TYPE_MEDICINE = 207;
    public final static int ITEM_TYPE_DRUGS = 208;
    public final static int ITEM_TYPE_COMMERCIALGOOD = 215;

    //ship items
    public final static int ITEM_TYPE_SHIP_UPGRADE = 209;
    public final static int ITEM_TYPE_SHIP_COLOR = 210;
    public final static int ITEM_TYPE_SHIP_ENGINE = 211;
    public final static int ITEM_TYPE_SHIP_WEAPON = 212;
    public final static int ITEM_TYPE_SHIP_SHIELD = 213;
    public final static int ITEM_TYPE_SHIP_HULL = 214;

    //single items

    //planetary resources
    final static String RES_IRON = "iron";
    final static String RES_WOOD = "wood";
    final static String RES_URANIUM = "uranium";
    final static String RES_PLUTONIUM = "plutonium";
    final static String RES_COPPER = "copper";
    final static String RES_SILVER = "silver";
    final static String RES_GOLD = "gold";
    final static String RES_PLATINIUM = "platinum";
    final static String RES_TITAN = "titanium";
    final static String RES_PALLADIUM = "palladium";
    final static String RES_ALUMINIUM = "aluminium";
    final static String RES_WATER = "water";
    final static String RES_SILICIUM = "silicium";
    final static String RES_OIL = "oil";

    final static String MAT_STEEL = "steel";
    final static String MAT_FUEL = "fuel";
    final static String MAT_BATTERIES = "batteries";


    final static String MAT_LIFESTOCK = "lifestock";
    final static String MAT_FOOD = "food";
    final static String MAT_BAVERAGES = "baverages";

    private String[] itemNames = {
            RES_IRON,RES_WOOD,RES_URANIUM,
            RES_PLUTONIUM,RES_COPPER,RES_SILICIUM,
            RES_SILVER,RES_GOLD,RES_PLATINIUM,
            RES_TITAN,RES_PALLADIUM,RES_ALUMINIUM,
            RES_WATER,RES_OIL
    };

    public static File ITEM_TYPE_IMAGE_FOLDER = MainActivity.internalDir;


    private boolean illegality = false;

    private double price;
    private double quality;
    private double quantity;
    private String name;
    private int itemType;
    private double containerSpace;

    //DE: Ablaufdatum
    private double expirationDate;

    static XmlHandler xml;
    
    Bitmap icon;

    Race myRace;

    private int myDrawableId;

    /**
     * Just the data holder for each created Item from database.
     * @param context
     * @param race
     * @param name
     * @param itemType
     */
    public Item(Context context, Race race, String name, int itemType){
        this.context = context;

        myRace = race;
        myDrawableId = getDrawableId(itemType,name);

        setIcon(myDrawableId);

        try {
            xml = new XmlHandler(XmlHandler.FILENAME_GAMECONTENT_XML, context);
        }catch (XmlPullParserException xmlparseEx) {
            System.err.println("XmlPull Ex");
        }
    }
    public Item(int race, String name, int itemType, int amount){
        quantity = amount;
        myRace = new Race(race);
        this.name = name;
        this.itemType = itemType;
    }
    //TODO create item root class
    
    public Bitmap getMyIcon(){
        return icon;
    }


    public void setIcon(int id){
        icon = BitmapFactory.decodeResource(context.getResources(), id);
        if(icon == null)
            System.err.println("Item icon not set!");
    }
    public Bitmap getIcon(){
        return icon;
    }

    //TODO Ich muss noch eine Item Erkennung erstellen die die items den datenbank spalten zuordnen kann

    /*private void putValue(ContentValues cv){
        cv.put()
    }*/

    /**
     * TODO This method has to get changed for querying a cursor in database table ITEMS
     * Has to get changed (follow TO-DO instructions)
     * @param itemType
     * @param itemXmlTagName
     * @return
     */
    public int getDrawableId(int itemType, String itemXmlTagName) {

        int id = 0;
        try {
            //gets the array index from the xml tag "arrayindex"
            id = Drawables.getItemDrawable(myRace,itemType)[xml.getIntAttribute(itemXmlTagName, XmlHandler.ATTR_ARRAYINDEX)];
            return id;
        }catch (XmlPullParserException pEx) {
        }
        return id;
    }


}

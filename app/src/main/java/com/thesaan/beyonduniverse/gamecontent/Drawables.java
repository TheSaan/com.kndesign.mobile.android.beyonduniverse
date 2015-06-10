package com.thesaan.beyonduniverse.gamecontent;

/**
 * Created by mknoe on 01.05.2015.
 */
public class Drawables {

    //the array indexes
    public final static int DRAWABLE_ITEMS = 0;
    public final static int DRAWABLE_ITEMS_BAVERAGES = 0;
    public final static int DRAWABLE_ITEMS_FOOD = 1;
    public final static int DRAWABLE_ITEMS_LIVESTOCK = 2;
    public final static int DRAWABLE_ITEMS_LUXURY = 3;
    public final static int DRAWABLE_ITEMS_MATERIALS = 4;
    public final static int DRAWABLE_ITEMS_PASSENGERS = 5;
    public final static int DRAWABLE_ITEMS_RESOURCES = 6;

    public final static int[] ITEM_CATEGORIES = {
            DRAWABLE_ITEMS_BAVERAGES,
            DRAWABLE_ITEMS_FOOD,
            DRAWABLE_ITEMS_LIVESTOCK,
            DRAWABLE_ITEMS_LUXURY,
            DRAWABLE_ITEMS_MATERIALS,
            DRAWABLE_ITEMS_PASSENGERS,
            DRAWABLE_ITEMS_RESOURCES
    };

    public final static int NUMBER_OF_ITEM_CATEGORIES = 7;


    public final static int DRAWABLE_MENU = 1;
    public final static int DRAWABLE_OBJECTS = 2;
    public final static int DRAWABLE_OBJECTS_CITIES = 0;
    public final static int DRAWABLE_SHIPS = 3;


    /**
     * Crox resource ids
     */

    private static int[] croxBaverages = {};
    private static int[] croxFood = {};
    private static int[] croxLivestock = {};
    private static int[] croxLuxury = {};
    private static int[] croxMaterials = {};
    private static int[] croxPassenger = {};
    private static int[] croxResources = {};
    private static int[] croxCities = {};

    private static int[] humanBaverages = {};
    private static int[] humanFood = {};
    private static int[] humanLivestock = {};
    private static int[] humanLuxury = {};
    private static int[] humanMaterials = {};
    private static int[] humanPassenger = {};
    private static int[] humanResources = {};
    private static int[] humanCities = {};

    private static int[] highsBaverages = {};
    private static int[] highsFood = {};
    private static int[] highsLivestock = {};
    private static int[] highsLuxury = {};
    private static int[] highsMaterials = {};
    private static int[] highsPassenger = {};
    private static int[] highsResources = {};
    private static int[] highsCities = {};

    /////////////////////////////////////////////////////////
    private static int[][] croxItems = {
            croxBaverages,
            croxFood,
            croxLivestock,
            croxLuxury,
            croxMaterials,
            croxPassenger,
            croxResources
    };

    private static int[][] croxMenu = {};
    private static int[][] croxObject = {
            croxCities
    };
    private static int[][] croxShip = {};
    public static int[][][] drawablesCrox = {
            croxItems,
            croxMenu,
            croxObject,
            croxShip
    };
    /////////////////////////////////////////////////////////
    private static int[][] humanItems = {
            humanBaverages,
            humanFood,
            humanLivestock,
            humanLuxury,
            humanMaterials,
            humanPassenger,
            humanResources
    };
    private static int[][] humanMenu = {};
    private static int[][] humanObject = {
            humanCities
    };
    private static int[][] humanShip = {};
    public static int[][][] drawablesHuman = {
            humanItems,
            humanMenu,
            humanObject,
            humanShip
    };
    /////////////////////////////////////////////////////////
    private static int[][] highsItems = {
            highsBaverages,
            highsFood,
            highsLivestock,
            highsLuxury,
            highsMaterials,
            highsPassenger,
            highsResources
    };
    private static int[][] highsMenu = {};
    private static int[][] highsObject = {
            highsCities
    };
    private static int[][] highsShip = {};
    /**
     * The id arrays to create a random asteroid image
     */
    public static int[][][] drawablesHighs = {
            highsItems,
            highsMenu,
            highsObject,
            highsShip
    };
    /////////////////////////////////////////////////////////


    /**
     * The id arrays to create a random star image
     */
    public static int[] drawablesStars = {};
    /**
     * The id arrays to create a random planet image
     */
    public static int[] drawablesPlanets = {};
    /**
     * The id arrays to create a random asteroid image
     */
    public static int[] drawablesAsteroids = {};
    /**
     * The id arrays to create a random cpmet image
     */
    public static int[] drawablesComets = {};
    /**
     * The id arrays to create a random moon image
     */
    public static int[] drawablesMoons = {};
    /**
     * The id arrays to create a random galaxy image
     */
    public static int[] drawablesGalaxies = {};

    public static int[] getItemDrawable(Race race, int produtType) {
        switch (race.getType()) {
            case Race.RACE_HUMAN: {
                return drawablesHuman[DRAWABLE_ITEMS][produtType];
            }
            case Race.RACE_HIGHS: {
                return drawablesHighs[DRAWABLE_ITEMS][produtType];
            }
            case Race.RACE_CROX: {
                return drawablesCrox[DRAWABLE_ITEMS][produtType];
            }
            default:
                return null;
        }
    }
}
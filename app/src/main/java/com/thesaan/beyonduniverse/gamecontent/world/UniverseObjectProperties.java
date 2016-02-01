package com.thesaan.beyonduniverse.gamecontent.world;

/**
 * Created by mknoe on 28.04.2015.
 */
public interface UniverseObjectProperties {

    int  INFO_POSITION = 5001;

    //seed for initialization
    int randomSeed = 1561488911;

    int STAR_BLUE = 101010;
    int STAR_YELLOW = 101011;
    int STAR_RED = 101012;
    int STAR_ORANGE = 101013;


    /*//the actual objects
    Used as identificator when creating a new instance of UniverseObject.
     */
    int OBJECT_WORLD = 100000;
    int OBJECT_UNIVERSE = 100001;
    int OBJECT_GALAXY = 100002;
    int OBJECT_SOLARSYSTEM = 100003;
    int OBJECT_STAR = 100004;
    int OBJECT_PLANET = 100005;
    int OBJECT_MOON = 100006;
    int OBJECT_ASTEROID = 100007;
    int OBJECT_COMET = 100008;
    int OBJECT_CITY = 100009;

    /*
    Used to categorize the kind of planets
     */
    int PLANET_TYPE_GRASS = 100010;
    int PLANET_TYPE_WATER = 100011;
    int PLANET_TYPE_DSCHUNGLE = 100012;
    int PLANET_TYPE_SNOW = 100013;
    int PLANET_TYPE_MAGMA = 100014;
    int PLANET_TYPE_DESSERT = 100015;
    int PLANET_TYPE_STONE = 100016;
    int PLANET_TYPE_ICE = 100017;
    int PLANET_TYPE_LOST = 100018;

    //Sizes
    /*
    These values are the relative properties for setting up other object properties
     */
    int EARTH_RADIUS = 6371;
    float EARTH_MASS = 1.0f;
    float EARTH_AVG_TEMPERATURE = 15f;
    float EARTH_SCOPE = (float) (EARTH_RADIUS * 2 * Math.PI);

    float SUN_MASS = 1.99f;//Quadrillion tons (1.990.000.000.000 t)

    /*
     *To set min & max values for habitability
     */
    int MAX_TEMPERATURE_TO_SURVIVE = 63;
    int MIN_TEMPERATURE_TO_SURVIVE = -45;

    /**
     * Units
     */
    //meters
    float M = 1f;
    //meters per pixel
    float MpP = 10f;
    //kilometers
    float KM = MpP * 10f;
    //int. miles
    float MILE = KM * 1.609344f;
    //astronomical units
    //ger.: AE - Astronomische Einheit
    float AU = (float) (1.49597870700f * Math.pow(10f, 8f) * KM);


    /**
     * To create unique object names
     */
    final String[] ALPHABET = {
            "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public final String LOG_TAG_CREATE_WORLD_INFO = "cwInfo";

    int MAX_UNIVERSE_OBJECTS = 1000000;
    int MAX_GALAXIES = 10000;
    int MAX_SOLAR_SYSTEMS_IN_GALAXY = 10000;


    int MAX_PLANETS_IN_SOLAR_SYSTEM = 15;
    int MAX_MOONS_AROUND_PLANET = 50;
    int MAX_CITIES_ON_PLANET = 10;

}

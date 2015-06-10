package com.thesaan.beyonduniverse.gamecontent.world;

import com.thesaan.gameengine.android.handler.MathHandler;

/**
 * Created by mknoe on 28.04.2015.
 */
public interface UniverseObjectProperties{




    int STAR_BLUE = 101010;
    int STAR_YELLOW = 101011;
    int STAR_RED = 101012;
    int STAR_ORANGE = 101013;


    //the actual objects
    int OBJECT_WORLD= 100000;
    int OBJECT_UNIVERSE = 100001;
    int OBJECT_GALAXY = 100002;
    int OBJECT_SOLARSYSTEM = 100003;
    int OBJECT_STAR = 100004;
    int OBJECT_PLANET = 100005;
    int OBJECT_MOON = 100006;
    int OBJECT_ASTEROID = 100007;
    int OBJECT_COMET = 100008;
    int OBJECT_CITY = 100009;

    int PLANET_TYPE_GRASS = 100010;
    int PLANET_TYPE_WATER = 100011;
    int PLANET_TYPE_DSCHUNGLE = 100012;
    int PLANET_TYPE_MAGMA = 100013;
    int PLANET_TYPE_DESSERT = 100014;
    int PLANET_TYPE_STONE = 100015;
    int PLANET_TYPE_SNOW = 100016;
    int PLANET_TYPE_ICE = 100017;
    int PLANET_TYPE_LOST = 100018;

    int MAX_UNIVERSE_OBJECTS = 1000000;


    //Sizes
   int EARTH_RADIUS = 6371;
   float EARTH_MASS = 1.0f;
   float EARTH_TEMPERATURE = 15f;
   float EARTH_SCOPE = (float) (EARTH_RADIUS*2* Math.PI);

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
   float KM = 1000f;
    //int. miles
   float MILE = KM*1.609344f;
    //astronomical units
   float AU = (float) (1.49597870700f* Math.pow(10f, 8f)*KM);




    /**
     * To create unique object names
     */
    String[] alphabet = {
            "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"
    };

    public final String LOG_TAG_CREATE_WORLD_INFO = "cwInfo";
    
   int numberOfAllowedGalaxies = 300;
   int numberOfAllowedSolarSystems = (MAX_UNIVERSE_OBJECTS - numberOfAllowedGalaxies) / 33;
   int numberOfAllowedPlanets = (MAX_UNIVERSE_OBJECTS - numberOfAllowedSolarSystems) / 4;
   int numberOfAllowedMoons = (MAX_UNIVERSE_OBJECTS - numberOfAllowedPlanets) / 10;
   int numberOfAllowedStars = (int) Math.round(numberOfAllowedSolarSystems * 1.3);
   int numberOfAllowedCities = (MAX_UNIVERSE_OBJECTS - numberOfAllowedMoons) / 10;

   int numberMaxSolarSystemsInGalaxy = numberOfAllowedSolarSystems / numberOfAllowedGalaxies;
   int numberMinSolarSystemsInGalaxy = 50;


   int MAX_MOONS_FOR_PLANET = 8;

   float UNIVERSE_VOLUME = 100000.00f;
   float UNIVERSE_X =(float) MathHandler.squareRootX(UNIVERSE_VOLUME, 3);
   float UNIVERSE_Y = UNIVERSE_X;
   float UNIVERSE_Z = UNIVERSE_X;

    //the maximum size of a galaxy
   float GALAXY_MAX_Z = UNIVERSE_Z / numberOfAllowedGalaxies;
   float GALAXY_MAX_X = UNIVERSE_X / numberOfAllowedGalaxies;
   float GALAXY_MAX_Y = UNIVERSE_Y / numberOfAllowedGalaxies;

    //the minimum size of a galaxy so that at least fit 50 solar systems inside
   float GALAXY_MIN_Z = GALAXY_MAX_Z / numberMinSolarSystemsInGalaxy;
   float GALAXY_MIN_X = GALAXY_MAX_X / numberMinSolarSystemsInGalaxy;
   float GALAXY_MIN_Y = GALAXY_MAX_Y / numberMinSolarSystemsInGalaxy;

    //the maximum size of a solar system
   float SOLARSYSTEM_MAX_Z = GALAXY_MAX_Z / numberMaxSolarSystemsInGalaxy;
   float SOLARSYSTEM_MAX_X = GALAXY_MAX_X / numberMaxSolarSystemsInGalaxy;
   float SOLARSYSTEM_MAX_Y = GALAXY_MAX_Y / numberMaxSolarSystemsInGalaxy;

   float SOLARSYSTEM_MIN = SOLARSYSTEM_MAX_X / numberMinSolarSystemsInGalaxy;


   float averageAmountOfPlanetsInSolarSystem = numberOfAllowedPlanets / numberOfAllowedSolarSystems;
   float averageAmountOfMoonsAroundPlanet = numberOfAllowedPlanets / numberOfAllowedMoons;
   float numberOfAvailableObjects = MAX_UNIVERSE_OBJECTS - (numberOfAllowedStars + numberOfAllowedPlanets + numberOfAllowedMoons + numberOfAllowedSolarSystems + numberOfAllowedCities + numberOfAllowedGalaxies);


}

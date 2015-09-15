package com.thesaan.beyonduniverse.gamecontent.world;

import android.content.Context;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thesaan.beyonduniverse.MainActivity;
import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.City;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Moon;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Planet;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.SolarSystem;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Star;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.gameengine.android.database.AppDatabase;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * The Universe class represents the creation algorithm for all objects
 * which will be created randomly (or if set, also predefined objects).
 * <p/>
 * There are some constants in {@link UniverseObjectProperties} which
 * define the final size or amounts for all included objects.
 * <p/>
 * Also included is the call for adding the created objects to {@link AppDatabase}
 * or {@link com.thesaan.gameengine.android.database.ServerDatabase} if the implementation
 * is done.
 */
public class Universe implements UniverseObjectProperties {

    Context context;
    AppDatabase uDb;

    //laoding
    float loadProgress = 0;
    int objectsToLoad = 0;

    //the value in the log text
    float mass, radius, degrees, volume, population, moons2, solarsystems2, planets2, stars2, cities2;
    int planetType;
    String name;
    float X, Y, Z;

    /**
     * Static array to get the set objects out of the thread object
     */
    private UniverseObject[] objects;

    float availableObjects = numberOfAvailableObjects;

    /**
     * These counters count the number of created objects
     * which should be the same as the indexes in the
     * certain table of the objects
     */
    private int galaxyIndex = 0, solarSystemIndex = 0, starIndex = 0, planetIndex = 0, moonIndex = 0, cityIndex = 0;

    private int[] amountIndexes = {
            galaxyIndex,
            solarSystemIndex,
            starIndex,
            planetIndex,
            moonIndex,
            cityIndex
    };


    Random r;

    /**
     * The universe object creates all objects like galaxies, solar systems, planets (+ thier cities), moons and stars.
     * <p/>
     * This class is only for the initializing step before running the game on the market. All created objects will be saved
     * in a server stored database.
     *
     * @param context
     * @param db
     */
    public Universe(Context context, AppDatabase db) {

        this.context = context;
        uDb = db;
        r = new Random();


        try {
            Thread T = new Thread(new Runnable() {
                @Override
                public void run() {
//                    createWorld();
//                    createGalaxies(15, 5);

                    //test method
                    //uDb.addEmptyGalaxy();
                }
            });

            T.start();

        } catch (Exception e) {
            System.err.println("Create Galaxy ERROR: ");
            e.printStackTrace();
        }

    }

    /**
     * Set the status of for the loading progress bar
     * //TODO not implemented
     *
     * @param progress
     */
    public void setLoadingProgressBar(int progress) {

    }

    /**
     * Creates a certain amount of {@link Galaxy}s with a
     * certain amount of {@link SolarSystem}s
     *
     * @param amount
     */
    private void createGalaxies(int amount, int amountOfSystems) {


        Galaxy g = new Galaxy("Milchstraße", UniverseObject.setRandomVolume(OBJECT_GALAXY), OBJECT_GALAXY);
        g.setSolarSystems(createSolarSystems(amountOfSystems, g));
        uDb.addGalaxy(g);

        g = new Galaxy("Andromeda", UniverseObject.setRandomVolume(OBJECT_GALAXY), OBJECT_GALAXY);
        g.setSolarSystems(createSolarSystems(amountOfSystems, g));
        uDb.addGalaxy(g);

        for (int i = 0; i < amount - 2; i++) {
            g = new Galaxy("", UniverseObject.setRandomVolume(OBJECT_GALAXY), OBJECT_GALAXY);
            g.setSolarSystems(createSolarSystems(amountOfSystems, g));
            uDb.addGalaxy(g);
        }

    }

    private Galaxy[] createWorld() {

        try {
            Thread T = new Thread(new Runnable() {
                @Override
                public void run() {
                    Random r = new Random();
                    int amountOfGalaxies = RandomHandler.createIntegerFromRange(50, numberOfAllowedGalaxies, r);
                    Galaxy[] galaxies = new Galaxy[amountOfGalaxies];

                    addObjectsToLoad(amountOfGalaxies);

                    //create galaxy after galaxy
                    for (int i = 0; i < amountOfGalaxies; i++) {

                        final int numberOfSolarsystems = RandomHandler.createIntegerFromRange(50, numberOfAllowedSolarSystems / amountOfGalaxies, r);

                        //count created galaxies
                        galaxyIndex++;

                        //the volume
                        volume = UniverseObject.setRandomVolume(OBJECT_GALAXY);
                        //create solar systems for galaxy


                        Galaxy g = new Galaxy("", volume, OBJECT_GALAXY);


                        SolarSystem[] solarSystems = createSolarSystems(numberOfSolarsystems, g);

                        //test
                        solarsystems2 = solarSystems.length;

                        g.setSolarSystems(solarSystems);

                        X = g.getPosition().getXf();
                        Y = g.getPosition().getYf();
                        Z = g.getPosition().getZf();
                        //test

                        //new galaxy with random data
                        galaxies[i] = g;

                        name = galaxies[i].getName();

                        Log.v(LOG_TAG_CREATE_WORLD_INFO, "Create galaxy at positionX: " + X + "\nY: " + Y + "\nZ: " + Z + " with\n" + solarsystems2 + " solarsystems.\n Volume: " + volume + " and called " + name);
                    }
                    objects = galaxies;
                }
            });

            T.start();
            try {
                return (Galaxy[]) objects;
            } catch (Exception error) {
                error.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates {@link SolarSystem}s in the parent {@link Galaxy}
     *
     * @param amount
     * @param parent
     * @return
     */
    private SolarSystem[] createSolarSystems(int amount, Galaxy parent) {
        final int a = amount;
        final Galaxy p = parent;
        try {
            SolarSystem[] solarSystems = new SolarSystem[a];

            for (int i = 0; i < solarSystems.length; i++) {

                solarSystemIndex++;

                //the volume
                volume = UniverseObject.setRandomVolume(OBJECT_SOLARSYSTEM);
                //the radius = the width /2
                try {

                    radius = (float) MathHandler.squareRootX(volume, 1.0f / 3.0f) / 2.0f;
                } catch (Exception e) {
                    System.err.println("CalcRadiusEx: " + e);
                }
                //the stars will be created in the middle of the solar System

                //test
                SolarSystem s = new SolarSystem("", volume, p, radius, OBJECT_SOLARSYSTEM);

                Star[] stars = createStars(s.getPosition());

                Planet[] planets = createPlanets(RandomHandler.createIntegerFromRange(0, 20, r), s.getPosition());

                s.setStars(stars);
                s.setPlanets(planets);

                X = s.getPosition().getXf();
                Y = s.getPosition().getYf();
                Z = s.getPosition().getZf();

                solarSystems[i] = s;

                name = solarSystems[i].getName();

                Log.v(LOG_TAG_CREATE_WORLD_INFO, "\tCreate solarsystem at position \n" + "X: " + X + "\nY: " + Y + "\nZ: " + Z + "\nwith " + planets2 + " planets and " + stars2 + " Star(s)." + " And called " + name);
            }
            try {
                return solarSystems;
            } catch (Exception error) {
                error.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates {@link Planet}s in the parent solar system
     *
     * @param amount
     * @param solarSystemPosition
     * @return
     */
    private Planet[] createPlanets(int amount, MathHandler.Vector solarSystemPosition) {
        final int a = amount;
        final MathHandler.Vector sPos = solarSystemPosition;
        try {

            Planet[] planets = new Planet[a];

            City[] cities;

            addObjectsToLoad(a);

            for (int i = 0; i < planets.length; i++) {

                planetIndex++;

                MathHandler.Vector vec = UniverseObject.setRandomPlanetPosition(sPos.getXf(), sPos.getYf(), sPos.getZf());
                if (vec == null)
                    System.err.println("vec is null");

                degrees = UniverseObject.setRandomTemperature(OBJECT_PLANET);

                if (degrees < MAX_TEMPERATURE_TO_SURVIVE && degrees > MIN_TEMPERATURE_TO_SURVIVE) {
                    if (degrees <= 3) {
                        planetType = PLANET_TYPE_SNOW;
                    } else if (degrees < 35 && degrees > 3) {
                        planetType = UniverseObject.setRandomPlanetType();
                    } else if (degrees > 45 && degrees < MAX_TEMPERATURE_TO_SURVIVE) {
                        planetType = PLANET_TYPE_DESSERT;
                    }
                    cities = createCities(RandomHandler.createIntegerFromRange(0, 5, r));

                    //reset array
                    objects = null;
                    addObjectsToLoad(cities.length);
                } else {

                    if (degrees < 500 && degrees > 0) {
                        planetType = PLANET_TYPE_STONE;
                    } else if (degrees >= 500) {
                        planetType = PLANET_TYPE_MAGMA;
                    } else if (degrees < MIN_TEMPERATURE_TO_SURVIVE) {
                        planetType = PLANET_TYPE_ICE;
                    } else {
                        planetType = PLANET_TYPE_LOST;
                    }
                    cities = null;
                }
                System.err.println("Planet C°: " + degrees + "\nPlanet type: " + Planet.getPlanetTypeName(planetType));

                Moon[] moons = createMoons(RandomHandler.createIntegerFromRange(0, MAX_MOONS_FOR_PLANET, r), vec);

                addObjectsToLoad(moons.length);

                mass = UniverseObject.setRandomMass(OBJECT_PLANET);

                //test
                if (moons != null)
                    moons2 = moons.length;
                else
                    System.err.println("No moons");

                if (cities != null)
                    cities2 = cities.length;
                else
                    System.err.println("No cities. Planet is not habitable!");

                Planet p = new Planet("", vec, mass, moons, cities, degrees, OBJECT_PLANET, planetType);
                planets[i] = p;

                name = planets[i].getName();

//                Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\tCreate planet with " + moons2 + " Moons and " + cities2 + " cities.\nMass: " + mass + " times the earth mass, Radius: " + radius + ", Degrees:  " + degrees + " .\nPopulation: " + population + " and called " + name + "\nPlanet type: " + Planet.getPlanetTypeName(planetType));
            }
            try {
                return planets;
            } catch (Exception error) {
                error.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a single star or with a minimal probability a twin star 1:10000
     *
     * @return
     */
    private Star[] createStars(MathHandler.Vector vector) {

        final MathHandler.Vector vec = vector;
        try {
            int amount;
            //creates a random number to choose if it will be a twin star system
            int prob = RandomHandler.createIntegerFromRange(0, 100000000, r);

            if (prob > 500010000 && prob < 500020000)
                amount = 2;
            else
                amount = 1;

            addObjectsToLoad(amount);

            Star[] stars = new Star[amount];
            //temporary storage for the x position if two stars are set
            float tempX = vec.getXf();
            for (int i = 0; i < amount; i++) {

                //place the two stars beside each other
                if (amount > 1) {
                    //change the vec value a bit
                    if (i == 0)
                        vec.setX(vec.getXf() * 0.999);
                    if (i == 1)
                        vec.setX(tempX * 1.001);
                }
                starIndex++;
                mass = UniverseObject.setRandomMass(OBJECT_STAR);
                degrees = UniverseObject.setRandomTemperature(OBJECT_STAR);

                Star s = new Star("", vec, mass, degrees, OBJECT_STAR);
                stars[i] = s;

                name = stars[i].getName();

                Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\tCreate star with Mass: " + mass + " Temperature: " + degrees + " Radius: " + s.getRadius() + " and called " + name);
            }

            try {
                return stars;
            } catch (Exception error) {
                error.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }

    /**
     * If the parent Planet is habitable, maybe create cities on it.
     *
     * @param amount
     * @return
     */
    private City[] createCities(int amount) {

        final int a = amount;
        try {
            City[] cities = new City[a];
            Market[] markets = new Market[a];

//            System.err.println("Cities " + cities.length);
//            System.err.println("CitiesIndex " + cityIndex);
//            System.err.println("Amount " + amount);

            for (int i = 0; i < cities.length; i++) {
                addObjectsToLoad(1);
                addObjectsToLoad(1);
                try {
                    cityIndex++;
//                    System.err.println("Test City " + cityIndex);
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println("Indexes out of bounds");
                }
//                System.err.println("i " + i);

                //TODO Hier könnte es sein, dass der Maximalwert der random zahl abgecuttet wird da der Integer wert eigentlich zu klein ist
                City c = new City("", RandomHandler.createIntegerFromRange(0, (int) Math.pow(10, 9), r), markets[i], UniverseObjectProperties.OBJECT_CITY);
                cities[i] = c;

                name = cities[i].getName();
                population = cities[i].getPopulation();

                Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\t\tCreate City " + name + " with a Population of " + population);
            }
            try {
                return cities;
            } catch (Exception error) {
                error.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates some Moons arround the planet.
     *
     * @param amount
     * @param vector
     * @return
     */
    private Moon[] createMoons(int amount, MathHandler.Vector vector) {

        final int a = amount;
        final MathHandler.Vector vec = vector;
        try {
            MathHandler.Vector vec2;
            Moon[] moons = new Moon[a];

            for (int i = 0; i < a; i++) {
                moonIndex++;

                mass = UniverseObject.setRandomMass(OBJECT_MOON);
                degrees = UniverseObject.setRandomTemperature(OBJECT_MOON);

                vec2 = UniverseObject.setRandomMoonPosition(vec.getXf(), vec.getYf(), vec.getZf());

                Moon m = new Moon("", vec2, mass, degrees, OBJECT_MOON);
                moons[i] = m;

                name = moons[i].getName();

                Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\t\tCreate Moon \nMass: " + mass + " times the earth mass, Radius: " + m.getRadius() + ", Degrees:  " + degrees + " called " + name);
            }
            try {
                return moons;
            } catch (Exception error) {
                error.printStackTrace();
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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


    /**
     * Sets the loading progress from the number of already loaded created objects
     */
    public void setLoadProgress() {
        try {


            if (context instanceof MainActivity) {
                ProgressBar pb = (ProgressBar) ((MainActivity) context).findViewById(R.id.loadProgressBar);
                TextView tv = (TextView) ((MainActivity) context).findViewById(R.id.loadProgressTextView);

                pb.setProgress(objectsToLoad / 100);
                tv.setText("Load " + objectsToLoad + "/" + (MAX_UNIVERSE_OBJECTS - numberOfAvailableObjects));
            } else {
                System.err.println("Wrong context!");
            }
        } catch (Exception e) {
            System.err.println("SetProgressException\n" + e);
        }
    }

    private void addObjectsToLoad(int objectsToLoad) {
        try {
            this.objectsToLoad += objectsToLoad;
//            System.out.println("Objects to load: " + this.objectsToLoad);

        } catch (Exception e) {
            System.err.println("Add Object To Load Exception\n" + e);
        }
    }

    /**
     * Only resets the temporary properties for the log monitoring
     */
    private void resetProperties() {
        mass = 0;
        degrees = 0;
        radius = 0;
        volume = 0;
        name = "";
        population = 0;
        planetType = 0;
    }
}

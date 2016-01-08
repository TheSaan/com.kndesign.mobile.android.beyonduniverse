package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 28.04.2015.
 */
public class SolarSystem extends UniverseObject {


    double volume;
    Planet[] planets;
    City[] cities;
    Star[] stars;

    public SolarSystem(String name, float volume, Galaxy parent, float radius, int type, int seed){
        super(name,parent,radius,type,seed);
        this.volume = volume;
    }

    public void setStars(Star[] stars){
        this.stars = stars;
    }

    public void setPlanets(){
        Planet[] planets = createPlanets(RandomHandler.createIntegerFromRange(0, 20, random), getPosition(),myRandomSeed);
    }

    private Planet[] createPlanets(int amount, MathHandler.Vector solarSystemPosition,int seed) {
        Random r = new Random(seed);

        final int a = amount;
        final MathHandler.Vector sPos = solarSystemPosition;
        try {

            Planet[] planets = new Planet[a];

            for (int i = 0; i < planets.length; i++) {

                MathHandler.Vector vec = UniverseObject.setRandomPlanetPosition(sPos.getXf(), sPos.getYf(), sPos.getZf(),r);
                if (vec == null)
                    System.err.println("vec is null");

                degrees = UniverseObject.setRandomTemperature(OBJECT_PLANET,random);

                if (degrees < MAX_TEMPERATURE_TO_SURVIVE && degrees > MIN_TEMPERATURE_TO_SURVIVE) {
                    if (degrees <= 3) {
                        planetType = PLANET_TYPE_SNOW;
                    } else if (degrees < 35 && degrees > 3) {
                        planetType = UniverseObject.setRandomPlanetType(random);
                    } else if (degrees > 45 && degrees < MAX_TEMPERATURE_TO_SURVIVE) {
                        planetType = PLANET_TYPE_DESSERT;
                    }
                    planets[i].createCities(random);

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
//                System.err.println("Planet C°: " + degrees + "\nPlanet type: " + Planet.getPlanetTypeName(planetType));

                Moon[] moons = planets[i].createMoons(RandomHandler.createIntegerFromRange(0, MAX_MOONS_FOR_PLANET, random), vec);

                mass = UniverseObject.setRandomMass(OBJECT_PLANET,random);

                Planet p = new Planet("", vec, mass, degrees, OBJECT_PLANET, planetType,r.nextInt());
                planets[i] = p;

                name = planets[i].getName();

//                //Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\tCreate planet with " + moons2 + " Moons and " + cities2 + " cities.\nMass: " + mass + " times the earth mass, Radius: " + radius + ", Degrees:  " + degrees + " .\nPopulation: " + population + " and called " + name + "\nPlanet type: " + Planet.getPlanetTypeName(planetType));
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
            int prob = RandomHandler.createIntegerFromRange(0, 100000000, random);

            if (prob > 500010000 && prob < 500020000)
                amount = 2;
            else
                amount = 1;


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
                mass = UniverseObject.setRandomMass(OBJECT_STAR,random);
                degrees = UniverseObject.setRandomTemperature(OBJECT_STAR,random);

                Star s = new Star("", vec, mass, degrees, OBJECT_STAR,random.nextInt());
                stars[i] = s;

                name = stars[i].getName();

                //Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\tCreate star with Mass: " + mass + " Temperature: " + degrees + " Radius: " + s.getRadius() + " and called " + name);
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
}

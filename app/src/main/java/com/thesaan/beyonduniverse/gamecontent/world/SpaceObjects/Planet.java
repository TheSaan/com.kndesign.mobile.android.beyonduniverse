package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Planet extends UniverseObject {

    public Planet(String name, MathHandler.Vector position, float mass, float degrees, int type, int planetType, int seed){
        super(name,position,mass,degrees,type, seed);


        this.planetType = planetType;
        this.type = type;

        setRandomRadius(random);
        if(cities != null) {
            this.cities = cities;

            this.markets = new Market[this.cities.length];

            for (int i = 0; i < cities.length; i++) {
                this.population += cities[i].getPopulation();
                Market m = cities[i].getMarket();
                this.markets[i] = m;
            }
        }
        if(moons != null) {
            this.moons = moons;
        }
    }

    public static String getPlanetTypeName(int myPlanetType){
        switch (myPlanetType){
            case PLANET_TYPE_GRASS: return "Grass planet";
            case PLANET_TYPE_WATER: return "Water planet";
            case PLANET_TYPE_DESSERT: return "Dessert planet";
            case PLANET_TYPE_MAGMA: return "Magma planet";
            case PLANET_TYPE_DSCHUNGLE: return "Dschungle planet";
            case PLANET_TYPE_STONE: return "Stone planet";
            case PLANET_TYPE_SNOW: return "Snow planet";
            case PLANET_TYPE_ICE: return "Ice planet";
            case PLANET_TYPE_LOST: return "Planet lost";
            default: return "no planet type detected";
        }
    }

    public int getPlanetType() {
        return planetType;
    }

    /**
     * Creates some Moons arround the planet.
     *
     * @param amount
     * @param vector
     * @return
     */
    protected Moon[] createMoons(int amount, MathHandler.Vector vector) {

        final int a = amount;
        final MathHandler.Vector vec = vector;
        try {
            MathHandler.Vector vec2;
            Moon[] moons = new Moon[a];

            for (int i = 0; i < a; i++) {

                mass = UniverseObject.setRandomMass(OBJECT_MOON,random);
                degrees = UniverseObject.setRandomTemperature(OBJECT_MOON,random);

                vec2 = UniverseObject.setRandomMoonPosition(vec.getXf(), vec.getYf(), vec.getZf(),random);

                Moon m = new Moon("", vec2, mass, degrees, OBJECT_MOON,random.nextInt());
                moons[i] = m;

                name = moons[i].getName();

                //Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\t\tCreate Moon \nMass: " + mass + " times the earth mass, Radius: " + m.getRadius() + ", Degrees:  " + degrees + " called " + name);
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
     * If the parent Planet is habitable, maybe create cities on it.
     *
     * @param r
     * @return
     */
    protected void createCities(Random r) {

        int seed = r.nextInt();

        try {
            City[] cities_ = new City[RandomHandler.createIntegerFromRange(1,500,random)];


            for (int i = 0; i < cities.length; i++) {
                try {
//                    System.err.println("Test City " + cityIndex);
                } catch (IndexOutOfBoundsException ex) {
                    System.err.println("Indexes out of bounds");
                }
//                System.err.println("i " + i);

                //TODO Hier könnte es sein, dass der Maximalwert der random zahl abgecuttet wird da der Integer wert eigentlich zu klein ist
                City c = new City("", RandomHandler.createIntegerFromRange(0, (int) Math.pow(10, 9), random), markets[i], UniverseObjectProperties.OBJECT_CITY,seed);
                cities[i] = c;

                name = cities[i].getName();
                population = cities[i].getPopulation();

                //Log.v(LOG_TAG_CREATE_WORLD_INFO, "\t\t\tCreate City " + name + " with a Population of " + population);
            }
            try {
                cities =  cities_;
            } catch (Exception error) {
                error.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

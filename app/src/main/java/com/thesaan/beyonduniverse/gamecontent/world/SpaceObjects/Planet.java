package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.economy.Market;
import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.UniverseObjectProperties;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 28.04.2015.
 */
public class Planet extends UniverseObject {

    private boolean isHabitabel = false;

    City[] cities;
    Moon[] moons;

    public Planet(String name, UniverseObject parent, Map map, int seed) {
        super(name, OBJECT_PLANET, parent, map, seed);

        setPlanetType();
    }

    public static String getPlanetTypeName(int myPlanetType) {
        switch (myPlanetType) {
            case PLANET_TYPE_GRASS:
                return "Grass planet";
            case PLANET_TYPE_WATER:
                return "Water planet";
            case PLANET_TYPE_DESSERT:
                return "Dessert planet";
            case PLANET_TYPE_MAGMA:
                return "Magma planet";
            case PLANET_TYPE_DSCHUNGLE:
                return "Dschungle planet";
            case PLANET_TYPE_STONE:
                return "Stone planet";
            case PLANET_TYPE_SNOW:
                return "Snow planet";
            case PLANET_TYPE_ICE:
                return "Ice planet";
            case PLANET_TYPE_LOST:
                return "Planet lost";
            default:
                return "no planet type detected";
        }
    }


    public void setPlanetType() {
        setRandomTemperature();

        if (temperature < MAX_TEMPERATURE_TO_SURVIVE && temperature > MIN_TEMPERATURE_TO_SURVIVE) {


            if (temperature <= 3) {
                planetType = PLANET_TYPE_SNOW;
            } else if (temperature < 35 && temperature > 3) {
                setRandomPlanetType();
            } else if (temperature > 45 && temperature < MAX_TEMPERATURE_TO_SURVIVE) {
                planetType = PLANET_TYPE_DESSERT;
            }
            isHabitabel = true;

            createCities();

        } else {

            if (temperature < 500 && temperature > 0) {
                planetType = PLANET_TYPE_STONE;
            } else if (temperature >= 500) {
                planetType = PLANET_TYPE_MAGMA;
            } else if (temperature < MIN_TEMPERATURE_TO_SURVIVE) {
                planetType = PLANET_TYPE_ICE;
            } else {
                planetType = PLANET_TYPE_LOST;
            }
            cities = null;
        }
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

                Moon m = new Moon("", this, getMap(), random.nextInt());
                moons[i] = m;

                name = moons[i].getName();
            }
            return moons;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * If the parent Planet is habitable, maybe create cities on it.
     *
     * @return
     */
    protected void createCities() {

        try {
            cities = new City[RandomHandler.createIntegerFromRange(1, MAX_CITIES_ON_PLANET, random)];

            for (int i = 0; i < cities.length; i++) {

                //TODO Hier könnte es sein, dass der Maximalwert der random zahl abgecuttet wird da der Integer wert eigentlich zu klein ist
                City c = new City("", this, getMap(), random.nextInt());

                cities[i] = c;

                cities[i].getName();

                population += cities[i].getPopulation();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isHabitabel() {
        return isHabitabel;
    }
}

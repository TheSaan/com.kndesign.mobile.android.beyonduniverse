package com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects;

import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;

import java.util.Random;

/**
 * Created by mknoe on 28.04.2015.
 */
public class SolarSystem extends UniverseObject {

    Planet[] planets;
    Star[] stars;

    private int twinStarProbability = 100000000;
    private int tspHalf = twinStarProbability / 2;

    public SolarSystem(String name, Galaxy parent, Map map, int seed) {
        super(name, OBJECT_SOLARSYSTEM, parent, map, seed);
        setScreenDimension(map.getScreenWidth(), map.getScreenHeight());
    }

    public Planet[] createPlanets() {
        final int a = getRandomPlanetAmount();

        try {

            planets = new Planet[a];

            for (int i = 0; i < planets.length; i++) {

                Planet p = new Planet("", this, getMap(), random.nextInt());
                planets[i] = p;

                name = planets[i].getName();
            }
            return planets;

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
    private Star[] createStars() {

        try {
            int amount;
            //creates a random number to choose if it will be a twin star system
            //with this settings the probability is quiet small
            int prob = RandomHandler.createIntegerFromRange(0, twinStarProbability, random);

            if (prob > tspHalf - 10000 && prob < tspHalf + 10000) {
                amount = 2;
            } else {
                amount = 1;
            }

            stars = new Star[amount];

            for (int i = 0; i < amount; i++) {

                //place the two stars beside each other
                if (amount > 1) {
                    //change the vec value a bit
                    if (i == 0) {
                        stars[i].setX(getX() * 0.999f);
                    } else if (i == 1) {
                        stars[i].setX(getX() * 1.001f);
                    }
                }

                Star s = new Star("", this, getMap(), random.nextInt());
                stars[i] = s;

                name = stars[i].getName();

            }

            return stars;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

package com.thesaan.beyonduniverse.gamecontent.world;

import android.app.Dialog;
import android.content.Context;
import android.os.SystemClock;
import android.widget.TextView;

import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.gameengine.android.opengl.shapes.Shape;

import java.util.Random;

/**
 * The World class represents the container of all objects (but recursive)
 * so it just contains the galaxies and they continue than.
 * <p/>
 * There are some constants in {@link UniverseObjectProperties} which
 * define the final size or amounts for all included objects.
 * <p/>
 * Also included is the call
 * {@link com.thesaan.gameengine.android.database.DatabaseConnection} if the implementation
 * is done.
 */
public class World implements UniverseObjectProperties {

    Context context;


    //the value in the log text
    float mass, radius, degrees, volume, population;
    int planetType;
    String name;

    private final int randomSeed = 1561488911;

    Random r;
    public Galaxy[] galaxies;
    /**
     * The universe object creates all objects like galaxies, solar systems, planets (+ thier cities), moons and stars.
     * <p/>
     * This class is only for the initializing step before running the game on the market. All created objects will be saved
     * in a server stored database.
     *
     * @param context
     */
    public World(Context context) {

        this.context = context;
        r = new Random(randomSeed);

        try {
            Thread T = new Thread(new Runnable() {
                @Override
                public void run() {

                }
            });

            T.start();

        } catch (Exception e) {
            System.err.println("Create Galaxy ERROR: ");
            e.printStackTrace();
        }

    }


    /**
     * Creates a certain amount of {@link Galaxy}s. Also proofs on twins
     *
     */
    public Galaxy[] openGalaxies(int amount) {




        Galaxy g;

        galaxies = new Galaxy[amount];


        for (int i = 0; i < amount; i++) {

            g = new Galaxy("",OBJECT_GALAXY, r.nextInt());

//            System.out.println("Loading object "+(i+1)+"/"+amount+" called "+g.getName());

            //check for twins
            for(Galaxy g_test:galaxies){
                /*
                 If one of the already created galaxies has the same name
                 as the current one. re-run this loop step. Than another random
                 name will be set.
                 */
                if(g_test != null && i == 1) {
                    if (g_test.getName() == g.getName()) {
                        i--;
                        continue;
                    }
                }
            }
            galaxies[i] = g;

        }
        return galaxies;
    }

    public static Map getMap(UniverseObject[] objects){
        float[] coordinates = new float[objects.length* Shape.COORDS_PER_VERTEX];
        int step = 0;
        for(int i = 0; i < objects.length; i++){
            if(i == 0) {
                coordinates[0] = objects[0].getPosition().getXf();
                coordinates[1] = objects[0].getPosition().getYf();
                coordinates[2] = objects[0].getPosition().getZf();
            }else{
                coordinates[step] = objects[i].getPosition().getXf();
                coordinates[step+1] = objects[i].getPosition().getYf();
                coordinates[step+2] = objects[i].getPosition().getZf();
            }
            step += 3;
        }

        Map map = new Map();
        map.setObjectCoordinates(coordinates);

        return map;
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


    /**
     * Set the status of for the loading progress bar
     * //TODO not implemented
     *
     * @param progress
     */
    public void setLoadingProgressBar(int progress) {

    }


}

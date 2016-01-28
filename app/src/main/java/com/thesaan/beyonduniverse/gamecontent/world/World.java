package com.thesaan.beyonduniverse.gamecontent.world;

import android.content.Context;
import android.os.AsyncTask;
import android.os.*;

import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.gameengine.android.handler.TestHandler;

/**
 * The World class creates the {@link Galaxy} list or returns it.
 * Also defines the first random instance with the initializing
 * random seed
 */
public class World extends UniverseObject implements UniverseObjectProperties {

    Context context;


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
        super(OBJECT_UNIVERSE, randomSeed);
        this.context = context;
    }

    /**
     * Creates a certain amount of {@link Galaxy}s. Also proofs on twins
     */
    public Galaxy[] createGalaxies(int amount, Map map) {


        TestHandler test = new TestHandler();
//        Object[] params = {amount, map, this};
//        MyAsyncClass ma = new MyAsyncClass();
//        ma.execute(params);
//
//        ma.notify();


        Galaxy g;
        test.startTimer();

        galaxies = new Galaxy[amount];
        for (int i = 0; i < amount; i++) {
            int seed = random.nextInt();

            g = new Galaxy("", this, map, seed);

            System.out.println("Loading object " + (i + 1) + "/" + amount + " called " + g.getName());

            //check for twins
            for (int k = 0; k < i; k++) {
                /*
                 If one of the already created galaxies has the same name
                 as the current one. re-run this loop step. Than another random
                 name will be set.
                 */
                if (galaxies[k] != null) {
                    if (galaxies[k].getName() == g.getName()) {
                        i--;
                        continue;
                    }
                }
            }
            galaxies[i] = g;
            test.printLoopProgress("create galaxies", i, amount);
        }

        test.stopTimer("Finished creating Galaxies");
        return galaxies;
    }
//    TODO only use this AsyncTask when a loading sequence at the start of the game is implemented
//    class MyAsyncClass extends AsyncTask<Object,Void,Galaxy[]> {
//        // All the stuff, core work in doInBackground
//
//
//        @Override
//        protected void onPostExecute(Galaxy... objects) {
//            galaxies = objects;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//
//        }
//
//        @Override
//        protected Galaxy[] doInBackground(Object... params) {
//
//            int a = (int)params[0];
//            Map m = (Map)params[1];
//            World w = (World)params[2];
//
//            System.out.println("a: "+a+"\n m: "+m+"\n w: "+w);
//            TestHandler test = new TestHandler();
//
//            System.out.println("doInBackground started...");
//
//            Galaxy g;
//            test.startTimer();
//
//            galaxies = new Galaxy[a];
//            for (int i = 0; i < a; i++) {
//                int seed = random.nextInt();
//
//                g = new Galaxy("", w, m, seed);
//
//                    System.out.println("Loading object "+(i+1)+"/"+a+" called "+g.getName());
//
//                //check for twins
//                for (int k = 0; k < i; k++) {
//                        /*
//                         If one of the already created galaxies has the same name
//                         as the current one. re-run this loop step. Than another random
//                         name will be set.
//                         */
//                    if (galaxies[k] != null) {
//                        if (galaxies[k].getName() == g.getName()) {
//                            i--;
//                            continue;
//                        }
//                    }
//                    galaxies[i] = g;
//                }
//                test.printLoopProgress("create galaxies",i,a);
//            }
//
//            test.stopTimer("Finished creating Galaxies");
//            return galaxies;
//        }
//    }
}




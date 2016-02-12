package com.thesaan.beyonduniverse.gamecontent.world;

import android.app.Activity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.*;
import android.util.DisplayMetrics;

import com.thesaan.beyonduniverse.MyGLSurfaceView;
import com.thesaan.beyonduniverse.gamecontent.Spaceship;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.gameengine.android.handler.TestHandler;
import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;
import com.threed.jpct.SimpleVector;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

/**
 * The World class creates the {@link Galaxy} list or returns it.
 * Also defines the first random instance with the initializing
 * random seed
 */
public class Game extends UniverseObject implements UniverseObjectProperties {

    public final static int NEW_GAME = 1;
    public final static int LOAD_GAME = 2;

    Context context;

    /**
     * List of pre defined {@link Level}s
     */
    Level[] levels;

    /**
     * Current {@link Level}
     */
    Level level;

    //the current objects of the map
    UniverseObject[] objects;

    BUObject3D[] objectModels;

    Map map;

    //screen center position
    SimpleVector pivot;

    public Galaxy[] galaxies;


    private MyGLSurfaceView mGLView;

    private MyGLRenderer renderer = null;

    /**
     * <p>
     * The universe object creates all objects like galaxies, solar systems, planets (+ thier cities), moons and stars.
     * <p/>
     * And handles level changes.
     * <p/>
     * <p/>
     * <p>
     * This class is only for the initializing step before running the game on the market. All created objects will be saved
     * in a server stored database.
     * </p>
     *
     * @param context
     */
    public Game(Context context) {
        super(OBJECT_UNIVERSE, randomSeed);
        this.context = context;
    }

    public void initialize() {

        map = new Map(
                context,
                getMapWidth(),
                getMapHeight()
        );


        /*
        If no level is loaded, create a default (for testing) level
         */
        if (level == null) {
            level = new Level(context);
            //TODO Just loading a default level for testing
            //TODO Level.load() muss im Hintergrund ausgeführt werden wärend der Spieler zu einer neuen Position fliegt
            level.load("default_level");
        }
        if (map != null) {
            map.load();
            if (map.isLoaded()) {
                int length = map.getNumberOfObjects();
                objects = map.getObjects();

                //center of the screen
                pivot = new SimpleVector(
                        map.getScreenWidth() / 2,
                        map.getScreenHeight() / 2,
                        1000f);

                //set the positions of the cubes to the galaxies
                for (int i = 0; i < length; i++) {
                    objects[i].resetModel();
                    objects[i].getObject().setRotationPivot(pivot);
                }

                objectModels = map.getObjectModels();
            }
        }
    }

    public MyGLSurfaceView start(Activity activity) {
        mGLView = new MyGLSurfaceView(context);


        mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                // back to Pixelflinger on some device (read: Samsung I7500)
                int[] attributes = new int[]{EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE};
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        initialize();

        renderer = new MyGLRenderer(context, this);

        mGLView.setRendererLink(renderer);
        mGLView.setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);

        return mGLView;
    }

    /**
     * Get the data of the players save games
     * TODO
     */
    public void getSavedData() {

    }

    /**
     * Displaying Development Team Data
     */
    public void diplayCredits() {

    }

    /**
     * Close Game
     */
    public void exit(){

    }

    /**
     *
     * @param levelname
     * @param loadingmode
     * Choose wether a new game will start or a existing saved game will be loaded
     */
    public Level load(String levelname,int loadingmode){
        if(loadingmode != NEW_GAME || loadingmode != LOAD_GAME){
            System.err.println("Wrong load parameter for loadingmode("+loadingmode+")");
            //TODO let return to main menue if happens
            return null;
        }
        if((levelname == "" || levelname == null)&& loadingmode == LOAD_GAME){
            System.err.println("Cannot load Level without a name.");
            //TODO let return to main menue if happens
            return null;
        }

        //Everything went fine
        Level l = new Level(context);
        l.load(levelname);

        return l;

    }
    /**
     * Save current data and settings
     */
    public void save(){


    }

    /**
     * Loads the User Interface
     */
    public void loadUI(){

    }
    /**
     * @return
     */
    @Override
    public Map getMap() {
        return map;
    }

    /**
     * @return
     */
    public SimpleVector getPivot() {
        return pivot;
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

    //    TODO Run on load screen
    class MapLoader extends AsyncTask<Object, Void, Galaxy[]> {
        // All the stuff, core work in doInBackground


        @Override
        protected void onPostExecute(Galaxy... objects) {
            galaxies = objects;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Galaxy[] doInBackground(Object... params) {

            int a = (int) params[0];
            Map m = (Map) params[1];
            Game w = (Game) params[2];
//
            Galaxy g;

            galaxies = new Galaxy[a];

            for (int i = 0; i < a; i++) {
                int seed = random.nextInt();

                g = new Galaxy("", w, m, seed);
//
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
                    galaxies[i] = g;
                }
            }

            return galaxies;
        }
    }
}




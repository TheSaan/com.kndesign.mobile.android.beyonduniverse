package com.thesaan.beyonduniverse.gamecontent.world;

import android.content.Context;
import android.os.AsyncTask;

import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.threed.jpct.Loader;
import com.threed.jpct.Object3D;
import com.threed.jpct.World;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Vector;

/**
 * Contains all Objects in the current area which are
 * loaded with their models.
 */
public class Level extends World {

    Context context;

    /**
     * Non Player Characters ordered
     */
    Vector<BUObject3D> npcs;
    /**
     * Static Objects like Buildings, First Person View Ship Interior, ... ordered
     */
    Vector<BUObject3D> staticObjects;
    /**
     * All Players
     */
    Vector<BUObject3D> players;
    /**
     * All Objects which are non static but not interaktive
     */
    Vector<BUObject3D> nonStaticObjects;

    /**
     * A common name for this level
     */
    private String levelName;

    /**
     * A subclass of {@link com.threed.jpct.World} which sort the different
     * kinds of objects instead of only listing them at once.
     * <p/>
     * Levels also can be used to create pre defined levels
     *
     * @param context
     */
    public Level(Context context) {
        super();
        this.context = context;
    }

    /**
     * Loads the static content of a level like  terrain or
     * non interaktive models
     *
     * @param filename
     */
    public void load(String filename) {
        StaticMapLoader loader = new StaticMapLoader();
        loader.execute(filename);
    }

    /**
     * Just runs small levels for testing certain stuff
     * @param testlevel
     * @param testtype
     */
    public Level(String testlevel,int testtype){

    }
    /**
     * @param npc
     */
    public void addNPC(BUObject3D npc) {
        npcs.add(npc);
    }

    /**
     * @param player
     */
    public void addPlayer(BUObject3D player) {
        npcs.add(player);
    }

    /**
     * @param obj
     */
    public void addStaticObject(BUObject3D obj) {
        npcs.add(obj);
    }

    /**
     * @param obj
     */
    public void addNonStaticObject(BUObject3D obj) {
        npcs.add(obj);
    }

    /**
     * @param index
     */
    public void removeNPC(int index) {
        npcs.remove(index);
    }

    /**
     * @param index
     */
    public void removePlayer(int index) {
        players.remove(index);
    }

    /**
     * @param index
     */
    public void removeNonStaticObject(int index) {
        nonStaticObjects.remove(index);
    }

    /**
     * @param index
     */
    public void removeStaticObjects(int index) {
        staticObjects.remove(index);
    }

    /**
     * @param levelName
     */
    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    /**
     * Runs test data
     */
    public void testLoad(){

    }
    /**
     * Loads the level (all static objects)
     */
    class StaticMapLoader extends AsyncTask<Object, Void, Vector<BUObject3D>> {
        // All the stuff, core work in doInBackground


        @Override
        protected void onPostExecute(Vector<BUObject3D> objects) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Vector<BUObject3D> doInBackground(Object... params) {
            try {

                System.err.println("Level File not found");

                String filename = (String) params[0];

                String mtl = filename + ".mtl";//material file
                String obj = filename + ".obj";//object file

                InputStream isObj = new FileInputStream("levels/" + filename + "/" + obj);
                InputStream isMtl;

                try {
                    isMtl = new FileInputStream("levels/" + filename + "/" + mtl);
                } catch (FileNotFoundException f) {
                    //for Loader the mtl file can be null
                    isMtl = null;
                }

                BUObject3D[] objects = (BUObject3D[]) Loader.loadOBJ(isObj, isMtl, 1);

                //add all static objects
                for (int i = 0; i < objects.length; i++) {
                    staticObjects.add(i, objects[i]);
                }

                setLevelName(filename);

                return staticObjects;
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                return null;
            }

        }
    }
}

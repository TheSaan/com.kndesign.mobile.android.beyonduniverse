package com.thesaan.beyonduniverse.gamecontent.world;

import android.content.Context;

import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.beyonduniverse.math.ObjectVector;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.RandomHandler;
import com.thesaan.gameengine.android.handler.TestHandler;
import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.thesaan.gameengine.android.opengl.shapes.Shape;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;

/**
 * Controls all Universe Object Handles
 */
public class Map {

    /**
     * standard minimap
     */
    public static final int MAP_MODE_MINIMAP = 1;
    /**
     * When flying near at some objects, show a radar based
     * minimap
     */
    public static final int MAP_MODE_MINIMAP_LOCAL = 2;
    /**
     * Show the map as fullscreen to select objects and get
     * further information about objects
     */
    public static final int MAP_MODE_FULLSCREEN = 2;

    public final static int LOADED = 1;
    public final static int NOT_LOADED = 2;

    /**
     * Represents the current player position on map
     */
    MathHandler.Vector mapCursor;

    /**
     * TODO Needs to be tested
     * <p/>
     * Translates the map and cursor slower times this factor
     */
    private final float MAP_TRANSLATION_FACTOR = 0.00001f;

    /**
     * A coordinate list of cubes which
     * define the 64 space quadrants
     * in the virtual universe
     */
    private float[][] quadrantMap;
    private boolean quadrantsLoaded = false;
    /**
     * openGL Vertex array
     */
    float[] objectCoordinates;

    //Vertex object which contains all coordinates of all objects
    public Vertex coordList;

    //the way the map is shown
    private int mapMode;

    //to save the translation after draw
    ObjectVector[] positionList;

    //contains the current objects
    UniverseObject[] mObjects;

    //all models for the current object list
    BUObject3D[] objectModels;

    //Screen dimension
    int screenWidth, screenHeight;

    int[] habitalSeeds;

    int numberOfObjects;

    private boolean loaded = false;

    Context context;

    /**
     * @param context
     * @param width
     * @param height
     */
    public Map(Context context, int width, int height) {
        screenHeight = height;
        screenWidth = width;

        setContext(context);

        loadQuadrantMap();

        int gMin = 100;
        int gMax = 2000;

    }

    /**
     * Map with created {@link UniverseObject}s
     *
     * @param objects
     */
    public Map(UniverseObject[] objects) {

        mObjects = objects;
        init();
    }

    public void loadQuadrantMap() {
        float mw = screenWidth;//x value
        float mh = screenHeight;// y value
        float md = mw; //z value

        //cube steps
        float wS = mw / 4;
        float hS = mh / 4;
        float dS = md / 4;

        //64x8 points = 512
        float[][] cubes = new float[64][24];
        /*
        Creating an array which contains all
        cube edge data
         */

        // cube counter
        int cubeCount = 0;


        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                for (int l = 0; l < 4; l++) {

                    float lx = wS * l;//left x
                    float rx = wS * (l + 1);//right x
                    float ty = hS * k;//top y
                    float by = hS * (k + 1);//bottom y
                    float fz = dS * i;//front z
                    float rz = dS * (i + 1);//rear z

                    cubes[cubeCount][0] = lx;
                    cubes[cubeCount][1] = ty;
                    cubes[cubeCount][2] = fz;
                    cubes[cubeCount][3] = rx;
                    cubes[cubeCount][4] = ty;
                    cubes[cubeCount][5] = fz;
                    cubes[cubeCount][6] = rx;
                    cubes[cubeCount][7] = by;
                    cubes[cubeCount][8] = fz;
                    cubes[cubeCount][9] = lx;
                    cubes[cubeCount][10] = by;
                    cubes[cubeCount][11] = fz;

                    cubes[cubeCount][12] = lx;
                    cubes[cubeCount][13] = ty;
                    cubes[cubeCount][14] = rz;
                    cubes[cubeCount][15] = rx;
                    cubes[cubeCount][16] = ty;
                    cubes[cubeCount][17] = rz;
                    cubes[cubeCount][18] = rx;
                    cubes[cubeCount][19] = by;
                    cubes[cubeCount][20] = rz;
                    cubes[cubeCount][21] = lx;
                    cubes[cubeCount][22] = by;
                    cubes[cubeCount][23] = rz;

                    //move on in array
                    cubeCount++;
                }
            }
        }

        quadrantMap = cubes;

        quadrantsLoaded = true;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void load() {


        if (context != null) {
            World world = new World(context);

            //            mObjects = ((World) world).createGalaxies(
//                    RandomHandler.createIntegerFromRange(gMin,gMax,world.random)
//            );
            try {
                mObjects = world.createGalaxies(50, this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (mObjects != null) {
                int length = mObjects.length;
                if (length > 0) {
                    numberOfObjects = length;
                    int loading_status = init();
                    if (loading_status == LOADED) {
                        loaded = true;
                    } else {
                        loaded = false;
                    }
                }
            }
        } else {
            System.err.println("Could not load map. Context is null");
        }
    }

    /**
     * Sets the Quadrant to each loaded object
     */
    public void setQuadrant() {
        if (loaded && quadrantsLoaded) {
            for (UniverseObject obj : mObjects) {
                float x = obj.myPosition.getXf();
                float y = obj.myPosition.getYf();
                float z = obj.myPosition.getZf();

                for (int i = 0; i < quadrantMap.length; i++) {
                    if (
                            x > quadrantMap[i][0] && x < quadrantMap[i][3] &&
                                    y > quadrantMap[i][1] && y < quadrantMap[i][4] &&
                                    z > quadrantMap[i][2] && z < quadrantMap[i][5]
                            ) {
                        obj.setVirtualPosition(i + 1);
                        break;
                    }
                }
            }

        } else {
            System.err.println("No objects loaded to set quadrants and virtual position!");
        }

    }

    /**
     * Creates an single float array with all object coordinates
     */
    public int init() {
        if (mObjects != null) {
            objectCoordinates = new float[mObjects.length * Shape.COORDS_PER_VERTEX];
            int step = 0;
            for (int i = 0; i < mObjects.length; i++) {
                if (i == 0) {
                    objectCoordinates[0] = mObjects[0].getPosition().getXf();
                    objectCoordinates[1] = mObjects[0].getPosition().getYf();
                    objectCoordinates[2] = mObjects[0].getPosition().getZf();
                } else {
                    objectCoordinates[step] = mObjects[i].getPosition().getXf();
                    objectCoordinates[step + 1] = mObjects[i].getPosition().getYf();
                    objectCoordinates[step + 2] = mObjects[i].getPosition().getZf();
                }
                step += 3;
            }
        }

        int loaded = makeVertex();

        makeObjects();

        return loaded;
    }

    /**
     * Creates an array of floats to make it
     * useable for the {@link Vertex#draw(float[], int)} method.
     * <p/>
     * <p>It also calculates the Coordinates as percentage,
     * because the GLView uses 0.0 to 1.0 values
     * </p>
     */
    public int makeVertex() {
        if (objectCoordinates != null) {
            coordList = new Vertex(objectCoordinates);
            coordList.setDimension(screenWidth, screenHeight);
            coordList.convertToScreenPercentage();
            return LOADED;

        } else {
            return NOT_LOADED;
        }
    }

    /**
     * Creates {@link MathHandler.Vector} list
     * to keep the translated position.
     */
    public void makeObjects() {
        float[] coords = coordList.getPointCoords();
        int length = coords.length;
        positionList = new ObjectVector[length / 3];

        int c = 0;

        for (int i = 0; i < positionList.length; i++) {
            if (i == 0) {
                positionList[0] = new ObjectVector(coords[0], coords[1], coords[2]);

            } else {
                positionList[i] = new ObjectVector(coords[c], coords[c + 1], coords[c + 2]);
            }


            positionList[i].setArrayListIndex(c);

            c += 3;
        }

        for (MathHandler.Vector element : positionList) {
            element.setScreenCenter(screenWidth, screenHeight);
        }

        //now store the new positions into the coordList array
        for (ObjectVector obj : positionList) {
            int i = obj.getArrayListIndex();
            coords[i] = obj.getXf();
            coords[i + 1] = obj.getYf();
            coords[i + 2] = obj.getZf();
        }
    }

    /**
     * Rotate all objects in once.
     *
     * @param angle
     * @param x
     * @param y
     * @param z
     * @param projCamView
     * @param origin
     */
    public void rotateObjects(float angle, int x, int y, int z, MathHandler.Matrix projCamView, MathHandler.Vector origin) {

        TestHandler test = new TestHandler();

        test.startTimer();

        float[] list = coordList.getPointCoords();

        for (int i = 0; i < positionList.length; i += 3) {
            positionList[i].rotate3D(angle, x, y, z, projCamView, origin);
            list[i] = positionList[i].getXf();
            list[i + 1] = positionList[i].getYf();
            list[i + 2] = positionList[i].getZf();
        }

        coordList.setPointCoords(list);
        test.stopTimer("Rotate objects");
    }

    /**
     * Get the {@link Vertex} with all coordinates of all current objects
     *
     * @return
     */
    public Vertex getCoordList() {
//        coordList.init();
        return coordList;
    }


    /**
     * Only use this method if the coordinates are complete.
     * Otherwise not all objects get translated.
     *
     * @param objectCoordinates
     */
    public void setObjectCoordinates(float[] objectCoordinates) {
        this.objectCoordinates = objectCoordinates;
    }


    /**
     * @param width
     * @param height
     */
    public void setScreenSize(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

    }

    /**
     * @return
     */
    public float[] getObjectCoordinates() {
        return objectCoordinates;
    }

    /**
     * Get a list of single {@link Vertex}es. Only use this
     * if you're sure you need every coordinate as a {@link Vertex}
     *
     * @return
     */
    public Vertex[] getObjectVertexes() {

        float[][] f = new float[objectCoordinates.length / Shape.COORDS_PER_VERTEX][Shape.COORDS_PER_VERTEX];

        Vertex[] vertexes = new Vertex[f.length];


        for (int i = 0; i < vertexes.length; i++) {
            Vertex temp = new Vertex(f[i][0], f[i][1], f[i][2]);
            vertexes[i] = temp;
        }

        return vertexes;
    }

    /**
     * Set the way the map is shown.
     * <p>Modes:</p>
     * <p>{@link #MAP_MODE_FULLSCREEN}</p>
     * <p>{@link #MAP_MODE_MINIMAP}</p>
     *
     * @param mode
     */
    public void setMapMode(int mode) {
        this.mapMode = mode;
    }

    /**
     * @return
     */
    public int getMapMode() {
        return mapMode;
    }

    public UniverseObject[] getObjects() {
        return mObjects;
    }

    //    /**
//     * Get a list of random seeds which create a planet with habital conditions
//     */
//    public int[] getHabitalSeeds() {

//        ArrayList<Integer> list = new ArrayList<>(10000);
//
//        SolarSystem ss;
//
//
//        //list index
//        int k = 0;
//        ss = new SolarSystem("", 0.0f, new Galaxy("",0,this), 0.0f, 0);
//
//        for (int i = 0; i < Integer.MAX_VALUE; i++) {
//
//            ss.createPlanets(ss.getPosition(), i);
//
//            for (Planet p : ss.getPlanets()) {
//                if (p.isHabitabel()) {
//                    System.out.println("Seed "+i+"| Planet Temp.: "+p.getTemperature());
//                    list.add(k++, i);
//                }
//            }
//        }
//        list.trimToSize();
//
//        int[] array = new int[list.size()];
//
//        for (int i = 0; i < array.length; i++) {
//            array[i] = list.get(i);
//        }
//
//        return array;

//    }

    public void setMapCursor(MathHandler.Vector mapCursor) {
        this.mapCursor = mapCursor;
    }


    /**
     * Moves the cursor on map in relation to the player movement
     *
     * @param x
     * @param y
     * @param z
     * @param factor if the player moves faster, increase or opposite
     */
    public void moveOnMap(float x, float y, float z, float factor) {

    }

    public boolean isLoaded() {
        return loaded;
    }

    public int getScreenHeight() {
        return (int) screenHeight;
    }

    public int getScreenWidth() {
        return (int) screenWidth;
    }

    public int setObjects(UniverseObject[] objects) {
        mObjects = objects;

        //get their coordinates
        return init();
    }

    public BUObject3D[] getObjectModels() {

        int length = getObjects().length;

        objectModels = new BUObject3D[length];

        for(int i = 0; i < length; i++){
            if(mObjects[i].getObject() == null){
                mObjects[i].resetModel();
            }
            objectModels[i] = mObjects[i].getObject();
        }

        return objectModels;
    }

    public int getNumberOfObjects() {
        return numberOfObjects;
    }
}

package com.thesaan.gameengine.android.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.widget.Toast;

import com.thesaan.beyonduniverse.MainActivity;
import com.thesaan.beyonduniverse.gamecontent.*;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Planet;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.SolarSystem;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Star;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.beyonduniverse.gamecontent.world.World;
import com.thesaan.gameengine.android.DB_Settings;
import com.thesaan.gameengine.android.Settings;
import com.thesaan.gameengine.android.database.AppDatabase;
import com.thesaan.gameengine.android.drawables.CoordinateSystem3D;
import com.thesaan.gameengine.android.drawables.forms.Cube;
import com.thesaan.gameengine.android.drawables.forms.Form;

import java.io.IOException;

import static com.thesaan.gameengine.android.handler.MathHandler.*;

/**
 * Created by mknoe on 17.04.2015.
 */
public class StarMapSurface extends SurfaceView implements SurfaceHolder.Callback, GestureDetector.OnGestureListener {

    /**
     * Background image for a universe effect
     */
    Bitmap universeImage;
    /**
     * Bitmap array with all possible planet bitmaps, which are currently needed in the
     * solar system.
     */
    Bitmap[] planetBitmaps;

    /**
     * Equals the mathematical Y-Axis.
     */
    public final static int PAN_MODE = Matrix.Z_AXIS;
    /**
     * Equals the mathematical X-Axis. Because in the two dimensional
     * surface only x & Y exists and z (depth) is a simulated value.
     */
    public final static int ROLL_MODE = Matrix.Y_AXIS;
    /**
     * Equals the mathematical Z-Axis. So the calculated X-Axis is
     * the depth of the two dimensional surface.
     */
    public final static int TILT_MODE = Matrix.X_AXIS;

    /**
     * Identifier for the movement action UP
     */
    public final static int DIRECTION_UP = 10;
    /**
     * Identifier for the movement action DOWN
     */
    public final static int DIRECTION_DOWN = 11;
    /**
     * Identifier for the movement action LEFT
     */
    public final static int DIRECTION_LEFT = 12;
    /**
     * Identifier for the movement action RIGHT
     */
    public final static int DIRECTION_RIGHT = 13;

    /**
     * Defines a break between every canvas redraw frame. This is
     * only used for testing purposes.
     */
    long numberOfGalaxies = 10;

    /**
     * The maximum distance for selection when touching close to an object
     */
    public final float MAX_TOUCH_RANGE = 20f;

    /**
     * Checks if the canvas is locked by holder.lockCanvas()
     */
    public boolean isLocked = false;

    /**
     * Proof if the axises are already set
     */
    boolean axisesAvailable = false;
    /**
     * Proof if the axises positions gets calculeted
     * on the upper left corner on the surface to
     * calculate the positions correctly
     */
    boolean axisesOnScreenOrigin = false;

    /**
     * The scale factor to draw all objects. Default = 1.0f
     */
    private float mScaleFactor = 1.0f;
    /**
     * If this is true, the onZoom methods gets called by the objects to draw
     */
    boolean inScaleMode = false;

    /**
     * Turns to true if an object on the map was selected
     */
    protected boolean isMapObjectSelected = false;

    /**
     * Handles the double buffering of the canvas
     */
    int isClearable = 2;
    Activity mActivity;

    /**
     * last touched coordinates
     */
    public float touchedX, touchedY;

    /**
     * The surface dimension
     */
    int width, height;

    /**
     * the zoom position of the src rect for the background
     */
    public int bgndLastLeft, bgndLastRight, bgndLastTop, bgndLastBottom;
    /**
     * The properties text shows the current behavior data
     */
    private String propertiesText;
    /**
     * The ankerpoint of the cube where to calculate the rest of the lines
     */
    float cubeAnkerX, cubeAnkerY;

    /**
     * the selected Galaxy
     */
    UniverseObject selectedObject;

    /**
     * The center of the surface view
     */
    float centerX, centerY;
    /**
     * This sets the angle distance of the translations
     */
    float moveStep = 0.02f;
    /**
     * This sets the amount of steps in a for loop for translating the object.
     * Simply calls e.g. onRotate 20 times
     */
    final int STEPS = 5;
    /**
     * The edges of the surface view
     */
    float top, bottom, left, right;

    /**
     * All four edges of the surface view
     */
    public static float[] surfaceEdges = new float[4];

    /**
     *
     */
    StarMapBuilder builder;

    //represents the currently opened Object
    UniverseObject currParent;

    public static final int STAR_MAP_LAYER_MODE_UNIVERSE = 16;
    public static final int STAR_MAP_LAYER_MODE_GALAXY = 15;
    public static final int STAR_MAP_LAYER_MODE_SOLARSYSTEM = 14;
    public static final int STAR_MAP_LAYER_MODE_PLANET = 13;
    public static final int STAR_MAP_LAYER_MODE_MOON = 12;
    public static final int STAR_MAP_LAYER_MODE_STAR = 11;
    public static final int STAR_MAP_LAYER_MODE_CITY = 10;
    /**
     * The current map view mode. Default is {@link #STAR_MAP_LAYER_MODE_UNIVERSE}
     * <p>Other modes:</p>
     * <p>{@link #STAR_MAP_LAYER_MODE_GALAXY}</p>
     * <p>{@link #STAR_MAP_LAYER_MODE_SOLARSYSTEM}</p>
     * <p>{@link #STAR_MAP_LAYER_MODE_PLANET}</p>
     * <p>{@link #STAR_MAP_LAYER_MODE_MOON}</p>
     * <p>{@link #STAR_MAP_LAYER_MODE_STAR}</p>
     * <p>{@link #STAR_MAP_LAYER_MODE_CITY}</p>
     */
    private int starMapMode = STAR_MAP_LAYER_MODE_UNIVERSE;
    private CoordinateSystem3D.CoordinateAxis[] XYZaxises = new CoordinateSystem3D.CoordinateAxis[3];
    private CoordinateSystem3D coordinateSystem3D;

    /**
     * if the coordinates system is on screen
     */
    boolean isCoordinateSystemDrawn = false;

    /**
     * The action which is running in {@link com.thesaan.gameengine.android.ui.StarMapSurface.GameThread}
     */
    public int runAction;

    private ScaleGestureDetector scaleGestureDetector;

    private VelocityTracker velocityTracker = null;

    protected SurfaceHolder holder;
    private Bitmap bitmap;
    public Canvas canvas;
    //the thread handler
    public Handler surfaceHandler;
    GestureDetector mGestureDetector;

    /*----------------------------------------CONSTRUCTORS-----------------------------------*/
    public StarMapSurface(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
//        if (!isInEditMode()) {

        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());

        mGestureDetector = new GestureDetector(this);
        //init the bitmap for the canvas


        bitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);

        //init the canvas
        canvas = new Canvas(bitmap);

        //setBackgroundColor(Color.WHITE);
        //get the SurfaceHolder
        holder = getHolder();

        holder.addCallback(this);

        surfaceHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                //TODO MessageSystem einstellen {@link https://developer.android.com/training/multiple-threads/communicate-ui.html#Handler}
            }
        };


        //initialize all bitmaps
        surfaceHandler.post(new BitmapLoader());

        //init dimensions
        setCenter(holder);
        setScreenEdges(holder);

//        }
    }

    /*----------------------------------------TESTERS-----------------------------------*/

    /*----------------------------------------EVENTS-----------------------------------*/


    public boolean onTouchEvent(MotionEvent event) {


        int action = MotionEventCompat.getActionMasked(event);
        int index = MotionEventCompat.getActionIndex(event);
        int pointerId = event.getPointerId(index);

        scaleGestureDetector.onTouchEvent(event);
        mGestureDetector.onTouchEvent(event);

        setCenter(holder);
        setScreenEdges(holder);

        switch (action) {

            case MotionEvent.ACTION_MOVE: {


                if (velocityTracker == null) {
                    //retrieve new object to watch
                    velocityTracker = VelocityTracker.obtain();
                    System.out.println("Obtain");
                } else {
                    //reset tracker
                    velocityTracker.clear();
                    System.out.println("Clear");
                }

                setCubeAnkerPivot(event.getX(), event.getY());
                setTouchedPivot(MotionEventCompat.getX(event, index), MotionEventCompat.getY(event, index));
                int mode = 0;
                int direction = 0;

                boolean isObjectSelected = false;

                //check wether the touch position is equal to an current object or not
                UniverseObject[][] obj = getObjectsByStarMapMode();
                if (obj != null) {
                    for (int i = 0; i < obj.length && obj[i] != null; i++) {
                        for (int k = 0; k < obj[i].length && obj[i][k] != null; k++) {
                            isObjectSelected = isTouchPositionOnObject(obj[i][k]);
                            if (isObjectSelected) {
                                Toast.makeText(getContext(), obj[i][k].getName() + " was selected", Toast.LENGTH_SHORT).show();
                                surfaceHandler.post(new GameThread(GameThread.ACTION_TOUCH_OBJECT));
                            }
                        }
                    }
                } else {
                    System.err.println("UniverseObject[][] container is null");
                }

                if (!isObjectSelected) {
                    if (touchedX < centerX && (touchedY < centerY + 100 && touchedY > centerY - 100)) {

                        mode = PAN_MODE;
                        direction = DIRECTION_LEFT;
                    } else if (touchedX > centerX && (touchedY < centerY + 100 && touchedY > centerY - 100)) {

                        mode = PAN_MODE;
                        direction = DIRECTION_RIGHT;
                    } else if (touchedY < centerY && (touchedX < centerX + 100 && touchedX > centerX - 100)) {
                        mode = TILT_MODE;
                        direction = DIRECTION_UP;
                    } else if (touchedY > centerY && (touchedX < centerX + 100 && touchedX > centerX - 100)) {
                        mode = TILT_MODE;
                        direction = DIRECTION_DOWN;
                    } else if (touchedX > (centerX + (centerX / 2)) && touchedY < (centerY - (centerY / 2))) {
                        mode = ROLL_MODE;
                        direction = DIRECTION_RIGHT;
                    } else if (touchedX < (centerX - (centerX / 2)) && touchedY < (centerY - (centerY / 2))) {
                        mode = ROLL_MODE;
                        direction = DIRECTION_LEFT;
                    }

                    onRotateMap(mode, direction);
                }
//                    return false;

            }
            case MotionEvent.ACTION_DOWN: {

                if (velocityTracker == null) {
                    //retrieve new object to watch
                    velocityTracker = VelocityTracker.obtain();
                    System.out.println("Obtain");
                } else {
                    //reset tracker
                    velocityTracker.clear();
                    System.out.println("Clear");
                }

                setCubeAnkerPivot(event.getX(), event.getY());
                setTouchedPivot(MotionEventCompat.getX(event, index), MotionEventCompat.getY(event, index));
                int mode = 0;
                int direction = 0;

                boolean isObjectSelected = false;

                //check wether the touch position is equal to an current object or not
                UniverseObject[][] obj = getObjectsByStarMapMode();
                if (obj != null) {
                    for (int i = 0; i < obj.length && obj[i] != null; i++) {
                        for (int k = 0; k < obj[i].length && obj[i][k] != null; k++) {
                            isObjectSelected = isTouchPositionOnObject(obj[i][k]);
                            if (isObjectSelected) {
                                Toast.makeText(getContext(), obj[i][k].getName() + " was selected", Toast.LENGTH_SHORT).show();
                                surfaceHandler.post(new GameThread(GameThread.ACTION_TOUCH_OBJECT));
                            }
                        }
                    }
                } else {
                    System.err.println("UniverseObject[][] container is null");
                }

                if (!isObjectSelected) {
                    if (touchedX < centerX && (touchedY < centerY + 100 && touchedY > centerY - 100)) {

                        mode = PAN_MODE;
                        direction = DIRECTION_LEFT;
                    } else if (touchedX > centerX && (touchedY < centerY + 100 && touchedY > centerY - 100)) {

                        mode = PAN_MODE;
                        direction = DIRECTION_RIGHT;
                    } else if (touchedY < centerY && (touchedX < centerX + 100 && touchedX > centerX - 100)) {
                        mode = TILT_MODE;
                        direction = DIRECTION_UP;
                    } else if (touchedY > centerY && (touchedX < centerX + 100 && touchedX > centerX - 100)) {
                        mode = TILT_MODE;
                        direction = DIRECTION_DOWN;
                    } else if (touchedX > (centerX + (centerX / 2)) && touchedY < (centerY - (centerY / 2))) {
                        mode = ROLL_MODE;
                        direction = DIRECTION_RIGHT;
                    } else if (touchedX < (centerX - (centerX / 2)) && touchedY < (centerY - (centerY / 2))) {
                        mode = ROLL_MODE;
                        direction = DIRECTION_LEFT;
                    }

                    onRotateMap(mode, direction);
                }
//                    return false;

            }
            case MotionEvent.ACTION_POINTER_DOWN: {

                setTouchedPivot(MotionEventCompat.getX(event, index), MotionEventCompat.getY(event, index));
                inScaleMode = true;
                System.out.println("POINTER DOWN");
                return true;
            }
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:{
                // Return a VelocityTracker object back to be re-used by others.
                velocityTracker.recycle();
//                isClearable = false;
                inScaleMode = false;

                System.out.println("UP");
                return false;
            }
//            case MotionEvent.ACTION_MOVE: {
//                if (inScaleMode) {
//                    scaleGestureDetector.onTouchEvent(event);
//                }
//
//                //VelocityTracker
//                velocityTracker.addMovement(event);
//
//                velocityTracker.computeCurrentVelocity(1000);
//                // Log velocity of pixels per second
//                // Best practice to use VelocityTrackerCompat where possible.
//                System.out.println("X velocity: " +
//                        VelocityTrackerCompat.getXVelocity(velocityTracker,
//                                pointerId));
//                System.out.println("Y velocity: " +
//                            VelocityTrackerCompat.getYVelocity(velocityTracker,
//                                    pointerId));
//
//                System.out.println("MOVE");
//                onZoom();
//                return false;
//            }
        }
        return true;
    }


    /**
     * Returns the called touch action as a string
     *
     * @param action
     * @return
     */
    public static String actionToString(int action) {
        switch (action) {

            case MotionEvent.ACTION_DOWN:
                return "Down";
            case MotionEvent.ACTION_MOVE:
                return "Move";
            case MotionEvent.ACTION_POINTER_DOWN:
                return "Pointer Down";
            case MotionEvent.ACTION_UP:
                return "Up";
            case MotionEvent.ACTION_POINTER_UP:
                return "Pointer Up";
            case MotionEvent.ACTION_OUTSIDE:
                return "Outside";
            case MotionEvent.ACTION_CANCEL:
                return "Cancel";
        }
        return "";
    }

    /*----------------------------------------BOOLERS-----------------------------------*/

    private boolean isTouchPositionOnObject(UniverseObject object) {
        if (object.getX() >= touchedX - MAX_TOUCH_RANGE && object.getX() <= touchedX + MAX_TOUCH_RANGE &&
                object.getY() >= touchedY - MAX_TOUCH_RANGE && object.getY() <= touchedY + MAX_TOUCH_RANGE) {
            return true;
        } else {
            return false;
        }
    }
    /*----------------------------------------GETTERS-----------------------------------*/

    /**
     * @return Use the object which was selected to get its direct children.
     */
    private UniverseObject[][] getObjectsByStarMapMode() {
        UniverseObject[][] object = new UniverseObject[2][];
        switch (getStarMapMode()) {
            case STAR_MAP_LAYER_MODE_UNIVERSE: {
                object[0] = builder.getGalaxies();
                return object;
            }
            case STAR_MAP_LAYER_MODE_GALAXY: {
                object[0] = builder.getSolarSystems((Galaxy) selectedObject);
                return object;
            }
            case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
                object = builder.getSolarSystemChildren((SolarSystem) selectedObject);
                return object;
            }

//            case STAR_MAP_LAYER_MODE_STAR: return builder.getGalaxies(); // not required actually
//            case STAR_MAP_LAYER_MODE_MOON: return builder.getGalaxies(); // not required actually
            case STAR_MAP_LAYER_MODE_PLANET: {
                object = builder.getPlanetChildren((Planet) selectedObject);
                return object;
            }
//            case STAR_MAP_LAYER_MODE_CITY: return builder.getGalaxies(); // not required actually
            default: {
                Toast.makeText(getContext(), "No Star map mode detected.\nShow galaxies...", Toast.LENGTH_SHORT).show();
                object[0] = builder.getGalaxies();
                return object;
            }
        }
    }

    /**
     * Gets the initialized {@link CoordinateSystem3D coordinateSystem3D}
     *
     * @return
     */
    public CoordinateSystem3D getCoordinateSystem3D() {
        return coordinateSystem3D;
    }

    /**
     * Gets the current GameSurface object
     *
     * @return
     */
    private StarMapSurface getMe() {
        return this;
    }

    /**
     * Example: If you've selected a Galaxy and you are in
     * Starmapmode SOLARSYSTEMS than it returns the Galaxy
     * you've selected before.
     *
     * @return {@link UniverseObject}
     */
    protected UniverseObject getCurrentParent() {
        return currParent;
    }

    public float getmScaleFactor() {
        return mScaleFactor;
    }

    public String getStarmapModeDescription() {
        switch (getStarMapMode()) {
            case STAR_MAP_LAYER_MODE_UNIVERSE:
                return "\tModus: Universe\n";
            case STAR_MAP_LAYER_MODE_GALAXY:
                return "\tModus: Galaxy\n";
            case STAR_MAP_LAYER_MODE_SOLARSYSTEM:
                return "\tModus: Solarsystem\n";
            case STAR_MAP_LAYER_MODE_STAR:
                return "\tModus: Star\n";
            case STAR_MAP_LAYER_MODE_PLANET:
                return "\tModus: Planet\n";
            case STAR_MAP_LAYER_MODE_MOON:
                return "\tModus: Moon\n";
            case STAR_MAP_LAYER_MODE_CITY:
                return "\tModus: City\n";
            default:
                return "\tModus: No Starmap mode detected!\n";
        }
    }

    public StarMapBuilder getStarmapBuilder(){
        return builder;
    }
    /**
     * Gets the current map view mode {@link #starMapMode}
     *
     * @return
     */
    protected int getStarMapMode() {
        return starMapMode;
    }

    /**
     * Gets the type name out of the unique random name of the object.
     * E.g. "G.OIADFF.646652", it takes out the "G" and returns "Galaxy".
     *
     * @param objectName
     * @return
     */
    public String getObjectTypeDescription(String objectName) {
        String[] parts = objectName.split("\\.");
        if (parts[0] == "SS") return "Solar system";
        else if (parts[0] == "G") return "Galaxy";
        else if (parts[0] == "P") return "Planet";
        else if (parts[0] == "M") return "Moon";
        else if (parts[0] == "C") return "City";
        else if (parts[0] == "S") return "Star";
        else
            //TODO Named objects as an array loop for detection
            //special names
            if (objectName == "Milchstraße") return "Galaxy";
            else if (objectName == "Andromeda") return "Galaxy";
            else return "Unknown object";
    }
    /*----------------------------------------SETTERS-----------------------------------*/

    /**
     * Initialize the {@link StarMapBuilder builder}
     *
     * @param map
     */
    public void setStarMap(StarMapBuilder map) {
        builder = map;
    }

    /**
     * Set {@link #centerX} & {@link #centerY}
     *
     * @param holder
     */
    private void setCenter(SurfaceHolder holder) {
        centerX = holder.getSurfaceFrame().centerX();
        centerY = holder.getSurfaceFrame().centerY();
    }

    /**
     * Defines the edges (Positions) of the certain screen or surface
     *
     * @param holder
     */
    private void setScreenEdges(SurfaceHolder holder) {
        top = holder.getSurfaceFrame().top + 1;
        bottom = holder.getSurfaceFrame().bottom;
        left = holder.getSurfaceFrame().left + 1;
        right = holder.getSurfaceFrame().right;

    }

    /**
     * Sets the axises
     *
     * @param x
     * @param y
     * @param z
     */
    private void setAxises(CoordinateSystem3D.CoordinateAxis x, CoordinateSystem3D.CoordinateAxis y, CoordinateSystem3D.CoordinateAxis z) {
        XYZaxises[0] = x;
        XYZaxises[1] = y;
        XYZaxises[2] = z;

    }

    /**
     * Initializes a {@link CoordinateSystem3D} before start drawing it
     *
     * @param axises
     */
    private void setCoordinateSystem3D(CoordinateSystem3D.CoordinateAxis[] axises) {
        coordinateSystem3D = new CoordinateSystem3D(axises, canvas, holder);
    }

    public void setAction(int action) {
        runAction = action;
    }

    private void setTouchedPivot(float x, float y) {
        touchedX = x;
        touchedY = y;
    }

    public void sethight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * The properties Text puts the given string to the {@link MainActivity#setOptionsText(String)} method
     *
     * @param str
     */
    private void setPropertiesText(String str) {
        CoordinateSystem3D.CoordinateAxis[] axises = getCoordinateSystem3D().getAxises();

        propertiesText = str;

        if (mActivity != null) {
            if (mActivity instanceof MainActivity) {
                ((MainActivity) mActivity).setOptionsText(propertiesText);
            } else {
                System.err.println("Wrong instance");
            }
        } else {
            System.err.println("Activity not set");

            mActivity = (MainActivity) this.getContext();
            //re-run
            setPropertiesText(str);
        }


    }

    /**
     * Create an Activity object to get the parent activity
     *
     * @param activity
     */
    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    /**
     * Sets the pivot for drawing a wireframe (or surfaced) {@link Cube}.
     *
     * @param x
     * @param y
     */
    private void setCubeAnkerPivot(float x, float y) {
        cubeAnkerY = y;
        cubeAnkerX = x;
    }

    public void setStarMapMode(int mode) {
        starMapMode = mode;
        if (getContext() instanceof MainActivity) {
            ((MainActivity) getContext()).setStarMapModeInfo(getStarmapModeDescription());
        }
    }

    /**
     * Get one step deeper in the starmap mode (until {@link #STAR_MAP_LAYER_MODE_CITY}).
     */
    protected void setStarMapLayerDown() {
        if (getStarMapMode() > STAR_MAP_LAYER_MODE_CITY)
            setStarMapMode(getStarMapMode() - 1);
    }

    /**
     * Get one step higher in the starmap mode (until {@link #STAR_MAP_LAYER_MODE_UNIVERSE}).
     */
    protected void setStarMapLayerUp() {
        if (getStarMapMode() < STAR_MAP_LAYER_MODE_UNIVERSE)
            setStarMapMode(getStarMapMode() + 1);
    }

    /**
     * Goes back to universal starmap to see the galaxies again
     */
    public void goToUniverseMode() {
        surfaceHandler.post(new GameThread(GameThread.ACTION_DRAW_UNIVERSE_MAP, builder.getGalaxies(), 0, -1, -1, -1));
    }




    /*----------------------------------------DRAWING METHODS-----------------------------------*/

    /**
     * Calls {@link #rotate(float, int, int, int)}
     *
     * @param mode
     * @param direction The angle
     * @return
     */
    public boolean onRotateMap(int mode, int direction) {
        try {
            switch (mode) {
                //2 finger
                case PAN_MODE: {
                    for (int i = 0; i < STEPS; i++) {
                        rotate(moveStep, PAN_MODE, direction, Matrix.Z_AXIS);
                    }
                    return true;
                }
                case ROLL_MODE: {
                    for (int i = 0; i < STEPS; i++) {
                        rotate(moveStep, ROLL_MODE, direction, Matrix.Y_AXIS);
                    }
                    return true;
                }
                //3 fingers
                case TILT_MODE: {
                    for (int i = 0; i < STEPS; i++) {
                        rotate(moveStep, TILT_MODE, direction, Matrix.X_AXIS);
                    }

                    return true;
                }
            }

            return true;

        } catch (Exception ex) {
            System.err.println("TouchMotion Error " + ex);
        }
        return true;
    }

    public void onDrawPlanet() {
        surfaceHandler.post(new GameThread(GameThread.ACTION_DRAW_PLANET));
    }

    /**
     * Calls the {@link com.thesaan.gameengine.android.ui.StarMapSurface.GameThread#post(Runnable)} to draw
     * the universe objects with the rotation data
     *
     * @param distance  The actual angle
     * @param mode
     * @param direction
     * @param axis
     */
    protected void rotate(float distance, int mode, int direction, int axis) {
        surfaceHandler.post(new GameThread(GameThread.ACTION_DRAW_UNIVERSE_MAP, builder.getGalaxies(), distance, mode, direction, axis));
    }

    /**
     * Scales the objects view distances up
     *
     * @param factor
     * @throws InterruptedException
     */
    public void scaleUp(float factor) throws InterruptedException {
        mScaleFactor += factor;
        inScaleMode = true;
        Toast.makeText(getContext(), "mScaleFactor: " + mScaleFactor, Toast.LENGTH_SHORT).show();
    }

    /**
     * Scales the objects view distances down
     *
     * @param factor
     * @throws InterruptedException
     */
    public void scaleDown(float factor) throws InterruptedException {
        mScaleFactor -= factor;
        inScaleMode = true;
        Toast.makeText(getContext(), "mScaleFactor: " + mScaleFactor, Toast.LENGTH_SHORT).show();
    }


//    protected void drawDot() {
//        surfaceHandler.post(new DotDrawer());
//    }

    /**
     * creates a
     *
     * @param width
     * @param height
     * @param depth
     */
    public void drawWireframeCube(float width, float height, float depth) {

        if (canvas != null) {
            Vector vec = new Vector(cubeAnkerX, cubeAnkerY, 50);
            if (vec != null)
                surfaceHandler.post(new GameThread(GameThread.ACTION_DRAW_CUBE, width, height, depth, vec));
            else
                System.err.println("Vector for cube pivot is null");
        }
    }

    /**
     * Calls {@link com.thesaan.gameengine.android.ui.StarMapSurface.GameThread} to animate the player (or spaceship,...) image.
     */
    public void animatePlayer() {
        surfaceHandler.post(new GameThread(GameThread.ACTION_DRAW_PLAYER_ANIMATION, cubeAnkerX, cubeAnkerY, touchedX, touchedY, 10));
    }


    /*----------------------------------------HANDLERS-----------------------------------*/
    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {


        surfaceHandler.post(new GameThread(GameThread.ACTION_TOUCH_OBJECT, e.getX(), e.getY()));
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        //check the position of the touched object an mark it

        //test
        //onDrawPlanet();
    }

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        try {
            System.out.println("onFling()");
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                return false;
            }
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println("LEFT");

                System.out.println("Velocity: " + velocityX);
                moveStep = 0.1f;
                int push = (int) (25 * (-velocityX / 1000));
                for (int i = 0; i < push; i++) {
                    rotate(moveStep, PAN_MODE, DIRECTION_LEFT, Matrix.Y_AXIS);
                    if (i > push * 0.3)
                        moveStep -= (moveStep / i);

                }
                //set back to original
                moveStep = 0.02f;

            }
            // left to right swipe
            if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                System.out.println("RIGHT");
                System.out.println("Velocity: " + velocityX);
                moveStep = 0.05f;
                for (int i = 0; i < SWIPE_MIN_DISTANCE; i++) {
                    rotate(moveStep, PAN_MODE, DIRECTION_RIGHT, Matrix.Y_AXIS);

                    moveStep -= (moveStep / i);

                }
                //set back to original
                moveStep = 0.02f;
            }
        } catch (Exception e) {

        }
        return false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        isClearable = 2;
//        while (isClearable > 0) {
//            canvas.drawColor(Color.BLACK);
//            isClearable--;
//        }
//
////        canvas.drawColor(Color.BLACK);
//        holder.unlockCanvasAndPost(canvas);
    }

    /**
     * Zooms into starmap field at the center of the zoom gesture
     * TODO not implemented yet
     */
    public void onZoom() {

    }
    /*----------------------------------------RUNNABLES-----------------------------------*/

    /**
     * Handles all drawing processes for the star map
     */
    public class GameThread extends Thread implements Runnable {

        /**
         * These constants define  which action is going to get started
         */
        public final static int ACTION_DRAW_3D_SYSTEM = 0;
        public final static int ACTION_DRAW_DOT = 1;
        public final static int ACTION_DRAW_UNIVERSE_MAP = 2;
        public final static int ACTION_DRAW_LINEAL = 3;
        public final static int ACTION_DRAW_CUBE = 4;
        public final static int ACTION_DRAW_PLAYER_ANIMATION = 5;
        public final static int ACTION_SCALE = 6;
        public final static int ACTION_TOUCH_OBJECT = 7;
        public final static int ACTION_DRAW_PLANET = 8;

        private final int MAX_PLANET_SINGLE_ANIM_IMAGES = 28;

        Cube cube;
        Paint p;
        Canvas myCanvas;
        CoordinateSystem3D.CoordinateAxis.Coordinate zS, zE, yS, yE, xS, xE;
        CoordinateSystem3D.CoordinateAxis x, y, z;
        private int mAction;
        //contains the origin objects in the game universe
        Galaxy[] galaxies;
        //contains either galaxies, or solar systems
        UniverseObject[] currentObjects;
        //contains children of solar systems (Planets, stars) or of planets (cities,moons)
        UniverseObject[][] multipleObjects;
        private float distance;

        private float selectX, selectY;

        //initialize with universe size
        float virtualScaleFactor = 1.0f;

        //current position
        float myX, myY;

        //end position
        float endX, endY;

        //velocity
        int pace;
        private int axis, direction, mode;

        /**
         * For player animation.
         *
         * @param action
         * @param currX
         * @param currY
         * @param moveToX
         * @param moveToY
         * @param pace
         */
        public GameThread(int action, float currX, float currY, float moveToX, float moveToY, int pace) {
            mAction = action;
        }

        /**
         * For creating and animating the UniverseMap
         *
         * @param action
         * @param galaxies
         * @param swipeDistance
         * @param mode
         * @param direction
         * @param axis
         */
        public GameThread(int action, Galaxy[] galaxies, float swipeDistance, int mode, int direction, int axis) {
            mAction = action;
            this.galaxies = galaxies;
            this.direction = direction;
            this.mode = mode;
            this.axis = axis;
            p = new Paint();
            distance = swipeDistance;
        }


        /**
         * For creating the coordinate system
         *
         * @param action
         * @param swipeDistance
         * @param mode
         * @param direction
         * @param axis
         */
        public GameThread(int action, float swipeDistance, int mode, int direction, int axis) {
            mAction = action;
            this.direction = direction;
            this.mode = mode;
            this.axis = axis;
            p = new Paint();
            distance = swipeDistance;
        }

        /**
         * Used for any action which doesn't require any other argument
         *
         * @param action
         */
        public GameThread(int action) {
            mAction = action;
            p = new Paint();
        }


        /**
         * For testing like drawing dots.
         *
         * @param action
         */
        public GameThread(int action, float touchX, float touchY) {
            mAction = action;
            p = new Paint();
            selectX = touchX;
            selectY = touchY;

        }

        /**
         * For testing like drawing cubes.
         *
         * @param action
         */
        public GameThread(int action, float width, float height, float depth, Vector vec) {
            mAction = action;
            if (vec != null)
                cube = new Cube(width, height, depth, Color.MAGENTA, vec);
            else
                System.err.println("Pivot vector is null in Runnable CubeDrawer");
        }


        @Override
        public void run() {
            /*
            if an object was touched, the canvas is not allowed
            to clear
             */
            if (!isLocked && mAction != ACTION_TOUCH_OBJECT) {
                myCanvas = holder.lockCanvas();
                myCanvas.drawColor(Color.BLACK);
                isLocked = true;
            }

            switch (mAction) {
                case ACTION_DRAW_3D_SYSTEM: {
                    draw3DCoordinateSystem();
                    break;
                }
                case ACTION_DRAW_DOT: {
                    drawDot();
                    break;
                }
                case ACTION_DRAW_UNIVERSE_MAP: {
                    if (!isMapObjectSelected) {
                        prepareObjectsForDrawing();
//                        draw3DCoordinateSystem();
                    }
                    break;
                }
                case ACTION_DRAW_LINEAL: {
//                    myCanvas = drawLineal(mScaleFactor,myCanvas);
                    break;
                }
                case ACTION_DRAW_CUBE: {
                    drawCube();
                    break;
                }
                case ACTION_DRAW_PLAYER_ANIMATION: {
                    animatePlayer();
                    break;
                }
                case ACTION_SCALE: {
                    scale();
                    break;
                }
                case ACTION_TOUCH_OBJECT: {
                    if (!isMapObjectSelected) {
                        selectObject();
                    } else {
                        openObject();
                    }
                    break;
                }
                case ACTION_DRAW_PLANET: {
                    for (int i = 1; i <= MAX_PLANET_SINGLE_ANIM_IMAGES; i++) {
                        if (!isLocked) {
                            myCanvas = holder.lockCanvas();
                            isLocked = true;
                        }
                        myCanvas.drawColor(Color.BLACK);
                        myCanvas = drawPlanet(myCanvas, i);

                        holder.unlockCanvasAndPost(myCanvas);
                        isLocked = false;
                    }
                    break;
                }
            }
            if (isLocked)
                holder.unlockCanvasAndPost(myCanvas);

            isLocked = false;
        }

        /**
         * NOT IMPLEMENTED YET. Will open a small (View based) monitor with all information about
         * this object.
         *
         * @param g
         * @throws InterruptedException
         */
        public void openObjectDetails(Galaxy g) throws InterruptedException {
            //the origin galaxy
            float x, y, z;
            x = g.getX();
            y = g.getY();
            z = g.getZ();

//            View info = findViewById(R.id.objectInfoView);

//            info.setLeft((int) x);
//            info.setBottom((int) y);
//            info.setLayoutParams(Action);


        }

        private void setSelectedObject(int mode, UniverseObject[] objects) {
            Object[] selected = new UniverseObject[objects.length];
            /*
            * If the user taps on an object, it is possible that
            * there are some other objects behind
            *
            * the following steps check the hight (Z-Value) of the
            * tapped objects and chooses the closest one
            * */
            int selectedIndex = 0;
            for (int i = 0; i < objects.length; i++) {
                //if a galaxy is in the touched range add it to the selected galaxies array
                if (objects[i].getX() >= selectX - MAX_TOUCH_RANGE && objects[i].getX() <= selectX + MAX_TOUCH_RANGE &&
                        objects[i].getY() >= selectY - MAX_TOUCH_RANGE && objects[i].getY() <= selectY + MAX_TOUCH_RANGE) {
                    selected[selectedIndex] = objects[i];
                    selectedIndex++;
                }
            }

            //reduce index by one because when the last selected galaxy gets stored, the index will be raised by one also which
            //is wrong
            if (selectedIndex > 0)
                selectedIndex--;
            //now check the z (depth value) of the selected galaxies and choose this one which has the highest (nearest) value

            //make it global
            selectedObject = getClosestObjectToCamera(objects, selectedIndex);

//                          TODO object details open
//                            try {
//                                openObjectDetails(selectedObject);
//                            }catch (InterruptedException ex){
//                                System.err.println("Thread sleep error in GameSurface.GameMapThread.goTo()");
//                            }
            if (getContext() instanceof MainActivity) {
                ((MainActivity) getContext()).setSelectedObjectInfo(selectedObject.getTypeName() + " " + selectedObject.getName() + " opened.");
            }

            //make the sub objects selectable again
            isMapObjectSelected = true;
        }

        /**
         * When tapping on an object, it gets selected and all data from it gets loaded dynamically.
         * To open, just tap again on it an {@link GameThread#openObject()} gets called.
         */
        private void selectObject() {
            switch (getStarMapMode()) {
                case STAR_MAP_LAYER_MODE_UNIVERSE: {
                    currentObjects = builder.getGalaxies();
                    setSelectedObject(STAR_MAP_LAYER_MODE_GALAXY, currentObjects);
                    break;
                }
                case STAR_MAP_LAYER_MODE_GALAXY: {

                    currentObjects = builder.getSolarSystems((Galaxy) selectedObject);
                    System.err.println("Selected object is type of " + selectedObject.getTypeName());
                    setSelectedObject(STAR_MAP_LAYER_MODE_SOLARSYSTEM, currentObjects);

                    break;
                }
                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {

                    multipleObjects = builder.getSolarSystemChildren((SolarSystem) selectedObject);
                    System.err.println("Selected object is type of " + selectedObject.getTypeName());
                    setSelectedObject(STAR_MAP_LAYER_MODE_GALAXY, currentObjects);
                    break;
                }
                case STAR_MAP_LAYER_MODE_STAR: {
                    if (selectedObject instanceof SolarSystem) {
                        currentObjects = builder.getSolarSystemChildren((SolarSystem) selectedObject)[0]; //index 0 returns the star(s)
                        System.err.println("Selected object is type of " + selectedObject.getTypeName());
                        setSelectedObject(STAR_MAP_LAYER_MODE_GALAXY, currentObjects);
                    } else {
                    }
                    break;
                }
                case STAR_MAP_LAYER_MODE_PLANET: {
                    if (selectedObject instanceof SolarSystem) {
                        currentObjects = builder.getSolarSystemChildren((SolarSystem) selectedObject)[1]; //index 1 returns the planets
                        System.err.println("Selected object is type of " + selectedObject.getTypeName());
                        setSelectedObject(STAR_MAP_LAYER_MODE_GALAXY, currentObjects);
                    } else {
                    }
                    break;
                }
                case STAR_MAP_LAYER_MODE_MOON: {
                    if (selectedObject instanceof Planet) {
                        currentObjects = builder.getPlanetChildren((Planet) selectedObject)[1]; //index 1 returns moons
                        System.err.println("Selected object is type of " + selectedObject.getTypeName());
                        setSelectedObject(STAR_MAP_LAYER_MODE_GALAXY, currentObjects);
                    } else {
                    }
                    break;
                }
                case STAR_MAP_LAYER_MODE_CITY: {
                    if (selectedObject instanceof Planet) {
                        currentObjects = builder.getPlanetChildren((Planet) selectedObject)[0]; //index 0 returns cities
                        System.err.println("Selected object is type of " + selectedObject.getTypeName());
                        setSelectedObject(STAR_MAP_LAYER_MODE_GALAXY, currentObjects);
                    } else {
                    }
                    break;
                }
            }
        }

        /**
         * If the Object was selected by {@link GameThread#selectObject()}, another tap is required to open the object.
         * <p>
         * Opens the {@link UniverseObject (Galaxy, Solar system, Planet, Moon, Star)} on touch. By opening this
         * object, all children like from galaxy -> all solar systems will be drawn now and so on.
         * </p>
         * <p/>
         * <p>If a few objects are stacked in the current camera position, the method compares the z-value
         * and selects the closest (the highest z-value) one</p>
         * <p/>
         */
        private void openObject() {

            if (selectedObject != null) {

                //change map mode by selected object
                if (selectedObject instanceof Galaxy)
                    setStarMapLayerDown();
                else if (selectedObject instanceof SolarSystem) {

                }


                if (!isLocked) {
                    myCanvas = holder.lockCanvas();
                    isLocked = true;
                }
                myCanvas.drawColor(Color.BLACK);

                int mode = getStarMapMode();

                if (mode == STAR_MAP_LAYER_MODE_GALAXY || mode == STAR_MAP_LAYER_MODE_UNIVERSE)
                    prepareObjectsForDrawing();

                if (mode == STAR_MAP_LAYER_MODE_SOLARSYSTEM) {
                    if (selectedObject instanceof SolarSystem) {
                        drawStars(selectedObject.getStars());
                    }
                }

                Toast.makeText(getContext(), "Open " + selectedObject.getTypeName() + " " + selectedObject.getName(), Toast.LENGTH_SHORT).show();
//                    drawLineal(virtualScaleFactor);
                isMapObjectSelected = false;

            } else {
                if (getStarMapMode() != STAR_MAP_LAYER_MODE_UNIVERSE)
                    prepareObjectsForDrawing();
            }

        }

        /**
         * In the first edition of this method the star
         * just gets drawn by canvas circle
         * <p/>
         * in the final step (not yet) the star will be
         * represented as gif animation
         * <p/>
         * just uncomment the lines
         *
         * @param stars the current star object to get the data from
         */
        private void drawStars(Star[] stars) {
            if (!isLocked) {
                myCanvas = holder.lockCanvas();
                isLocked = true;
            }


            myCanvas.drawColor(Color.BLACK);

            float starRadius = centerX * 0.125f;

            try {
                Paint p = new Paint();
                p.setColor(Color.YELLOW);
//                normal canvas circle version
                //////////////////////
                if (stars.length == 1) {
                    myCanvas.drawCircle(centerX, centerY, starRadius, p);
                } else if (stars.length == 2) {

                    //this is the rest of the free space width if the stars are drawn
                    float restWidth = (centerX * 2f) - (starRadius * 2f);

                    float starDistance = restWidth * 0.2f;

                    myCanvas.drawCircle(centerX - (starDistance / 2), centerY, starRadius, p);
                    p.setColor(Color.RED);
                    myCanvas.drawCircle(centerX + (starDistance / 2), centerY, starRadius, p);
                }

                holder.unlockCanvasAndPost(myCanvas);
                //////////////////////
//                normal canvas circle version end

//                gif version
                //////////////////////
//                BitmapHandler bh = new BitmapHandler(getContext());
//                bh.playGif(star.getGifFileName());
                //////////////////////
//                gif version end
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * If an object on the map gets touched to open. Start this animation.
         * NOT IMPLEMENTED YET.
         *
         * @param x
         * @param y
         * @param z
         * @param radius
         */
        private void animateOpener(float x, float y, float z, float radius) {

            AppDatabase db = new AppDatabase(getContext());

            db.getNamesOfGalaxies();

            Vector oldPos = db.getPositionOfObject(selectedObject.getName(), getMapTypeDBTable(getStarMapMode()));

            System.out.println("Old-> X: " + oldPos.getmFloatVec()[0] + " Y: " + oldPos.getmFloatVec()[1] + " Z: " + oldPos.getmFloatVec()[2] + "\n" +
                    "New-> X: " + x + " Y: " + y + " Z: " + z);
            Cube animCube = new Cube(radius, radius, radius / 2, Color.BLUE, new Vector(x * mScaleFactor, y * mScaleFactor, z * mScaleFactor));
            animCube.draw(myCanvas, Cube.WIREFRAME);

            float r = radius;
            boolean borderTouched = false;


        }

        /**
         * Gets the Database table name string
         *
         * @param mapType
         * @return Database table name string
         * @see #getStarMapMode()
         */
        private String getMapTypeDBTable(int mapType) {
            switch (mapType) {
                case STAR_MAP_LAYER_MODE_CITY:
                    return DB_Settings.DATABASE_TABLE_CITIES;
                case STAR_MAP_LAYER_MODE_MOON:
                    return DB_Settings.DATABASE_TABLE_MOONS;
                case STAR_MAP_LAYER_MODE_PLANET:
                    return DB_Settings.DATABASE_TABLE_PLANETS;
                case STAR_MAP_LAYER_MODE_STAR:
                    return DB_Settings.DATABASE_TABLE_STARS;
                case STAR_MAP_LAYER_MODE_SOLARSYSTEM:
                    return DB_Settings.DATABASE_TABLE_SOLARSYSTEMS;
                case STAR_MAP_LAYER_MODE_GALAXY:
                    return DB_Settings.DATABASE_TABLE_GALAXIES;
                default:
                    return null;
            }
        }

        /**
         * Draw a {@link Cube} with default setting {@link Form#SURFACED}
         */
        public void drawCube() {
            if (cube != null)
                cube.makeSurface(myCanvas, Form.SURFACED);
        }

        /**
         * Draws a {@link CoordinateSystem3D}
         */
        public void draw3DCoordinateSystem() {

            if (isCoordinateSystemDrawn) {
                //set new vector positions
                getCoordinateSystem3D().onRotate(distance, direction, axis);
                //print positions to TextView
                //setPropertiesText();
                //make the axises global
                setAxises(getCoordinateSystem3D().xAxis, getCoordinateSystem3D().yAxis, getCoordinateSystem3D().zAxis);
                //make the 3dSystem global
                setCoordinateSystem3D(XYZaxises);

                if (getCoordinateSystem3D() != null) {
                    if (getCoordinateSystem3D().getCoordinateDataAsFloat2D() != null) {
                        //draw
                        if (myCanvas != null) {
                            float[] data = getCoordinateSystem3D().getCoordinateDataAsFloat2D();
                            float[] layerValues = getCoordinateSystem3D().getLayerValues();
                            float[] xValues = getCoordinateSystem3D().getXValues();
                            float[] zValues = getCoordinateSystem3D().getZValues();

                            String str =
                                    "S(" + data[0] + "/" + data[1] + ") E(" + data[2] + "/" + data[3] + "),\n" +
                                            "S(" + data[4] + "/" + data[5] + ") E(" + data[6] + "/" + data[7] + "),\n" +
                                            "S(" + data[8] + "/" + data[9] + ") E(" + data[10] + "/" + data[11] + "),\n";

//                                setPropertiesText(str);
                            //standart single line
                            p.setStrokeWidth(150);
                            p.setAntiAlias(true);

                            //scale
                            for (float f : data) {
                                f *= mScaleFactor;
                            }

                            myCanvas.drawLine(data[0], data[1], data[2], data[3], getCoordinateSystem3D().getXPaint());
                            //standart single line
                            myCanvas.drawLine(data[4], data[5], data[6], data[7], getCoordinateSystem3D().getYPaint());

                            //standart single line
                            myCanvas.drawLine(data[8], data[9], data[10], data[11], getCoordinateSystem3D().getZPaint());


                            Paint paint = new Paint();
                            paint.setAntiAlias(true);

                            float lineWidth = 100f;
                            //draw raster
                            float k = -lineWidth * 15f;

                            //border lines from x axis view
                            //left
                            myCanvas.drawLine(data[0] + k, data[1], data[2] + k, data[3], paint);
                            //right
                            myCanvas.drawLine(data[0] + -k, data[1], data[2] + -k, data[3], paint);
                            //top
                            myCanvas.drawLine(data[0] + k, data[3], data[2] + -k, data[3], paint);
                            //bottom
                            myCanvas.drawLine(data[0] + k, data[1], data[2] + -k, data[1], paint);

                            int numberOfLines = 30;
                            //create axises
                            int f = 0;

                            while (f < numberOfLines) {
                                  /*
                                    if k would be zero, the actual axis would be overdrawn
                                     */


                                if (k != 0) {
                                    paint.setColor(Color.GRAY);
                                    myCanvas.drawLine(data[0] + k, data[1], data[2] + k, data[3], paint);
                                }
                                //y parallel
                                paint.setColor(Color.WHITE);
                                getCoordinateSystem3D().onRotate(90, DIRECTION_RIGHT, Matrix.Z_AXIS);
                                data = getCoordinateSystem3D().getCoordinateDataAsFloat2D();
                                myCanvas.drawLine(data[0] + k, data[1], data[2] + k, data[3], paint);
                                getCoordinateSystem3D().onRotate(90, DIRECTION_LEFT, Matrix.Z_AXIS);
                                data = getCoordinateSystem3D().getCoordinateDataAsFloat2D();


                                f++;
                                k += lineWidth;
                            }


//                                //shadows of the cicles
//                                p.setColor(Color.DKGRAY);
//                                p.setAlpha(40);
//                                int lightAngleY = 1;
//                                int lightAngleX = 1;
//                                int devCircle = 8;
//
//
//                                if (data[0] < 250) lightAngleX = -1;
//                                else lightAngleX = 1;
//                                if (data[1] < 250) lightAngleY = -1;
//                                else lightAngleY = 1;
//                                myCanvas.drawCircle(data[0] + (xValues[0] * lightAngleX) / devCircle, data[1] + (zValues[0] * lightAngleY) / devCircle, layerValues[0] / devCircle, p);
//                                if (data[4] < 250) lightAngleX = -1;
//                                else lightAngleX = 1;
//                                if (data[5] < 250) lightAngleY = -1;
//                                else lightAngleY = 1;
//                                myCanvas.drawCircle(data[4] + (xValues[2] * lightAngleX) / devCircle, data[5] + (zValues[2] * lightAngleY) / devCircle, layerValues[2] / devCircle, p);
//                                if (data[8] < 250) lightAngleX = -1;
//                                else lightAngleX = 1;
//                                if (data[9] < 250) lightAngleY = -1;
//                                else lightAngleY = 1;
//                                myCanvas.drawCircle(data[8] + (xValues[4] * lightAngleX) / devCircle, data[9] + (zValues[4] * lightAngleY) / devCircle, layerValues[4] / devCircle, p);
//
//                                if (data[2] < 250) lightAngleX = -1;
//                                else lightAngleX = 1;
//                                if (data[3] < 250) lightAngleY = -1;
//                                else lightAngleY = 1;
//                                myCanvas.drawCircle(data[2] + (xValues[1] * lightAngleX) / devCircle, data[3] + (zValues[1] * lightAngleY) / devCircle, layerValues[1] / devCircle, p);
//                                if (data[6] < 250) lightAngleX = -1;
//                                else lightAngleX = 1;
//                                if (data[7] < 250) lightAngleY = -1;
//                                else lightAngleY = 1;
//                                myCanvas.drawCircle(data[6] + (xValues[3] * lightAngleX) / devCircle, data[7] + (zValues[3] * lightAngleY) / devCircle, layerValues[3] / devCircle, p);
//                                if (data[10] < 250) lightAngleX = -1;
//                                else lightAngleX = 1;
//                                if (data[11] < 250) lightAngleY = -1;
//                                else lightAngleY = 1;
//                                myCanvas.drawCircle(data[10] + (xValues[5] * lightAngleX) / devCircle, data[11] + (zValues[5] * lightAngleY) / devCircle, layerValues[5] / devCircle, p);
//
//                                devCircle = 10;
//
//                                p.setColor(Color.GREEN);
//                                myCanvas.drawCircle(data[0], data[1], layerValues[0] / devCircle, p);
//                                myCanvas.drawCircle(data[4], data[5], layerValues[2] / devCircle, p);
//                                myCanvas.drawCircle(data[8], data[9], layerValues[4] / devCircle, p);
//
//                                p.setColor(Color.YELLOW);
//                                myCanvas.drawCircle(data[2], data[3], layerValues[1] / devCircle, p);
//                                myCanvas.drawCircle(data[6], data[7], layerValues[3] / devCircle, p);
//                                myCanvas.drawCircle(data[10], data[11], layerValues[5] / devCircle, p);

                        } else {
                            System.err.println("Canvas is null");
                        }
                    } else {
                        System.err.println("Coordinate Data is null");
                    }
                } else {
                    System.err.println("3DCoordinateSystem is null");
                }
            } else {

                //z-axis coordinates
                zS = new CoordinateSystem3D.CoordinateAxis.Coordinate(centerX, top);
                zE = new CoordinateSystem3D.CoordinateAxis.Coordinate(centerX, bottom);
                //y-axis coordinates
                yS = new CoordinateSystem3D.CoordinateAxis.Coordinate(left, centerY);
                yE = new CoordinateSystem3D.CoordinateAxis.Coordinate(right, centerY);
                //x-axis coordinates
                xS = new CoordinateSystem3D.CoordinateAxis.Coordinate(left, centerY / 2 + centerY);
                xE = new CoordinateSystem3D.CoordinateAxis.Coordinate(right, centerY / 2);
                //X-Y-Z Axises
                x = new CoordinateSystem3D.CoordinateAxis(xS, xE, surfaceEdges, getMe(), Matrix.X_AXIS);
                y = new CoordinateSystem3D.CoordinateAxis(yS, yE, surfaceEdges, getMe(), Matrix.Y_AXIS);
                z = new CoordinateSystem3D.CoordinateAxis(zS, zE, surfaceEdges, getMe(), Matrix.Z_AXIS);

                p = new Paint();
                //return the axises
                setAxises(x, y, z);

                //build the 3dSystem Object
                setCoordinateSystem3D(XYZaxises);

                //DEV: print all properties
                //setPropertiesText();

                //z-axis
                p.setColor(Color.BLUE);
                myCanvas.drawLine(zS.getMyX(), zS.getMyY(), zE.getMyX(), zE.getMyY(), p);

                //y-axis
                p.setColor(Color.GREEN);
                myCanvas.drawLine(yS.getMyX(), yS.getMyY(), yE.getMyX(), yE.getMyY(), p);

                //x-axis
                p.setColor(Color.RED);
                myCanvas.drawLine(xS.getMyX(), xS.getMyY(), xE.getMyX(), xE.getMyY(), p);


                isCoordinateSystemDrawn = true;
                axisesOnScreenOrigin = true;
                axisesAvailable = true;
            }
        }

        /**
         * Draws a green dot. Simple test method to check the touch behavior.
         */
        public void drawDot() {
            p.setColor(Color.GREEN);
            myCanvas.drawCircle(touchedX, touchedY, 20, p);
        }

        /**
         * Draws a rotating planet sprite.
         *
         * @param canvas
         * @param multiplicator From a loop. MIN = 1, MAX = 28. Defines the current printed image sequence of the planet image source
         * @return
         */
        public Canvas drawPlanet(Canvas canvas, int multiplicator) {

            int planetDimR = 128 * multiplicator;
            int planetDimL = planetDimR - 128;
            int planetDimB = 128;

            System.out.println("Planet Image R: " + planetDimR + " L: " + planetDimL);
            try {

                String[] list = getResources().getAssets().list("planets/");
                for (String str : list) {
                    System.out.println(str);
                }
                Bitmap planet = BitmapFactory.decodeStream(getContext().getAssets().open("planets/planet_h_1.png"));

                if (planet != null) {
                    Paint p = new Paint();

                    Rect src = new Rect(planetDimL, 0, planetDimR, planetDimB);
                    Rect dst = new Rect(200, 200, 328, 328);


                    canvas.drawBitmap(planet, src, dst, p);
                } else {
                    System.err.println("Planet Bitmap is null");
                }
            } catch (IOException io) {
                System.err.println("Bitmap planets load error " + io);
            }
            return canvas;
        }

        /**
         * <p>Calls {@link #setObjectPositionsOnMap(UniverseObject, UniverseObject[], int)} and checks if the {@link UniverseObject} object is not null.</p>
         * <p>Also checks the canvas locking status and the scale factor maximum value (atm 5f).</p>
         */
        public void prepareObjectsForDrawing() {

            int mode = getStarMapMode();
            if (!isLocked) {
                myCanvas = holder.lockCanvas();
                isLocked = true;
            }

            switch (mode) {
                case STAR_MAP_LAYER_MODE_UNIVERSE: {
                    galaxies = builder.getGalaxies();
                    multipleObjects = new UniverseObject[1][galaxies.length];
                    multipleObjects[0] = galaxies;
                    break;
                }
                case STAR_MAP_LAYER_MODE_GALAXY: {
                    if (selectedObject != null) {
                        SolarSystem[] systems = selectedObject.getSolarsystems();
                        //create on array filled with solar systems
                        multipleObjects = new UniverseObject[1][systems.length];
                        multipleObjects[0] = systems;
                    } else {
                        System.err.println("SelectedObject is null");
                    }
                    break;
                }
                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
                    if (selectedObject != null) {
                        if (selectedObject.isSolarSystem() || selectedObject instanceof SolarSystem) {
                            //create two dimensional array, first index with star(s), second with planets
                            multipleObjects = builder.getSolarSystemChildren((SolarSystem) selectedObject);
                            break;
                        } else {
                            System.err.println("When opening a solar system, a " + selectedObject + " was given as input");
                        }
                    }
                }
                default: {
                    System.err.println("Failed to initialize UniverseObjects");
                    break;
                }
            }

            switch (mode) {
                case STAR_MAP_LAYER_MODE_UNIVERSE:
                    setObjectPositionsOnMap(null, multipleObjects[0], mode);
                    break;
                case STAR_MAP_LAYER_MODE_GALAXY: {
                    if (selectedObject != null)
                        setObjectPositionsOnMap(selectedObject, multipleObjects[0], mode);
                    else
                        System.out.println("Wrong instance");

                    break;
                }
                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
                    for (int i = 0; i < multipleObjects.length; i++) {
                        for (int k = 0; k < multipleObjects[i].length; k++) {
                            setObjectPositionsOnMap(multipleObjects[i][k], multipleObjects[i], mode);
                        }
                    }
                    break;
                }
                default:
                    break;
            }

        }

        /**
         * Checks when more than 6 galaxies have a higher z value than the current one it returns false. Otherwise
         * it means that the current galaxy is one of the 7 nearest galaxies to the camera.
         *
         * @param o       The current galaxy
         * @param objects The galaxies to compare with the current one
         * @return
         */
        private boolean isOneOfTheClosest(UniverseObject o, UniverseObject[] objects) {

            int counter = 0;
            for (UniverseObject object : objects) {
                //when the loop object is the same as object "o" from arguments -> jump
                if (object.getName() == o.getName()) continue;

                if (object.getZ() > o.getZ()) {
                    counter++;
                }
            }
            if (counter > Settings.numberOfObjectsToPrintData) return false;
            else return true;
        }

        /**
         * Draws a background image sequence handled by the moving behavior of the player.
         *
         * @param canvas
         * @param direction
         * @return
         */
        private Canvas drawUniverseBackground(Canvas canvas, int direction) {

            int widthHeightRalativity = (int) (200 * 0.625f);

            int widthDivider = universeImage.getWidth() / 200;
            int heightDivider = universeImage.getHeight() / widthHeightRalativity;

            //if zoom is bigger than 25, the original image size is reached
            //TODO die zoom variable kann ich auch mit einer progress bar benutzen um eine zoom funktion zu generieren

            int zoom = 8;

            //set init position as middle of the full image
            int left = universeImage.getWidth() / 2 - (widthDivider * zoom);
            int top = universeImage.getHeight() / 2 - (heightDivider * zoom);
            int right = universeImage.getWidth() / 2 + (widthDivider * zoom);
            int bottom = universeImage.getHeight() / 2 + (heightDivider * zoom);

            //TODO hier muss ich aufpassen wenn ich die Ränder erreiche, dann wird die variable auch 0
            if (bgndLastBottom == 0 && bgndLastLeft == 0 && bgndLastRight == 0 && bgndLastTop == 0) {
                bgndLastBottom = bottom;
                bgndLastRight = right;
                bgndLastTop = top;
                bgndLastLeft = left;
            }

            int pictureMove = 1;

            if (direction == DIRECTION_LEFT) {
                bgndLastLeft -= pictureMove;
                bgndLastRight -= pictureMove;
            } else if (direction == DIRECTION_RIGHT) {
                bgndLastLeft += pictureMove;
                bgndLastRight += pictureMove;
            } else if (direction == DIRECTION_DOWN) {
                bgndLastTop -= pictureMove;
                bgndLastBottom -= pictureMove;
            } else if (direction == DIRECTION_UP) {
                bgndLastTop += pictureMove;
                bgndLastBottom += pictureMove;
            }
            Rect src = new Rect(bgndLastLeft, bgndLastTop, bgndLastRight, bgndLastBottom);

            Rect dst = new Rect(0, 0, holder.getSurfaceFrame().right, holder.getSurfaceFrame().bottom);

            canvas.drawBitmap(universeImage, null, dst, null);
            return canvas;
        }

        /**
         * This is the actual drawing method for drawing all galaxies to the map(SurfaceView)
         *
         * @param current This is the current object when handling solar systems. For Universe and Galaxy mode it can be null
         * @param objects
         * @return
         */
        private void setObjectPositionsOnMap(UniverseObject current, UniverseObject[] objects, int mapmode) {
            if (!isLocked) {
                myCanvas = holder.lockCanvas();
                isLocked = true;
            }

            switch (mapmode) {
                case STAR_MAP_LAYER_MODE_UNIVERSE: {
                    Galaxy[] galaxies = (Galaxy[]) objects;
                    //print its Galaxies
                    for (Galaxy galaxy : galaxies) {
                        float z = galaxy.getZ();
                        float y = galaxy.getY();
                        float x = galaxy.getX();
                        float radius = galaxy.getRadius();

                        if (radius / z > 0)
                            radius /= z;
                        else
                            radius = 5;

                        if (radius > 15)
                            radius = 15;


                        galaxy = (Galaxy) translateOnScreenOrigin(galaxy, false);
                        galaxy.onRotate(distance, mode, direction, axis);
                        galaxy = (Galaxy) translateOnScreenOrigin(galaxy, true);

                        if (z > 0) {
                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);
                        }

                        //print data of the 7 closest objects to camera
                        p.setColor(Color.WHITE);

                        if (radius <= 0)
                            myCanvas.drawPoint(x * mScaleFactor, y * mScaleFactor, p);
                        else
                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);

                        //
                        drawClosestObjectsToCamera(galaxy, galaxies, x, y, z, p);
                        //
                        drawConnectionLineToClosestObject(galaxy, galaxies, x, y);

//                        volume devider of current galaxy
//                        float dim = galaxy.getVolume()/3;
//                        Cube room = new Cube(dim,dim,dim, Color.BLUE,new Vector(galaxy.getX(),galaxy.getY(),galaxy.getZ()));
//                        room.draw(myCanvas,Cube.WIREFRAME);

                        myCanvas.drawPoint(x, y, setPaintByMapMode(mapmode));

                        float depth = 0f;
                        if (z < 0)
                            depth = z * -1f;
                        else
                            depth = z;

                        Paint pG = new Paint();
                        pG.setColor(Color.BLUE);

                        float vol, dim;
                        vol = (galaxy.getVolume() / 3f);
                        dim = vol + z / 10;

//                        Cube cube = new Cube(dim, dim / 3f, depth, Color.BLUE, new Vector(x - vol / 2, y + vol / 2, z - vol / 4));
//                        cube.draw(myCanvas, Cube.WIREFRAME);
                    }
                    break;
                }
                case STAR_MAP_LAYER_MODE_GALAXY: {
                    SolarSystem[] solarSystems = current.getSolarsystems();
                    /*
                    Multiply the positions of the contained solar systems
                    with the relative size of galaxy and the screen
                     */
                    float vol = current.getVolume();
                    float multiplier = vol / 3f;
                    multiplier /= ((centerX * 2) * (centerY * 2)) / 2f;

                    //print its solarsystems
                    for (SolarSystem solarSystem : solarSystems) {
                        float z = solarSystem.getZ() * multiplier;
                        float y = solarSystem.getY() * multiplier;
                        float x = solarSystem.getX() * multiplier;
                        float radius = solarSystem.getRadius();

                        if (radius / z > 0)
                            radius /= z;
                        else
                            radius = 5;

                        if (radius > 15)
                            radius = 15;

                        solarSystem = (SolarSystem) translateOnScreenOrigin(solarSystem, false);
                        solarSystem.onRotate(distance, mode, direction, axis);
                        solarSystem = (SolarSystem) translateOnScreenOrigin(solarSystem, true);

                        if (z > 0) {
                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);
                        }

                        //print data of the 7 closest objects to camera
                        p.setColor(Color.WHITE);

                        if (radius <= 0)
                            myCanvas.drawPoint(x * mScaleFactor, y * mScaleFactor, p);
                        else
                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);

                        //
                        drawClosestObjectsToCamera(solarSystem, solarSystems, x, y, z, p);
                        //
                        drawConnectionLineToClosestObject(solarSystem, solarSystems, x, y);

                        //TODO only once if the canvas has w & h same dimensions
                        float galaxyScaleFactor = (centerX / (current.getVolume() / 3)) * 3;
                        //test line to detect the solar systems
//                        myCanvas.drawLine(centerX,centerY,solarSystem.getX(), solarSystem.getY(), setPaintByMapMode(mapmode));
                        myCanvas.drawPoint(
                                solarSystem.getX() * galaxyScaleFactor,
                                solarSystem.getY() * galaxyScaleFactor,
                                setPaintByMapMode(mapmode));
                    }
                    break;
                }
                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
                    //TODO initialize solar system drawing
                    break;
                }
            }
        }

        /**
         * Returns the kind of color and style as Paint Object depending on
         * which type of starmap mode is currently on
         *
         * @param mode
         * @return
         */
        private Paint setPaintByMapMode(int mode) {
            switch (mode) {
                case STAR_MAP_LAYER_MODE_GALAXY: {
                    p.setAntiAlias(true);
                    p.setColor(Color.YELLOW);
                    p.setStyle(Paint.Style.STROKE);
                    return p;
                }
                case STAR_MAP_LAYER_MODE_UNIVERSE: {
                    p.setAntiAlias(true);
                    p.setColor(Color.RED);
                    p.setStyle(Paint.Style.STROKE);
                    return p;
                }
                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {

                    return p;
                }
                default: {
                    return p;
                }

            }
        }

        /**
         * Draws a connection line between itself and the closest (same class) object
         *
         * @param o
         * @param objects
         * @param x
         * @param y
         */
        private void drawConnectionLineToClosestObject(UniverseObject o, UniverseObject[] objects, float x, float y) {
            p.setAlpha(33);

            //draw line to the closest object
            UniverseObject n = getClosestObject(o, objects);

            p.setAlpha(80);
            //only draw the connection line if more than one galaxy is drawn
            if (n != null)
                myCanvas.drawLine(x * mScaleFactor, y * mScaleFactor, n.getX() * mScaleFactor, n.getY() * mScaleFactor, p);
        }

        /**
         * If the current object is one of the closest to the camera the {@link com.thesaan.gameengine.android.ui.StarMapSurface Settings#numberOfObjectsToPrintData}  variable
         * defines how many), a small description of this object gets shown in the format
         * <p>e.g. for a solar system</p>
         * <p>SS.JDKJEI.198784</p>
         * <p>X: 123 Y: -45 Z: 13</p>
         *
         * @param o
         * @param objects
         * @param x
         * @param y
         * @param z
         * @param p
         */
        private void drawClosestObjectsToCamera(UniverseObject o, UniverseObject[] objects, float x, float y, float z, Paint p) {
            //print information about the galaxy if its one of the 7 nearest to the camera
            if (isOneOfTheClosest(o, objects)) {
                int dx = 1;
                int dy = 1;
                if (x < centerX) dx = -1;
                if (y > centerY) dy = -1;

                p.setColor(Color.WHITE);


                if (x < centerX) {
                    if (y < centerY) {
                        //top left
                        myCanvas.drawText("X:" + (int) x + " Y:" + (int) y + " Z:" + (int) z, (x + (150 * dx)), y, p);
                        myCanvas.drawText(o.getName(), x + (150 * dx), y + 20, p);
                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
                        myCanvas.drawLine(x + 5 * dx, y + 5 * dy, x + 15 * dx, y + 15 * dy, p);//diagonal
                    } else {
                        //bottom left
                        myCanvas.drawText("X:" + (int) x + " Y:" + (int) y + " Z:" + (int) z, (x + (150 * dx)), y, p);
                        myCanvas.drawText(o.getName(), x + (150 * dx), y - 20, p);
                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
                        myCanvas.drawLine(x + 5 * dx, y + 5 * dy, x + 15 * dx, y + 15 * dy, p);//diagonal
                    }
                } else {
                    if (y < centerY) {
                        //top right
                        myCanvas.drawText("Z:" + (int) z + " Y:" + (int) y + " X:" + (int) x, (x + (45 * dx)), y, p);
                        myCanvas.drawText(o.getName(), x + (45 * dx), y + 20, p);
                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
                        myCanvas.drawLine(x + 5 * dx, y + 5, x + 15 * dx, y + 15 * dy, p);//diagonal
                    } else {
                        //bottom right
                        myCanvas.drawText("Z:" + (int) z + " Y:" + (int) y + " X:" + (int) x, (x + 45), y, p);
                        myCanvas.drawText(o.getName(), x + (45 * dx), y - 20, p);
                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
                        myCanvas.drawLine(x + 5 * dx, y + 5 * dy, x + 15 * dx, y + 15 * dy, p);//diagonal
                    }
                }
            }
        }

        /**
         * Translates the object origin to the screen origin to calculate the position correctly.
         *
         * @param o          The {@link UniverseObject} to translate
         * @param isOnOrigin <p>false - the object origin is on the correct position where is has to be drawn</p>
         *                   <p>true - the object origin is on the screen origin to be able the calculate the translation correctly</p>
         * @return Object with the translated position
         */
        private UniverseObject translateOnScreenOrigin(UniverseObject o, boolean isOnOrigin) {
            if (!isOnOrigin) {
                Vector changeVec = new Vector(centerX, 0, centerY);
                o.myPosition.subtractWith(changeVec);
            } else {
                Vector changeVec = new Vector(centerX, 0, centerY);
                o.myPosition.addWith(changeVec);
            }
            return o;
        }

        /**
         * The object searches for the closest object to draw a line to this object in {@link #prepareObjectsForDrawing()}
         *
         * @param o
         * @param objects
         * @return The object which is the closest to the current one
         */
        private UniverseObject getClosestObject(UniverseObject o, UniverseObject[] objects) {

            float rangeX, rangeY, rangeZ;
            float lastrangeX, lastrangeY, lastrangeZ;
            float g1x, g2x, g1y, g2y, g1z, g2z;

            rangeX = rangeY = rangeZ = 0;
            //the init value has to be high that a smaller range can be detected
            lastrangeX = lastrangeY = lastrangeZ = 1000000;

            UniverseObject nearest = null;
            for (int i = 0; i < objects.length; i++) {

                if (o.getName() == objects[i].getName())
                    continue;

                g1x = o.getX();
                g2x = objects[i].getX();
                g1y = o.getY();
                g2y = objects[i].getY();
                g1z = o.getZ();
                g2z = objects[i].getZ();

                if (g1x < 0) g1x *= -1;
                if (g2x < 0) g2x *= -1;
                if (g1y < 0) g1y *= -1;
                if (g2y < 0) g2y *= -1;
                if (g1z < 0) g1z *= -1;
                if (g2z < 0) g2z *= -1;

                //test which value is bigger
                if (g1x > g2x) rangeX = g1x - g2x;
                else rangeX = g2x - g1x;
                if (g1y > g2y) rangeY = g1y - g2y;
                else rangeY = g2y - g1y;
                if (g1z > g2z) rangeZ = g1z - g2z;
                else rangeZ = g2z - g1z;

                if (rangeX < lastrangeX && rangeY < lastrangeY && rangeZ < lastrangeZ) {
                    lastrangeX = rangeX;
                    lastrangeY = rangeY;
                    lastrangeZ = rangeZ;
                    nearest = objects[i];

                }

            }
            return nearest;
        }

        /**
         * Returns the closest object to the camera. Means the object with the biggest z-value.
         *
         * @param objects
         * @param selectedIndex
         * @return
         */
        private UniverseObject getClosestObjectToCamera(UniverseObject[] objects, int selectedIndex) {

            UniverseObject highest = null;
            if (objects[0] != null) {

                for (int i = 0; i < selectedIndex + 1; i++) {

                    if (selectedIndex > 0) {
                        if (highest == null) {
                            highest = objects[0];
                        } else {
                            if (highest.getZ() < objects[i].getZ()) {
                                highest = objects[i];
                            }
                        }
                    } else {
                        highest = objects[0];
                    }
                }
            }
            return highest;
        }

        /**
         * Prints a 3D lineal to see the dimensions on the surface view
         *
         * @param mScaleFactor
         * @param myCanvas
         * @return
         */
        public Canvas drawLineal(float mScaleFactor, Canvas myCanvas) {
            if (!isLocked) {
                myCanvas = holder.lockCanvas();
                isLocked = true;
            }
            Paint p = new Paint();
            p.setColor(Color.WHITE);
            p.setAlpha(20);
            //
            myCanvas.drawLine(left + 20, bottom - 20, centerX, centerY, p);
            //y
            myCanvas.drawLine(left + 20, bottom - 20, right - 20, bottom - 20, p);
            //z
            myCanvas.drawLine(left + 20, bottom - 20, left + 20, top + 20, p);

            p.setAlpha(60);
            myCanvas.drawText("SF: " + mScaleFactor, left + 80, bottom - 100, p);
            myCanvas.drawText("X: " + (World.UNIVERSE_X * mScaleFactor), left + 80, bottom - 80, p);
            myCanvas.drawText("Y: " + (World.UNIVERSE_X * mScaleFactor), left + 80, bottom - 60, p);
            myCanvas.drawText("Z: " + (World.UNIVERSE_X * mScaleFactor), left + 80, bottom - 40, p);

            return myCanvas;
        }

        /**
         * Only recall {@link #prepareObjectsForDrawing()} but now the {@link #mScaleFactor} is updated
         */
        public void scale() {
            prepareObjectsForDrawing();
            //            drawLineal(mScaleFactor);

        }

    }

    /**
     * Initializing loader for all required Bitmaps. Gets started on constructor
     */
    public class BitmapLoader extends Thread implements Runnable {

        private final int planetImageWidth = 3840;
        private final int planetImageHeight = 128;
        private final int planetSceneAmount = 28;

        private final String IMAGE_FOLDER = Environment.getExternalStorageDirectory().getPath() + "/beyonduniverse/img/";

        public BitmapLoader() {

        }

        @Override
        public void run() {

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inMutable = true;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            loadPlanetBitmaps(options);
        }

        private void loadPlanetBitmaps(BitmapFactory.Options options) {
//            TypedArray planets = getResources().obtainTypedArray(R.array.planet_bitmaps);
//
//            planetBitmaps = new Bitmap[planets.getIndexCount()];
//            for (int i = 0; i < planetBitmaps.length; i++) {
//                Bitmap bm = BitmapFactory.decodeResource(getResources(), planets.getResourceId(i, -1), options);
//                planetBitmaps[i] = bm;
//            }
        }

    }

    /*----------------------------------------LISTENERS-----------------------------------*/
    public class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            System.out.println("Scalefactor: " + mScaleFactor);

            // Don't let the object get too small or too large.         Sys
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            Canvas c = holder.lockCanvas();

            c.save();

            c.scale(mScaleFactor, mScaleFactor, touchedX, touchedY);


//            UniverseObject[] objects;

//            if (getStarMapMode() == STAR_MAP_LAYER_MODE_UNIVERSE) {
//                objects = builder.getGalaxies();
//            } else {
//                objects = getCurrentparent().getChildren();
//            }
            invalidate();
            return true;
        }
    }


}


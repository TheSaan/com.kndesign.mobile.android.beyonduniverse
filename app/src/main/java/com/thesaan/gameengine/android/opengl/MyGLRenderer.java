package com.thesaan.gameengine.android.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.SystemClock;


import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.opengl.shapes.Rastar;
import com.thesaan.gameengine.android.opengl.shapes.Vertex;
import com.thesaan.gameengine.android.handler.TestHandler;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Michael on 30.12.2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    Vertex objectsVertex;

    Handler surfaceHandler;

    Context mContext;

    float touchOffset;

    /**
     * Stands for the users input origin coordinates
     */
    MathHandler.Vector origin;

    private float[] mRotationMatrix = new float[16];


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    public volatile float mAngle;
    public volatile float mLastAngle;

    //the distance the finger already moved
    //to calculate a certain angle for direct moved transistion
    public float x_moved;
    public float y_moved;

    //current touch position
    float selectX;
    float selectY;

    //defines the speed, the finger moves on the screen
    public float fingerSpeed;

    //for stopping a draw zyclus without touching the screen on start
    public boolean fingerDown;

    //axices which rotating
    private int xAxis, yAxis, zAxis;

    //just getting the screen dimension
    public int screenWidth;
    public int screenHeigth;

    boolean isMapObjectSelected = false;

    //defines during development a number to create this amount of galaxies
    public int galaxy_amount = 10;

    public float mDeltaX;
    public float mDeltaY;

    /**
     * Global map object. Created in {@link com.thesaan.beyonduniverse.MyGLSurfaceView}.
     */
    Map map;

    TestHandler test;

    Rastar rastar;

    /**
     * To make sure, all objects are loaded
     */
    public boolean objectsLoaded = false;

    /**
     * Store the accumulated rotation.
     */
    private final float[] mAccumulatedRotation = new float[16];

    /**
     * Store the current rotation.
     */
    private final float[] mCurrentRotation = new float[16];


    /**
     * @param context
     */
    public MyGLRenderer(Context context) {
        this.mContext = context;
//        world = new World(context);
//        setObjectCoordinatesList(new Map(world));

    }

    /**
     * @param gl
     * @param config
     */
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        test = new TestHandler();

        //reset axices
        xAxis = yAxis = zAxis = 0;

        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        if (map != null) {
            map.load();
            if (map.isLoaded()) {
                setObjectCoordinatesList(map);
            }
        }

        // Initialize the accumulated rotation matrix
        Matrix.setIdentityM(mAccumulatedRotation, 0);

    }

    /**
     * @param gl
     */
    public void onDrawFrame(GL10 gl) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);


        float[] scratch = new float[16];

        mAngle = -0.05f * (float) getDegreesFromTouchEvent();
//        box_test.startTimer();
//        Matrix.setRotateM(mRotationMatrix,
//                0,
//                (float)getDegreesFromTouchEvent(),
//                xAxis,
//                yAxis,
//                zAxis
//        );
//        System.out.print("[1]");
//        for (float m : mRotationMatrix) {
//            System.out.print(m);
//        }
        Matrix.setRotateM(mRotationMatrix, 0, (float) getDegreesFromTouchEvent(), xAxis, yAxis, zAxis);
//        box_test.stopTimer("OpenGLES20 setRotationMatrix");

//        MathHandler.Matrix rotation = new MathHandler.Matrix();

//        rotation.setMatrix(MathHandler.Matrix.getRotationMatrix(
//                        mAngle,
//                        xAxis,
//                        yAxis,
//                        zAxis,
//                        origin
//                )
//        );
//        rotation.setMatrix(MathHandler.Matrix.getRotationMatrix(
//                        mAngle,
//                        0,
//                        0,
//                        1,
//                        null
//                )
//        );
//
//        mRotationMatrix = rotation.toOpenGLESMatrix();

        setProjectionAndCameraView(scratch);

//        map.rotateObjects(mAngle,xAxis,yAxis,zAxis,null,origin);

//        setObjectCoordinatesList(map);
//        Triangle mT = new Triangle();

        if (map.isLoaded() && fingerDown) {
//            drawMap(objectsVertex.getPointCoords());
            objectsVertex.draw(scratch);
//            rastar.draw(scratch);

//            mV.draw(scratch);
//            mT.draw(scratch);
        }
//        TODO Only activate when GameThread is re-build for opengl
//        if (surfaceHandler != null) {
//            surfaceHandler.post(
//                    new GameThread(
//                            GameThread.ACTION_DRAW_MAP,
//                            scratch));
//        }
    }

    /**
     * @param gl
     * @param width
     * @param height
     */
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        setScreenDim(width, height);


        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 2, 7);//TODO do not set 7th arg above 3
    }

    public void drawMap(float[] mapCoords) {

        float[] mModelMatrix = new float[16];
        float[] mTemporaryMatrix = new float[16];
        // Draw a cube.
// Translate the cube into the screen.
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.translateM(mModelMatrix, 0, 0.0f, 0.8f, -3.5f);

// Set a matrix that contains the current rotation.
        Matrix.setIdentityM(mCurrentRotation, 0);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaX, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mCurrentRotation, 0, mDeltaY, 1.0f, 0.0f, 0.0f);
        mDeltaX = 0.0f;
        mDeltaY = 0.0f;

// Multiply the current rotation by the accumulated rotation, and then set the accumulated
// rotation to the result.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mCurrentRotation, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mAccumulatedRotation, 0, 16);

// Rotate the cube taking the overall rotation into account.
        Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mAccumulatedRotation, 0);
        System.arraycopy(mTemporaryMatrix, 0, mModelMatrix, 0, 16);

        mTemporaryMatrix = new float[mapCoords.length];

        Matrix.multiplyMM(mTemporaryMatrix, 0, mModelMatrix, 0, mapCoords, 0);
        System.arraycopy(mTemporaryMatrix, 0, mapCoords, 0, mapCoords.length);
    }

    /**
     * @param fingerDown
     */

    public void setFingerDown(boolean fingerDown) {
        this.fingerDown = fingerDown;
    }

    /**
     * @param view
     */
    public void setProjectionAndCameraView(float[] view) {

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //Combine the rotation matrix with the projection and camera view
        //Note that the mMVPMatrix factor *must be first* in order
        //for the matrix multiplication product to be correct.
        Matrix.multiplyMM(view, 0, mMVPMatrix, 0, mRotationMatrix, 0);

    }

    /**
     * Loading the shader
     *
     * @param type
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode) {

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /**
     * When in {@link com.thesaan.beyonduniverse.MyGLSurfaceView} is a
     * origin {@link MathHandler.Vector} defined,
     * a N Rotation matrix can get called.
     *
     * @return
     */
    public boolean hasOrigin() {
        if (origin != null)
            return true;
        else
            return false;
    }

    /**
     * Save the last angle of the finger on the first tap.
     * To make it possible to calculate the real movement of the finger
     *
     * @param mLastAngle
     */
    public void setLastAngle(float mLastAngle) {
        this.mLastAngle = mLastAngle;
//        System.out.println("MyGLRenderer: mLastAngle: "+this.mLastAngle);
    }

    public void setTouchOffset(float offset) {
        touchOffset = offset;
    }

    public void setTouchPosition(float x, float y) {
        selectX = x;
        selectY = y;
    }

    /**
     * TODO Create Volatile speed listener
     *
     * @param fingerSpeed
     */
    public void setFingerSpeed(float fingerSpeed) {
        this.fingerSpeed = fingerSpeed;
    }

    /**
     * Set the current angle
     *
     * @param angle
     * @param speed
     */
    public void setAngle(float angle, float speed) {
//        System.out.println("Speed: "+ speed);
        long time = SystemClock.uptimeMillis() % 4000L;
//        mAngle = mLastAngle -(angle * speed * 0.009f * ((int) time));
        mAngle = mLastAngle - angle;
//        mAngle = angle *  0.00009f * ((int) time);
    }

    /**
     * Set the origin to calculate the N Rotation Matrix
     *
     * @param origin
     */
    public void setOrigin(MathHandler.Vector origin) {
        this.origin = origin;
    }

    /**
     * Defines the axis multiplicators for the rotation method
     * of the Matrix class.
     */
    public void setAxices(int x, int y, int z) {
        xAxis = x;
        yAxis = y;
        zAxis = z;
    }

    /**
     * The distance the finger just moved
     *
     * @param x
     * @param y
     */
    public void setMovedDistance(float x, float y) {
        x_moved = x;
        y_moved = y;
    }

    /**
     * Set the screen dimension for further calculations
     *
     * @param width
     * @param height
     */
    public void setScreenDim(int width, int height) {
        screenWidth = width;
        screenHeigth = height;
    }

    /**
     * Get the temperature of the finger position of the last call
     *
     * @return
     */
    public double getDegreesFromTouchEvent() {
        double delta_x = x_moved - (screenWidth) / 2;
        double delta_y = (screenHeigth) / 2 - y_moved;
        double radians = Math.atan2(delta_y, delta_x);

        double r = mLastAngle - Math.toDegrees(radians);
//        System.out.println("r = "+r);
        return r;
    }

    /**
     * Get the temperature of the current finger position
     *
     * @param x
     * @param y
     * @return
     */
    public double getDegreesFromTouchEvent(float x, float y) {
        double delta_x = x - (screenWidth) / 2;
        double delta_y = (screenHeigth) / 2 - y;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(radians);
    }

    /**
     * @param context
     */
    public void setContext(Context context) {
        mContext = context;
    }

    /**
     * @return
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * @param map
     */
    public void setMap(Map map) {
        this.map = map;

    }

    /**
     * Set the {@link Map} object to make all data usable
     *
     * @param map
     */
    public void setObjectCoordinatesList(Map map) {
        this.map = map;
        if (map.getCoordList() != null) {
            objectsVertex = map.getCoordList();
            rastar = new Rastar(map.getCoordList().getPointCoords());
            System.out.println("Map vertices loaded\n");

        } else {
            System.err.println("Failed to load map vertices");
        }
    }

    public void setGalaxy_amount(int galaxy_amount) {
        this.galaxy_amount = galaxy_amount;
    }

    public void changeNumberOfObjects() {
        onSurfaceChanged(null, screenWidth, screenHeigth);
    }

    public void setSurfaceHandler(Handler surfaceHandler) {
        this.surfaceHandler = surfaceHandler;
    }
//
//    /**
//     * Handles all drawing processes for the star map
//     */
//    public class GameThread extends Thread implements Runnable {
//
//        /**
//         * These constants define  which action is going to get started
//         */
//        public final static int ACTION_DRAW_3D_SYSTEM = 0;
//        public final static int ACTION_DRAW_DOT = 1;
//        public final static int ACTION_DRAW_MAP = 2;
//        public final static int ACTION_DRAW_LINEAL = 3;
//        public final static int ACTION_DRAW_CUBE = 4;
//        public final static int ACTION_DRAW_PLAYER_ANIMATION = 5;
//        public final static int ACTION_SCALE = 6;
//        public final static int ACTION_TOUCH_OBJECT = 7;
//        public final static int ACTION_DRAW_PLANET = 8;
//
//        int mAction;
//
//        float[] mProjCamMatrix;
//
//        UniverseObject[] objects;
//
//        UniverseObject selectedObject;
//
//        /**
//         * @param action
//         * Actions are defined in
//         * {@link com.thesaan.gameengine.android.opengl.MyGLRenderer.GameThread}
//         * @param projection_matrix
//         */
//        public GameThread(int action, float[] projection_matrix) {
//            mAction = action;
//            mProjCamMatrix = projection_matrix;
//        }
//
//
//        @Override
//        public void run() {
//
//            switch (mAction) {
//
//                case ACTION_DRAW_MAP: {
//
//                    //reference objects for further calculations
//                    objects = map.getObjects();
//
//                    map.rotateObjects(mAngle, xAxis, yAxis, zAxis, null, origin);
//
//                    if (objectsLoaded) {
//                        objectsVertex.draw(mProjCamMatrix);
//                    }
//                    break;
//                }
//                case ACTION_TOUCH_OBJECT: {
//                    if (!isMapObjectSelected) {
//                        selectObject();
//                    } else {
//                        openObject();
//                    }
//                    break;
//                }
//
//            }
//        }
//
//        /**
//         * NOT IMPLEMENTED YET. Will open a small (View based) monitor with all information about
//         * this object.
//         *
//         * @param g
//         * @throws InterruptedException
//         */
//        public void openObjectDetails(Galaxy g) throws InterruptedException {
//            //the origin galaxy
//            float x, y, z;
//            x = g.getX();
//            y = g.getY();
//            z = g.getZ();
//
////            View info = findViewById(R.id.objectInfoView);
//
////            info.setLeft((int) x);
////            info.setBottom((int) y);
////            info.setLayoutParams(Action);
//
//
//        }
//
//        private void setSelectedObject(int mode, UniverseObject[] objects) {
//            Object[] selected = new UniverseObject[objects.length];
//            /*
//            * If the user taps on an object, it is possible that
//            * there are some other objects behind
//            *
//            * the following steps check the hight (Z-Value) of the
//            * tapped objects and chooses the closest one
//            * */
//            int selectedIndex = 0;
//            for (int i = 0; i < objects.length; i++) {
//                //if a object is in the touched range add it to the selected galaxies array
//                if (
//                        objects[i].getX() >= selectX - touchOffset &&
//                        objects[i].getX() <= selectX + touchOffset &&
//                        objects[i].getY() >= selectY - touchOffset &&
//                        objects[i].getZ() <= selectY + touchOffset
//                        ) {
//                    selected[selectedIndex] = objects[i];
//                    selectedIndex++;
//                }
//            }
//
//            //reduce index by one because when the last selected galaxy gets stored, the index will be raised by one also which
//            //is wrong
//            if (selectedIndex > 0)
//                selectedIndex--;
//            //now check the z (depth value) of the selected galaxies and choose this one which has the highest (nearest) value
//
//            //make it global
//            selectedObject = getClosestObjectToCamera(objects, selectedIndex);
//
////          TODO object details open
////          try {
////              openObjectDetails(selectedObject);
////          }catch (InterruptedException ex){
////              System.err.println("Thread sleep error in GameSurface.GameMapThread.goTo()");
////          }
//
//
//            //make the sub objects selectable again
//            isMapObjectSelected = true;
//        }
//
//        /**
//         * When tapping on an object, it gets selected and all data from it gets loaded dynamically.
//         * To open, just tap again on it an {@link GameThread#openObject()} gets called.
//         */
//        private void selectObject() {
//
//        }
//
//        /**
//         * If the Object was selected by {@link GameThread#selectObject()}, another tap is required to open the object.
//         * <p>
//         * Opens the {@link UniverseObject (Galaxy, Solar system, Planet, Moon, Star)} on touch. By opening this
//         * object, all children like from galaxy -> all solar systems will be drawn now and so on.
//         * </p>
//         * <p/>
//         * <p>If a few objects are stacked in the current camera position, the method compares the z-value
//         * and selects the closest (the highest z-value) one</p>
//         * <p/>
//         */
//        private void openObject() {
//
//            if (selectedObject != null) {
//
//                //change map mode by selected object
//                if (selectedObject instanceof Galaxy)
//                    setStarMapLayerDown();
//                else if (selectedObject instanceof SolarSystem) {
//
//                }
//
//
//                if (!isLocked) {
//                    myCanvas = holder.lockCanvas();
//                    isLocked = true;
//                }
//                myCanvas.drawColor(Color.BLACK);
//
//                int mode = getStarMapMode();
//
//                if (mode == STAR_MAP_LAYER_MODE_GALAXY || mode == STAR_MAP_LAYER_MODE_UNIVERSE)
//                    prepareObjectsForDrawing();
//
//                if (mode == STAR_MAP_LAYER_MODE_SOLARSYSTEM) {
//                    if (selectedObject instanceof SolarSystem) {
//                        drawStars(selectedObject.getStars());
//                    }
//                }
//
//                Toast.makeText(getContext(), "Open " + selectedObject.getTypeName() + " " + selectedObject.getName(), Toast.LENGTH_SHORT).show();
////                    drawLineal(virtualScaleFactor);
//                isMapObjectSelected = false;
//
//            } else {
//                if (getStarMapMode() != STAR_MAP_LAYER_MODE_UNIVERSE)
//                    prepareObjectsForDrawing();
//            }
//
//        }
//
//        /**
//         * In the first edition of this method the star
//         * just gets drawn by canvas circle
//         * <p/>
//         * in the final step (not yet) the star will be
//         * represented as gif animation
//         * <p/>
//         * just uncomment the lines
//         *
//         * @param stars the current star object to get the data from
//         */
//        private void drawStars(Star[] stars) {
//            if (!isLocked) {
//                myCanvas = holder.lockCanvas();
//                isLocked = true;
//            }
//
//
//            myCanvas.drawColor(Color.BLACK);
//
//            float starRadius = centerX * 0.125f;
//
//            try {
//                Paint p = new Paint();
//                p.setColor(Color.YELLOW);
////                normal canvas circle version
//                //////////////////////
//                if (stars.length == 1) {
//                    myCanvas.drawCircle(centerX, centerY, starRadius, p);
//                } else if (stars.length == 2) {
//
//                    //this is the rest of the free space width if the stars are drawn
//                    float restWidth = (centerX * 2f) - (starRadius * 2f);
//
//                    float starDistance = restWidth * 0.2f;
//
//                    myCanvas.drawCircle(centerX - (starDistance / 2), centerY, starRadius, p);
//                    p.setColor(Color.RED);
//                    myCanvas.drawCircle(centerX + (starDistance / 2), centerY, starRadius, p);
//                }
//
//                holder.unlockCanvasAndPost(myCanvas);
//                //////////////////////
////                normal canvas circle version end
//
////                gif version
//                //////////////////////
////                BitmapHandler bh = new BitmapHandler(getContext());
////                bh.playGif(star.getGifFileName());
//                //////////////////////
////                gif version end
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//
//        /**
//         * If an object on the map gets touched to open. Start this animation.
//         * NOT IMPLEMENTED YET.
//         *
//         * @param x
//         * @param y
//         * @param z
//         * @param radius
//         */
//        private void animateOpener(float x, float y, float z, float radius) {
//
//            AppDatabase db = new AppDatabase(getContext());
//
//            db.getNamesOfGalaxies();
//
//            MathHandler.Vector oldPos = db.getPositionOfObject(selectedObject.getName(), getMapTypeDBTable(getStarMapMode()));
//
//            System.out.println("Old-> X: " + oldPos.getmFloatVec()[0] + " Y: " + oldPos.getmFloatVec()[1] + " Z: " + oldPos.getmFloatVec()[2] + "\n" +
//                    "New-> X: " + x + " Y: " + y + " Z: " + z);
//            Cube animCube = new Cube(radius, radius, radius / 2, Color.BLUE, new MathHandler.Vector(x * mScaleFactor, y * mScaleFactor, z * mScaleFactor));
//            animCube.draw(myCanvas, Cube.WIREFRAME);
//
//            float r = radius;
//            boolean borderTouched = false;
//
//
//        }
//
//        /**
//         * Gets the Database table name string
//         *
//         * @param mapType
//         * @return Database table name string
//         * @see #getStarMapMode()
//         */
//        private String getMapTypeDBTable(int mapType) {
//            switch (mapType) {
//                case STAR_MAP_LAYER_MODE_CITY:
//                    return DB_Settings.DATABASE_TABLE_CITIES;
//                case STAR_MAP_LAYER_MODE_MOON:
//                    return DB_Settings.DATABASE_TABLE_MOONS;
//                case STAR_MAP_LAYER_MODE_PLANET:
//                    return DB_Settings.DATABASE_TABLE_PLANETS;
//                case STAR_MAP_LAYER_MODE_STAR:
//                    return DB_Settings.DATABASE_TABLE_STARS;
//                case STAR_MAP_LAYER_MODE_SOLARSYSTEM:
//                    return DB_Settings.DATABASE_TABLE_SOLARSYSTEMS;
//                case STAR_MAP_LAYER_MODE_GALAXY:
//                    return DB_Settings.DATABASE_TABLE_GALAXIES;
//                default:
//                    return null;
//            }
//        }
//
//        /**
//         * Draw a {@link Cube} with default setting {@link Form#SURFACED}
//         */
//        public void drawCube() {
//            if (cube != null)
//                cube.makeSurface(myCanvas, Form.SURFACED);
//        }
//
//        /**
//         * Draws a {@link CoordinateSystem3D}
//         */
//        public void draw3DCoordinateSystem() {
//
//            if (isCoordinateSystemDrawn) {
//                //set new vector positions
//                getCoordinateSystem3D().onRotate(distance, direction, axis);
//                //print positions to TextView
//                //setPropertiesText();
//                //make the axises global
//                setAxises(getCoordinateSystem3D().xAxis, getCoordinateSystem3D().yAxis, getCoordinateSystem3D().zAxis);
//                //make the 3dSystem global
//                setCoordinateSystem3D(XYZaxises);
//
//                if (getCoordinateSystem3D() != null) {
//                    if (getCoordinateSystem3D().getCoordinateDataAsFloat2D() != null) {
//                        //draw
//                        if (myCanvas != null) {
//                            float[] data = getCoordinateSystem3D().getCoordinateDataAsFloat2D();
//                            float[] layerValues = getCoordinateSystem3D().getLayerValues();
//                            float[] xValues = getCoordinateSystem3D().getXValues();
//                            float[] zValues = getCoordinateSystem3D().getZValues();
//
//                            String str =
//                                    "S(" + data[0] + "/" + data[1] + ") E(" + data[2] + "/" + data[3] + "),\n" +
//                                            "S(" + data[4] + "/" + data[5] + ") E(" + data[6] + "/" + data[7] + "),\n" +
//                                            "S(" + data[8] + "/" + data[9] + ") E(" + data[10] + "/" + data[11] + "),\n";
//
////                                setPropertiesText(str);
//                            //standart single line
//                            p.setStrokeWidth(150);
//                            p.setAntiAlias(true);
//
//                            //scale
//                            for (float f : data) {
//                                f *= mScaleFactor;
//                            }
//
//                            myCanvas.drawLine(data[0], data[1], data[2], data[3], getCoordinateSystem3D().getXPaint());
//                            //standart single line
//                            myCanvas.drawLine(data[4], data[5], data[6], data[7], getCoordinateSystem3D().getYPaint());
//
//                            //standart single line
//                            myCanvas.drawLine(data[8], data[9], data[10], data[11], getCoordinateSystem3D().getZPaint());
//
//
//                            Paint paint = new Paint();
//                            paint.setAntiAlias(true);
//
//                            float lineWidth = 100f;
//                            //draw raster
//                            float k = -lineWidth * 15f;
//
//                            //border lines from x axis view
//                            //left
//                            myCanvas.drawLine(data[0] + k, data[1], data[2] + k, data[3], paint);
//                            //right
//                            myCanvas.drawLine(data[0] + -k, data[1], data[2] + -k, data[3], paint);
//                            //top
//                            myCanvas.drawLine(data[0] + k, data[3], data[2] + -k, data[3], paint);
//                            //bottom
//                            myCanvas.drawLine(data[0] + k, data[1], data[2] + -k, data[1], paint);
//
//                            int numberOfLines = 30;
//                            //create axises
//                            int f = 0;
//
//                            while (f < numberOfLines) {
//                                  /*
//                                    if k would be zero, the actual axis would be overdrawn
//                                     */
//
//
//                                if (k != 0) {
//                                    paint.setColor(Color.GRAY);
//                                    myCanvas.drawLine(data[0] + k, data[1], data[2] + k, data[3], paint);
//                                }
//                                //y parallel
//                                paint.setColor(Color.WHITE);
//                                getCoordinateSystem3D().onRotate(90, DIRECTION_RIGHT, MathHandler.Matrix.Z_AXIS);
//                                data = getCoordinateSystem3D().getCoordinateDataAsFloat2D();
//                                myCanvas.drawLine(data[0] + k, data[1], data[2] + k, data[3], paint);
//                                getCoordinateSystem3D().onRotate(90, DIRECTION_LEFT, MathHandler.Matrix.Z_AXIS);
//                                data = getCoordinateSystem3D().getCoordinateDataAsFloat2D();
//
//
//                                f++;
//                                k += lineWidth;
//                            }
//
//
////                                //shadows of the cicles
////                                p.setColor(Color.DKGRAY);
////                                p.setAlpha(40);
////                                int lightAngleY = 1;
////                                int lightAngleX = 1;
////                                int devCircle = 8;
////
////
////                                if (data[0] < 250) lightAngleX = -1;
////                                else lightAngleX = 1;
////                                if (data[1] < 250) lightAngleY = -1;
////                                else lightAngleY = 1;
////                                myCanvas.drawCircle(data[0] + (xValues[0] * lightAngleX) / devCircle, data[1] + (zValues[0] * lightAngleY) / devCircle, layerValues[0] / devCircle, p);
////                                if (data[4] < 250) lightAngleX = -1;
////                                else lightAngleX = 1;
////                                if (data[5] < 250) lightAngleY = -1;
////                                else lightAngleY = 1;
////                                myCanvas.drawCircle(data[4] + (xValues[2] * lightAngleX) / devCircle, data[5] + (zValues[2] * lightAngleY) / devCircle, layerValues[2] / devCircle, p);
////                                if (data[8] < 250) lightAngleX = -1;
////                                else lightAngleX = 1;
////                                if (data[9] < 250) lightAngleY = -1;
////                                else lightAngleY = 1;
////                                myCanvas.drawCircle(data[8] + (xValues[4] * lightAngleX) / devCircle, data[9] + (zValues[4] * lightAngleY) / devCircle, layerValues[4] / devCircle, p);
////
////                                if (data[2] < 250) lightAngleX = -1;
////                                else lightAngleX = 1;
////                                if (data[3] < 250) lightAngleY = -1;
////                                else lightAngleY = 1;
////                                myCanvas.drawCircle(data[2] + (xValues[1] * lightAngleX) / devCircle, data[3] + (zValues[1] * lightAngleY) / devCircle, layerValues[1] / devCircle, p);
////                                if (data[6] < 250) lightAngleX = -1;
////                                else lightAngleX = 1;
////                                if (data[7] < 250) lightAngleY = -1;
////                                else lightAngleY = 1;
////                                myCanvas.drawCircle(data[6] + (xValues[3] * lightAngleX) / devCircle, data[7] + (zValues[3] * lightAngleY) / devCircle, layerValues[3] / devCircle, p);
////                                if (data[10] < 250) lightAngleX = -1;
////                                else lightAngleX = 1;
////                                if (data[11] < 250) lightAngleY = -1;
////                                else lightAngleY = 1;
////                                myCanvas.drawCircle(data[10] + (xValues[5] * lightAngleX) / devCircle, data[11] + (zValues[5] * lightAngleY) / devCircle, layerValues[5] / devCircle, p);
////
////                                devCircle = 10;
////
////                                p.setColor(Color.GREEN);
////                                myCanvas.drawCircle(data[0], data[1], layerValues[0] / devCircle, p);
////                                myCanvas.drawCircle(data[4], data[5], layerValues[2] / devCircle, p);
////                                myCanvas.drawCircle(data[8], data[9], layerValues[4] / devCircle, p);
////
////                                p.setColor(Color.YELLOW);
////                                myCanvas.drawCircle(data[2], data[3], layerValues[1] / devCircle, p);
////                                myCanvas.drawCircle(data[6], data[7], layerValues[3] / devCircle, p);
////                                myCanvas.drawCircle(data[10], data[11], layerValues[5] / devCircle, p);
//
//                        } else {
//                            System.err.println("Canvas is null");
//                        }
//                    } else {
//                        System.err.println("Coordinate Data is null");
//                    }
//                } else {
//                    System.err.println("3DCoordinateSystem is null");
//                }
//            } else {
//
//                //z-axis coordinates
//                zS = new CoordinateSystem3D.CoordinateAxis.Coordinate(centerX, top);
//                zE = new CoordinateSystem3D.CoordinateAxis.Coordinate(centerX, bottom);
//                //y-axis coordinates
//                yS = new CoordinateSystem3D.CoordinateAxis.Coordinate(left, centerY);
//                yE = new CoordinateSystem3D.CoordinateAxis.Coordinate(right, centerY);
//                //x-axis coordinates
//                xS = new CoordinateSystem3D.CoordinateAxis.Coordinate(left, centerY / 2 + centerY);
//                xE = new CoordinateSystem3D.CoordinateAxis.Coordinate(right, centerY / 2);
//                //X-Y-Z Axises
//                x = new CoordinateSystem3D.CoordinateAxis(xS, xE, surfaceEdges, getMe(), MathHandler.Matrix.X_AXIS);
//                y = new CoordinateSystem3D.CoordinateAxis(yS, yE, surfaceEdges, getMe(), MathHandler.Matrix.Y_AXIS);
//                z = new CoordinateSystem3D.CoordinateAxis(zS, zE, surfaceEdges, getMe(), MathHandler.Matrix.Z_AXIS);
//
//                p = new Paint();
//                //return the axises
//                setAxises(x, y, z);
//
//                //build the 3dSystem Object
//                setCoordinateSystem3D(XYZaxises);
//
//                //DEV: print all properties
//                //setPropertiesText();
//
//                //z-axis
//                p.setColor(Color.BLUE);
//                myCanvas.drawLine(zS.getMyX(), zS.getMyY(), zE.getMyX(), zE.getMyY(), p);
//
//                //y-axis
//                p.setColor(Color.GREEN);
//                myCanvas.drawLine(yS.getMyX(), yS.getMyY(), yE.getMyX(), yE.getMyY(), p);
//
//                //x-axis
//                p.setColor(Color.RED);
//                myCanvas.drawLine(xS.getMyX(), xS.getMyY(), xE.getMyX(), xE.getMyY(), p);
//
//
//                isCoordinateSystemDrawn = true;
//                axisesOnScreenOrigin = true;
//                axisesAvailable = true;
//            }
//        }
//
//        /**
//         * Draws a green dot. Simple box_test method to check the touch behavior.
//         */
//        public void drawDot() {
//            p.setColor(Color.GREEN);
//            myCanvas.drawCircle(touchedX, touchedY, 20, p);
//        }
//
//        /**
//         * Draws a rotating planet sprite.
//         *
//         * @param canvas
//         * @param multiplicator From a loop. MIN = 1, MAX = 28. Defines the current printed image sequence of the planet image source
//         * @return
//         */
//        public Canvas drawPlanet(Canvas canvas, int multiplicator) {
//
//            int planetDimR = 128 * multiplicator;
//            int planetDimL = planetDimR - 128;
//            int planetDimB = 128;
//
//            System.out.println("Planet Image R: " + planetDimR + " L: " + planetDimL);
//            try {
//
//                String[] list = getResources().getAssets().list("planets/");
//                for (String str : list) {
//                    System.out.println(str);
//                }
//                Bitmap planet = BitmapFactory.decodeStream(getContext().getAssets().open("planets/planet_h_1.png"));
//
//                if (planet != null) {
//                    Paint p = new Paint();
//
//                    Rect src = new Rect(planetDimL, 0, planetDimR, planetDimB);
//                    Rect dst = new Rect(200, 200, 328, 328);
//
//
//                    canvas.drawBitmap(planet, src, dst, p);
//                } else {
//                    System.err.println("Planet Bitmap is null");
//                }
//            } catch (IOException io) {
//                System.err.println("Bitmap planets load error " + io);
//            }
//            return canvas;
//        }
//
//        /**
//         * <p>Calls {@link #setObjectPositionsOnMap(UniverseObject, UniverseObject[], int)} and checks if the {@link UniverseObject} object is not null.</p>
//         * <p>Also checks the canvas locking status and the scale factor maximum value (atm 5f).</p>
//         */
//        public void prepareObjectsForDrawing() {
//
//            int mode = getStarMapMode();
//            if (!isLocked) {
//                myCanvas = holder.lockCanvas();
//                isLocked = true;
//            }
//
//            switch (mode) {
//                case STAR_MAP_LAYER_MODE_UNIVERSE: {
//                    galaxies = builder.getGalaxies();
//                    multipleObjects = new UniverseObject[1][galaxies.length];
//                    multipleObjects[0] = galaxies;
//                    break;
//                }
//                case STAR_MAP_LAYER_MODE_GALAXY: {
//                    if (selectedObject != null) {
//                        SolarSystem[] systems = selectedObject.getSolarsystems();
//                        //create on array filled with solar systems
//                        multipleObjects = new UniverseObject[1][systems.length];
//                        multipleObjects[0] = systems;
//                    } else {
//                        System.err.println("SelectedObject is null");
//                    }
//                    break;
//                }
//                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
//                    if (selectedObject != null) {
//                        if (selectedObject.isSolarSystem() || selectedObject instanceof SolarSystem) {
//                            //create two dimensional array, first index with star(s), second with planets
//                            multipleObjects = builder.getSolarSystemChildren((SolarSystem) selectedObject);
//                            break;
//                        } else {
//                            System.err.println("When opening a solar system, a " + selectedObject + " was given as input");
//                        }
//                    }
//                }
//                default: {
//                    System.err.println("Failed to initialize UniverseObjects");
//                    break;
//                }
//            }
//
//            switch (mode) {
//                case STAR_MAP_LAYER_MODE_UNIVERSE:
//                    setObjectPositionsOnMap(null, multipleObjects[0], mode);
//                    break;
//                case STAR_MAP_LAYER_MODE_GALAXY: {
//                    if (selectedObject != null)
//                        setObjectPositionsOnMap(selectedObject, multipleObjects[0], mode);
//                    else
//                        System.out.println("Wrong instance");
//
//                    break;
//                }
//                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
//                    for (int i = 0; i < multipleObjects.length; i++) {
//                        for (int k = 0; k < multipleObjects[i].length; k++) {
//                            setObjectPositionsOnMap(multipleObjects[i][k], multipleObjects[i], mode);
//                        }
//                    }
//                    break;
//                }
//                default:
//                    break;
//            }
//
//        }
//
//        /**
//         * Checks when more than 6 galaxies have a higher z value than the current one it returns false. Otherwise
//         * it means that the current galaxy is one of the 7 nearest galaxies to the camera.
//         *
//         * @param o       The current galaxy
//         * @param objects The galaxies to compare with the current one
//         * @return
//         */
//        private boolean isOneOfTheClosest(UniverseObject o, UniverseObject[] objects) {
//
//            int counter = 0;
//            for (UniverseObject object : objects) {
//                //when the loop object is the same as object "o" from arguments -> jump
//                if (object.getName() == o.getName()) continue;
//
//                if (object.getZ() > o.getZ()) {
//                    counter++;
//                }
//            }
//            if (counter > Settings.numberOfObjectsToPrintData) return false;
//            else return true;
//        }
//
//        /**
//         * Draws a background image sequence handled by the moving behavior of the player.
//         *
//         * @param canvas
//         * @param direction
//         * @return
//         */
//        private Canvas drawUniverseBackground(Canvas canvas, int direction) {
//
//            int widthHeightRalativity = (int) (200 * 0.625f);
//
//            int widthDivider = universeImage.getWidth() / 200;
//            int heightDivider = universeImage.getHeight() / widthHeightRalativity;
//
//            //if zoom is bigger than 25, the original image size is reached
//            //TODO die zoom variable kann ich auch mit einer progress bar benutzen um eine zoom funktion zu generieren
//
//            int zoom = 8;
//
//            //set init position as middle of the full image
//            int left = universeImage.getWidth() / 2 - (widthDivider * zoom);
//            int top = universeImage.getHeight() / 2 - (heightDivider * zoom);
//            int right = universeImage.getWidth() / 2 + (widthDivider * zoom);
//            int bottom = universeImage.getHeight() / 2 + (heightDivider * zoom);
//
//            //TODO hier muss ich aufpassen wenn ich die Ränder erreiche, dann wird die variable auch 0
//            if (bgndLastBottom == 0 && bgndLastLeft == 0 && bgndLastRight == 0 && bgndLastTop == 0) {
//                bgndLastBottom = bottom;
//                bgndLastRight = right;
//                bgndLastTop = top;
//                bgndLastLeft = left;
//            }
//
//            int pictureMove = 1;
//
//            if (direction == DIRECTION_LEFT) {
//                bgndLastLeft -= pictureMove;
//                bgndLastRight -= pictureMove;
//            } else if (direction == DIRECTION_RIGHT) {
//                bgndLastLeft += pictureMove;
//                bgndLastRight += pictureMove;
//            } else if (direction == DIRECTION_DOWN) {
//                bgndLastTop -= pictureMove;
//                bgndLastBottom -= pictureMove;
//            } else if (direction == DIRECTION_UP) {
//                bgndLastTop += pictureMove;
//                bgndLastBottom += pictureMove;
//            }
//            Rect src = new Rect(bgndLastLeft, bgndLastTop, bgndLastRight, bgndLastBottom);
//
//            Rect dst = new Rect(0, 0, holder.getSurfaceFrame().right, holder.getSurfaceFrame().bottom);
//
//            canvas.drawBitmap(universeImage, null, dst, null);
//            return canvas;
//        }
//
//        /**
//         * This is the actual drawing method for drawing all galaxies to the map(SurfaceView)
//         *
//         * @param current This is the current object when handling solar systems. For Universe and Galaxy mode it can be null
//         * @param objects
//         * @return
//         */
//        private void setObjectPositionsOnMap(UniverseObject current, UniverseObject[] objects, int mapmode) {
//            if (!isLocked) {
//                myCanvas = holder.lockCanvas();
//                isLocked = true;
//            }
//
//            switch (mapmode) {
//                case STAR_MAP_LAYER_MODE_UNIVERSE: {
//                    Galaxy[] galaxies = (Galaxy[]) objects;
//                    //print its Galaxies
//                    for (Galaxy galaxy : galaxies) {
//                        float z = galaxy.getZ();
//                        float y = galaxy.getY();
//                        float x = galaxy.getX();
//                        float radius = galaxy.getRadius();
//
//                        if (radius / z > 0)
//                            radius /= z;
//                        else
//                            radius = 5;
//
//                        if (radius > 15)
//                            radius = 15;
//
//
//                        galaxy = (Galaxy) translateOnScreenOrigin(galaxy, false);
//                        galaxy.onRotate(distance, mode, direction, axis);
//                        galaxy = (Galaxy) translateOnScreenOrigin(galaxy, true);
//
//                        if (z > 0) {
//                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);
//                        }
//
//                        //print data of the 7 closest objects to camera
//                        p.setColor(Color.WHITE);
//
//                        if (radius <= 0)
//                            myCanvas.drawPoint(x * mScaleFactor, y * mScaleFactor, p);
//                        else
//                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);
//
//                        //
//                        drawClosestObjectsToCamera(galaxy, galaxies, x, y, z, p);
//                        //
//                        drawConnectionLineToClosestObject(galaxy, galaxies, x, y);
//
////                        volume devider of current galaxy
////                        float dim = galaxy.getVolume()/3;
////                        Cube room = new Cube(dim,dim,dim, Color.BLUE,new Vector(galaxy.getX(),galaxy.getY(),galaxy.getZ()));
////                        room.draw(myCanvas,Cube.WIREFRAME);
//
//                        myCanvas.drawPoint(x, y, setPaintByMapMode(mapmode));
//
//                        float depth = 0f;
//                        if (z < 0)
//                            depth = z * -1f;
//                        else
//                            depth = z;
//
//                        Paint pG = new Paint();
//                        pG.setColor(Color.BLUE);
//
//                        float vol, dim;
//                        vol = (galaxy.getVolume() / 3f);
//                        dim = vol + z / 10;
//
////                        Cube cube = new Cube(dim, dim / 3f, depth, Color.BLUE, new Vector(x - vol / 2, y + vol / 2, z - vol / 4));
////                        cube.draw(myCanvas, Cube.WIREFRAME);
//                    }
//                    break;
//                }
//                case STAR_MAP_LAYER_MODE_GALAXY: {
//                    SolarSystem[] solarSystems = current.getSolarsystems();
//                    /*
//                    Multiply the positions of the contained solar systems
//                    with the relative size of galaxy and the screen
//                     */
//                    float vol = current.getVolume();
//                    float multiplier = vol / 3f;
//                    multiplier /= ((centerX * 2) * (centerY * 2)) / 2f;
//
//                    //print its solarsystems
//                    for (SolarSystem solarSystem : solarSystems) {
//                        float z = solarSystem.getZ() * multiplier;
//                        float y = solarSystem.getY() * multiplier;
//                        float x = solarSystem.getX() * multiplier;
//                        float radius = solarSystem.getRadius();
//
//                        if (radius / z > 0)
//                            radius /= z;
//                        else
//                            radius = 5;
//
//                        if (radius > 15)
//                            radius = 15;
//
//                        solarSystem = (SolarSystem) translateOnScreenOrigin(solarSystem, false);
//                        solarSystem.onRotate(distance, mode, direction, axis);
//                        solarSystem = (SolarSystem) translateOnScreenOrigin(solarSystem, true);
//
//                        if (z > 0) {
//                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);
//                        }
//
//                        //print data of the 7 closest objects to camera
//                        p.setColor(Color.WHITE);
//
//                        if (radius <= 0)
//                            myCanvas.drawPoint(x * mScaleFactor, y * mScaleFactor, p);
//                        else
//                            myCanvas.drawCircle(x * mScaleFactor, y * mScaleFactor, radius, p);
//
//                        //
//                        drawClosestObjectsToCamera(solarSystem, solarSystems, x, y, z, p);
//                        //
//                        drawConnectionLineToClosestObject(solarSystem, solarSystems, x, y);
//
//                        //TODO only once if the canvas has w & h same dimensions
//                        float galaxyScaleFactor = (centerX / (current.getVolume() / 3)) * 3;
//                        //box_test line to detect the solar systems
////                        myCanvas.drawLine(centerX,centerY,solarSystem.getX(), solarSystem.getY(), setPaintByMapMode(mapmode));
//                        myCanvas.drawPoint(
//                                solarSystem.getX() * galaxyScaleFactor,
//                                solarSystem.getY() * galaxyScaleFactor,
//                                setPaintByMapMode(mapmode));
//                    }
//                    break;
//                }
//                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
//                    //TODO initialize solar system drawing
//                    break;
//                }
//            }
//        }
//
//        /**
//         * Returns the kind of color and style as Paint Object depending on
//         * which type of starmap mode is currently on
//         *
//         * @param mode
//         * @return
//         */
//        private Paint setPaintByMapMode(int mode) {
//            switch (mode) {
//                case STAR_MAP_LAYER_MODE_GALAXY: {
//                    p.setAntiAlias(true);
//                    p.setColor(Color.YELLOW);
//                    p.setStyle(Paint.Style.STROKE);
//                    return p;
//                }
//                case STAR_MAP_LAYER_MODE_UNIVERSE: {
//                    p.setAntiAlias(true);
//                    p.setColor(Color.RED);
//                    p.setStyle(Paint.Style.STROKE);
//                    return p;
//                }
//                case STAR_MAP_LAYER_MODE_SOLARSYSTEM: {
//
//                    return p;
//                }
//                default: {
//                    return p;
//                }
//
//            }
//        }
//
//        /**
//         * Draws a connection line between itself and the closest (same class) object
//         *
//         * @param o
//         * @param objects
//         * @param x
//         * @param y
//         */
//        private void drawConnectionLineToClosestObject(UniverseObject o, UniverseObject[] objects, float x, float y) {
//            p.setAlpha(33);
//
//            //draw line to the closest object
//            UniverseObject n = getClosestObject(o, objects);
//
//            p.setAlpha(80);
//            //only draw the connection line if more than one galaxy is drawn
//            if (n != null)
//                myCanvas.drawLine(x * mScaleFactor, y * mScaleFactor, n.getX() * mScaleFactor, n.getY() * mScaleFactor, p);
//        }
//
//        /**
//         * If the current object is one of the closest to the camera the {@link com.thesaan.gameengine.android.ui.StarMapSurface Settings#numberOfObjectsToPrintData}  variable
//         * defines how many), a small description of this object gets shown in the format
//         * <p>e.g. for a solar system</p>
//         * <p>SS.JDKJEI.198784</p>
//         * <p>X: 123 Y: -45 Z: 13</p>
//         *
//         * @param o
//         * @param objects
//         * @param x
//         * @param y
//         * @param z
//         * @param p
//         */
//        private void drawClosestObjectsToCamera(UniverseObject o, UniverseObject[] objects, float x, float y, float z, Paint p) {
//            //print information about the galaxy if its one of the 7 nearest to the camera
//            if (isOneOfTheClosest(o, objects)) {
//                int dx = 1;
//                int dy = 1;
//                if (x < centerX) dx = -1;
//                if (y > centerY) dy = -1;
//
//                p.setColor(Color.WHITE);
//
//
//                if (x < centerX) {
//                    if (y < centerY) {
//                        //top left
//                        myCanvas.drawText("X:" + (int) x + " Y:" + (int) y + " Z:" + (int) z, (x + (150 * dx)), y, p);
//                        myCanvas.drawText(o.getName(), x + (150 * dx), y + 20, p);
//                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
//                        myCanvas.drawLine(x + 5 * dx, y + 5 * dy, x + 15 * dx, y + 15 * dy, p);//diagonal
//                    } else {
//                        //bottom left
//                        myCanvas.drawText("X:" + (int) x + " Y:" + (int) y + " Z:" + (int) z, (x + (150 * dx)), y, p);
//                        myCanvas.drawText(o.getName(), x + (150 * dx), y - 20, p);
//                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
//                        myCanvas.drawLine(x + 5 * dx, y + 5 * dy, x + 15 * dx, y + 15 * dy, p);//diagonal
//                    }
//                } else {
//                    if (y < centerY) {
//                        //top right
//                        myCanvas.drawText("Z:" + (int) z + " Y:" + (int) y + " X:" + (int) x, (x + (45 * dx)), y, p);
//                        myCanvas.drawText(o.getName(), x + (45 * dx), y + 20, p);
//                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
//                        myCanvas.drawLine(x + 5 * dx, y + 5, x + 15 * dx, y + 15 * dy, p);//diagonal
//                    } else {
//                        //bottom right
//                        myCanvas.drawText("Z:" + (int) z + " Y:" + (int) y + " X:" + (int) x, (x + 45), y, p);
//                        myCanvas.drawText(o.getName(), x + (45 * dx), y - 20, p);
//                        myCanvas.drawLine(x + 15 * dx, y + 15 * dy, x + 40 * dx, y + 15 * dy, p);//horizontal
//                        myCanvas.drawLine(x + 5 * dx, y + 5 * dy, x + 15 * dx, y + 15 * dy, p);//diagonal
//                    }
//                }
//            }
//        }
//
//        /**
//         * Translates the object origin to the screen origin to calculate the position correctly.
//         *
//         * @param o          The {@link UniverseObject} to translate
//         * @param isOnOrigin <p>false - the object origin is on the correct position where is has to be drawn</p>
//         *                   <p>true - the object origin is on the screen origin to be able the calculate the translation correctly</p>
//         * @return Object with the translated position
//         */
//        private UniverseObject translateOnScreenOrigin(UniverseObject o, boolean isOnOrigin) {
//            if (!isOnOrigin) {
//                MathHandler.Vector changeVec = new MathHandler.Vector(centerX, 0, centerY);
//                o.myPosition.subtractWith(changeVec);
//            } else {
//                MathHandler.Vector changeVec = new MathHandler.Vector(centerX, 0, centerY);
//                o.myPosition.addWith(changeVec);
//            }
//            return o;
//        }
//
//        /**
//         * The object searches for the closest object to draw a line to this object in {@link #prepareObjectsForDrawing()}
//         *
//         * @param o
//         * @param objects
//         * @return The object which is the closest to the current one
//         */
//        private UniverseObject getClosestObject(UniverseObject o, UniverseObject[] objects) {
//
//            float rangeX, rangeY, rangeZ;
//            float lastrangeX, lastrangeY, lastrangeZ;
//            float g1x, g2x, g1y, g2y, g1z, g2z;
//
//            rangeX = rangeY = rangeZ = 0;
//            //the init value has to be high that a smaller range can be detected
//            lastrangeX = lastrangeY = lastrangeZ = 1000000;
//
//            UniverseObject nearest = null;
//            for (int i = 0; i < objects.length; i++) {
//
//                if (o.getName() == objects[i].getName())
//                    continue;
//
//                g1x = o.getX();
//                g2x = objects[i].getX();
//                g1y = o.getY();
//                g2y = objects[i].getY();
//                g1z = o.getZ();
//                g2z = objects[i].getZ();
//
//                if (g1x < 0) g1x *= -1;
//                if (g2x < 0) g2x *= -1;
//                if (g1y < 0) g1y *= -1;
//                if (g2y < 0) g2y *= -1;
//                if (g1z < 0) g1z *= -1;
//                if (g2z < 0) g2z *= -1;
//
//                //box_test which value is bigger
//                if (g1x > g2x) rangeX = g1x - g2x;
//                else rangeX = g2x - g1x;
//                if (g1y > g2y) rangeY = g1y - g2y;
//                else rangeY = g2y - g1y;
//                if (g1z > g2z) rangeZ = g1z - g2z;
//                else rangeZ = g2z - g1z;
//
//                if (rangeX < lastrangeX && rangeY < lastrangeY && rangeZ < lastrangeZ) {
//                    lastrangeX = rangeX;
//                    lastrangeY = rangeY;
//                    lastrangeZ = rangeZ;
//                    nearest = objects[i];
//
//                }
//
//            }
//            return nearest;
//        }
//
//        /**
//         * Returns the closest object to the camera. Means the object with the biggest z-value.
//         *
//         * @param objects
//         * @param selectedIndex
//         * @return
//         */
//        private UniverseObject getClosestObjectToCamera(UniverseObject[] objects, int selectedIndex) {
//
//            UniverseObject highest = null;
//            if (objects[0] != null) {
//
//                for (int i = 0; i < selectedIndex + 1; i++) {
//
//                    if (selectedIndex > 0) {
//                        if (highest == null) {
//                            highest = objects[0];
//                        } else {
//                            if (highest.getZ() < objects[i].getZ()) {
//                                highest = objects[i];
//                            }
//                        }
//                    } else {
//                        highest = objects[0];
//                    }
//                }
//            }
//            return highest;
//        }
//
//        /**
//         * Prints a 3D lineal to see the dimensions on the surface view
//         *
//         * @param mScaleFactor
//         * @param myCanvas
//         * @return
//         */
//        public Canvas drawLineal(float mScaleFactor, Canvas myCanvas) {
//            if (!isLocked) {
//                myCanvas = holder.lockCanvas();
//                isLocked = true;
//            }
//            Paint p = new Paint();
//            p.setColor(Color.WHITE);
//            p.setAlpha(20);
//            //
//            myCanvas.drawLine(left + 20, bottom - 20, centerX, centerY, p);
//            //y
//            myCanvas.drawLine(left + 20, bottom - 20, right - 20, bottom - 20, p);
//            //z
//            myCanvas.drawLine(left + 20, bottom - 20, left + 20, top + 20, p);
//
////            p.setAlpha(60);
////            myCanvas.drawText("SF: " + mScaleFactor, left + 80, bottom - 100, p);
////            myCanvas.drawText("X: " + (World.MA * mScaleFactor), left + 80, bottom - 80, p);
////            myCanvas.drawText("Y: " + (World.UNIVERSE_X * mScaleFactor), left + 80, bottom - 60, p);
////            myCanvas.drawText("Z: " + (World.UNIVERSE_X * mScaleFactor), left + 80, bottom - 40, p);
//
//            return myCanvas;
//        }
//
//        /**
//         * Only recall {@link #prepareObjectsForDrawing()} but now the {@link #mScaleFactor} is updated
//         */
//        public void scale() {
//            prepareObjectsForDrawing();
//            //            drawLineal(mScaleFactor);
//
//        }
//
//    }

}
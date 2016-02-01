package com.thesaan.gameengine.android.opengl;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.Handler;


import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.world.*;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.*;
import com.thesaan.gameengine.android.opengl.text.GameFont;
import com.thesaan.gameengine.android.Settings;
import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.thesaan.gameengine.android.handler.TestHandler;


import com.thesaan.gameengine.android.opengl.text.lib.GLText;
import com.threed.jpct.*;
import com.threed.jpct.Texture;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


/**
 * Created by Michael on 30.12.2015.
 */
public class MyGLRenderer implements GLSurfaceView.Renderer {

    Handler surfaceHandler;

    Context mContext;

    float touchOffset;

    GameFont font;
    GLText glfont;


    public volatile float mAngle;
    public volatile float mLastAngle;

    //the distance the finger already moved
    //to calculate a certain angle for direct moved transistion
    public float x_moved;
    public float y_moved;

    //current touch position
    float selectX;
    float selectY;

    private float fps;

    private BUObject3D centersphere = null; //test
    BUObject3D[] objects;

    private Light sun = null;
    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor back = new RGBColor(50, 50, 100);

    //just getting the screen dimension
    public int screenWidth;
    public int screenHeigth;

    boolean isMapObjectSelected = false;

    private SimpleVector pivot;

    /**
     * Global map object. Created in {@link com.thesaan.beyonduniverse.MyGLSurfaceView}.
     */
    Map map;

    TestHandler test;


    float[] mRotationMatrix = new float[16];
    float[] mProjectionMatrix = new float[16];
    float[] mViewMatrix = new float[16];
    float[] mVPMatrix = new float[16];

    private long time = System.currentTimeMillis();

    private long camStartMove;
    Camera cam;

    /**
     * @param context
     */
    public MyGLRenderer(Context context, Map map) {
        super();
        this.mContext = context;
        this.map = map;

    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

////        font = new GameFont(mContext.getAssets());
//        glfont = new GLText(mContext.getAssets());
////
//        glfont.load("Roboto-Regular.ttf", 14, 2, 2);
//
        // enable texture + alpha blending
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_ONE, GLES20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void onSurfaceChanged(GL10 gl, int w, int h) {


        if (fb != null) {
            fb.dispose();
        }
        fb = new FrameBuffer(w, h);

        world = new World();
        world.setAmbientLight(20, 20, 20);

        sun = new Light(world);
        sun.setIntensity(250, 250, 250);

        // Create a texture out of the icon...:-)

        int textureID = 0;

        Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getContext().getResources().getDrawable(R.drawable.box_test)), 64, 64));

        objects = map.getObjectModels();

        for (BUObject3D obj : objects) {
            obj.calcTextureWrapSpherical();
            TextureManager.getInstance().addTexture("texture" + textureID, texture);
            obj.setTexture("texture" + textureID);
            obj.strip();
            obj.build();

            obj.setRotationPivot(pivot);

            world.addObject(obj);

            textureID++;
        }


        centersphere = new BUObject3D(Primitives.getSphere(10f));

        TextureManager.getInstance().addTexture("texture" + textureID, texture);
        centersphere.setTexture("texture" + textureID);
        centersphere.strip();
        centersphere.build();
        centersphere.translate(pivot);
        world.addObject(centersphere);

        cam = world.getCamera();
        cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);

        cam.lookAt(pivot);

        SimpleVector sv = new SimpleVector();
        sv.set(pivot);
        sv.y -= 100;
        sv.z -= 100;
        sun.setPosition(sv);
        MemoryHelper.compact();

    }


    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        animateScene();

        float[] scratch = new float[16];

        mAngle = -0.05f * (float) getDegreesFromTouchEvent();

        Matrix.setRotateM(mRotationMatrix, 0, (float) getDegreesFromTouchEvent(), 0, 1, 1);

        setProjectionAndCameraView(scratch);

//        font.write("test", 290,290,-100,0);
//        for(UniverseObject obj: map.getObjects()){
//            obj.writeInfo(UniverseObjectProperties.INFO_POSITION,font);
//        }

//        glfont.drawTexture(screenWidth / 2, screenHeigth / 2, mVPMatrix);
//
//        glfont.begin(1.0f, 1.0f, 1.0f, 1.0f, mVPMatrix);         // Begin Text Rendering (Set Color WHITE)
//        glfont.drawC("Test String 3D!", 200f, 200f, 0f, 0, 20, 0);
//        glfont.end();

        fb.clear(back);
        world.renderScene(fb);
        world.draw(fb);
        fb.display();

        if (System.currentTimeMillis() - time >= 1000) {
//             Logger.log(fps + "fps");
            fps = 0;
            time = System.currentTimeMillis();
        }
        fps++;
    }

    /**
     * @param view
     */
    public void setProjectionAndCameraView(float[] view) {

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        //Combine the rotation matrix with the projection and camera view
        //Note that the mVPMatrix factor *must be first* in order
        //for the matrix multiplication product to be correct.
        Matrix.multiplyMM(view, 0, mVPMatrix, 0, mRotationMatrix, 0);

    }

    public void setSceneCenter(SimpleVector pivot) {
        this.pivot = pivot;
    }

    private void animateScene() {

        centersphere.rotateX(0.01f);
    }

    public void setCamStartMoveTime() {
        camStartMove = System.currentTimeMillis();

    }

    public void moveCam(float x, float y, float z) {
        if (cam != null) {
            float speed = (System.currentTimeMillis() - camStartMove) / fps;

            if (x < 0) {
                cam.moveCamera(Camera.CAMERA_MOVELEFT, speed);
            }
            if (x > 0) {
                cam.moveCamera(Camera.CAMERA_MOVERIGHT, speed);
            }
            if (y < 0) {
                cam.moveCamera(Camera.CAMERA_MOVEDOWN, speed);
            }
            if (y > 0) {
                cam.moveCamera(Camera.CAMERA_MOVEUP, speed);
            }
            if (z < 0) {
                cam.moveCamera(Camera.CAMERA_MOVEOUT, speed);
            }
            if (z > 0) {
                cam.moveCamera(Camera.CAMERA_MOVEIN, speed);
            }
        }
    }


    /**
     * From Googles API Guide
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


    public void setSurfaceHandler(Handler surfaceHandler) {
        this.surfaceHandler = surfaceHandler;
    }

    /**
     * Handles all drawing processes for the star map
     */
    public class GameThread extends Thread implements Runnable {

        /**
         * These constants define  which action is going to get started
         */
        public final static int ACTION_DRAW_3D_SYSTEM = 0;
        public final static int ACTION_DRAW_DOT = 1;
        public final static int ACTION_DRAW_MAP = 2;
        public final static int ACTION_DRAW_LINEAL = 3;
        public final static int ACTION_DRAW_CUBE = 4;
        public final static int ACTION_DRAW_PLAYER_ANIMATION = 5;
        public final static int ACTION_SCALE = 6;
        public final static int ACTION_TOUCH_OBJECT = 7;
        public final static int ACTION_DRAW_PLANET = 8;

        int mAction;

        UniverseObject[] objects;

        UniverseObject selectedObject;

        /**
         * @param action Actions are defined in
         *               {@link com.thesaan.gameengine.android.opengl.MyGLRenderer.GameThread}
         */
        public GameThread(int action) {
            mAction = action;
        }


        @Override
        public void run() {

            switch (mAction) {

                case ACTION_DRAW_MAP: {

                    //reference objects for further calculations
                    objects = map.getObjects();

                    break;
                }
                case ACTION_TOUCH_OBJECT: {

                    break;
                }

            }
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
                //if a object is in the touched range add it to the selected galaxies array
                if (
                        objects[i].getX() >= selectX - touchOffset &&
                                objects[i].getX() <= selectX + touchOffset &&
                                objects[i].getY() >= selectY - touchOffset &&
                                objects[i].getZ() <= selectY + touchOffset
                        ) {
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

//          TODO object details open
//          try {
//              openObjectDetails(selectedObject);
//          }catch (InterruptedException ex){
//              System.err.println("Thread sleep error in GameSurface.GameMapThread.goTo()");
//          }


            //make the sub objects selectable again
            isMapObjectSelected = true;
        }

        /**
         * When tapping on an object, it gets selected and all data from it gets loaded dynamically.
         * To open, just tap again on it an {@link GameThread#openObject()} gets called.
         */
        private void selectObject() {

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
         * Draws a connection line between itself and the closest (same class) object
         *
         * @param o
         * @param objects
         * @param x
         * @param y
         */
        private void drawConnectionLine(UniverseObject o, UniverseObject[] objects, float x, float y) {

            //draw line to the closest object
            UniverseObject n = getClosestObject(o, objects);

            //only draw the connection line if more than one galaxy is drawn

        }

        /**
         * @param o
         * @param objects
         * @param x
         * @param y
         * @param z
         */
        private void drawClosestObjectsToCamera(UniverseObject o, UniverseObject[] objects, float x, float y, float z) {
            //print information about the galaxy if its one of the 7 nearest to the camera
            if (isOneOfTheClosest(o, objects)) {

            }
        }


        /**
         * The object searches for the closest object to draw a line to it
         *
         * @param o
         * @param objects
         * @return The object which is the closest to the current one
         */
        private UniverseObject getClosestObject(UniverseObject o, UniverseObject[] objects) {


            return null;
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


    }

}



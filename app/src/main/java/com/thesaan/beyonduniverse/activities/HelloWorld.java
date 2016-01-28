package com.thesaan.beyonduniverse.activities;
import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.graphics.Matrix;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.Galaxy;
import com.threed.jpct.Camera;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.Primitives;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.BitmapHelper;
import com.threed.jpct.util.MemoryHelper;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 *
 * @author EgonOlsen
 *
 */
public class HelloWorld extends Activity {

    final int FINGER_SCALE = 2;

    // Used to handle pause and resume...
    private static HelloWorld master = null;

    private GLSurfaceView mGLView;
    private MyRenderer renderer = null;
    private FrameBuffer fb = null;
    private World world = null;
    private RGBColor back = new RGBColor(50, 50, 100);

    private float touchTurn = 0;
    private float touchTurnUp = 0;

    private float scaleFactor = 0;
    private float scaleFactorHistory = 0;

    private float xpos = -1;
    private float ypos = -1;

    private Object3D cube = null;
    private int fps = 0;

    private Light sun = null;

    Map map;

    Object3D[] cubes;
    //screen center position
    SimpleVector pivot;
    protected void onCreate(Bundle savedInstanceState) {

        Logger.log("onCreate");

        if (master != null) {
            copy(master);
        }

        super.onCreate(savedInstanceState);
        mGLView = new GLSurfaceView(getApplication());

        mGLView.setEGLConfigChooser(new GLSurfaceView.EGLConfigChooser() {
            public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display) {
                // Ensure that we get a 16bit framebuffer. Otherwise, we'll fall
                // back to Pixelflinger on some device (read: Samsung I7500)
                int[] attributes = new int[] { EGL10.EGL_DEPTH_SIZE, 16, EGL10.EGL_NONE };
                EGLConfig[] configs = new EGLConfig[1];
                int[] result = new int[1];
                egl.eglChooseConfig(display, attributes, configs, 1, result);
                return configs[0];
            }
        });

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        map = new Map(
                getApplicationContext(),
                displayMetrics.widthPixels,
                displayMetrics.heightPixels
        );

        if (map != null) {
            map.load();
            if (map.isLoaded()) {
                int length = map.getNumberOfObjects();
                Galaxy[] objects = (Galaxy[])map.getObjects();

                cubes = new Object3D[length];

                //center of the screen
                pivot = new SimpleVector(
                        map.getScreenWidth()/2,
                        map.getScreenHeight()/2,
                        0f);

                //set the positions of the cubes to the galaxies
                for(int i = 0; i < length; i++){

                    cubes[i] = Primitives.getCube(10f);
                    cubes[i].translate(
                            objects[i].getX(),
                            objects[i].getY(),
                            objects[i].getZ());
                }
            }
        }
        renderer = new MyRenderer();
        mGLView.setRenderer(renderer);
        setContentView(mGLView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void copy(Object src) {
        try {
            Logger.log("Copying data from master Activity!");
            Field[] fs = src.getClass().getDeclaredFields();
            for (Field f : fs) {
                f.setAccessible(true);
                f.set(this, f.get(src));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean onTouchEvent(MotionEvent me) {


        switch (me.getAction()){

            case MotionEvent.ACTION_DOWN:
                xpos = me.getX();
                ypos = me.getY();
                return true;


            case MotionEvent.ACTION_UP:
                xpos = -1;
                ypos = -1;
                touchTurn = 0;
                touchTurnUp = 0;
                return true;


            case MotionEvent.ACTION_MOVE:
                float xd = me.getX() - xpos;
                float yd = me.getY() - ypos;

                xpos = me.getX();
                ypos = me.getY();

                touchTurn = xd / -100f;
                touchTurnUp = yd / -100f;
                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                int fingers = me.getPointerCount();

                xd = me.getX() - xpos;
                yd = me.getY() - ypos;

                //when scaling horizontal
                if(xd > 0 || xd < 0){

                }
                //when scaling vertically
                if(yd > 0 || yd < 0){

                }
                switch (fingers){
                    case FINGER_SCALE:

                }
            case MotionEvent.ACTION_POINTER_UP:
//                scaleFactorHistory =



        }
//        try {
//            Thread.sleep(15);
//        } catch (Exception e) {
//            // No need for this...
//        }

        return super.onTouchEvent(me);
    }

    protected boolean isFullscreenOpaque() {
        return true;
    }

    class MyRenderer implements GLSurfaceView.Renderer {

        private long time = System.currentTimeMillis();

        public MyRenderer() {
        }

        public void onSurfaceChanged(GL10 gl, int w, int h) {
            if (fb != null) {
                fb.dispose();
            }
            fb = new FrameBuffer(gl, w, h);

            if (master == null) {

                world = new World();
                world.setAmbientLight(20, 20, 20);

                sun = new Light(world);
                sun.setIntensity(250, 250, 250);

                // Create a texture out of the icon...:-)
                Texture texture = new Texture(BitmapHelper.rescale(BitmapHelper.convert(getResources().getDrawable(R.drawable.box_test)), 64, 64));


                int textureID = 0;

                for(Object3D cube: cubes) {
                    cube.calcTextureWrapSpherical();
                    TextureManager.getInstance().addTexture("texture" + textureID, texture);
                    cube.setTexture("texture" + textureID);
                    cube.strip();
                    cube.build();

                    cube.setRotationPivot(pivot);
                    world.addObject(cube);

                    textureID++;
                }
                Camera cam = world.getCamera();
                cam.moveCamera(Camera.CAMERA_MOVEOUT, 50);
//                cam.lookAt(pivot);
                cam.lookAt(cubes[3].getTransformedCenter());

                SimpleVector sv = new SimpleVector();
                sv.set(pivot);
                sv.y -= 100;
                sv.z -= 100;
                sun.setPosition(sv);
                MemoryHelper.compact();

                if (master == null) {
                    Logger.log("Saving master Activity!");
                    master = HelloWorld.this;
                }
            }
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        public void onDrawFrame(GL10 gl) {
            animateScene();

            fb.clear(back);
            world.renderScene(fb);
            world.draw(fb);
            fb.display();

            if (System.currentTimeMillis() - time >= 1000) {
                Logger.log(fps + "fps");
                fps = 0;
                time = System.currentTimeMillis();
            }
            fps++;
        }

        private void animateScene(){
//            for(Object3D cube: cubes) {
//                if (touchTurn != 0) {
//                    cube.rotateY(touchTurn);
//                    touchTurn = 0;
//                }
//
//                if (touchTurnUp != 0) {
//                    cube.rotateX(touchTurnUp);
//                    touchTurnUp = 0;
//                }
//            }
            for(Object3D cube: cubes) {

                cube.rotateY(0.01f);

                cube.rotateX(0.01f);

            }
        }
    }
}
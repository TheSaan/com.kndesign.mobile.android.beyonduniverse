package com.thesaan.beyonduniverse.activities;

import java.lang.reflect.Field;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.thesaan.beyonduniverse.MyGLSurfaceView;
import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.SpaceObjects.UniverseObject;
import com.thesaan.gameengine.android.jpct_extend.BUObject3D;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;
import com.threed.jpct.Logger;
import com.threed.jpct.Primitives;
import com.threed.jpct.SimpleVector;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 *
 * @author EgonOlsen
 */
public class GameActivity extends Activity {

    final int FINGER_SCALE = 2;

    private MyGLSurfaceView mGLView;
    private MyGLRenderer renderer = null;


    private float touchTurn = 0;
    private float touchTurnUp = 0;


    private ScaleGestureDetector mScaleDetector;

    //the percentual factor of scaling
    private float mScaleFactor = 1f;

    private float xpos = -1;
    private float ypos = -1;

    //the current objects of the map
    UniverseObject[] objects;

    BUObject3D[] objectModels;

    Map map;
    //screen center position
    SimpleVector pivot;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());

        mGLView = new MyGLSurfaceView(getApplicationContext());


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
                objects =  map.getObjects();

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

        renderer = new MyGLRenderer(getApplicationContext(),map);

        renderer.setScreenDim(map.getScreenWidth(),map.getScreenHeight());
        renderer.setSceneCenter(pivot);

        mGLView.setRendererLink(renderer);
        mGLView.setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        mGLView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
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

    public boolean onTouchEvent(MotionEvent me) {
// Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(me);

        int fingers;

        switch (me.getAction()) {

            case MotionEvent.ACTION_DOWN:
                xpos = me.getX();
                ypos = me.getY();
                renderer.setCamStartMoveTime();
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

                fingers = me.getPointerCount();

                //2 fingers move the cam
                if(fingers == 2) {
                    renderer.moveCam(xd,yd,0);
                }else
                //3 fingers zoom
                if(fingers == 3){
                    renderer.moveCam(0,0,yd);
                }

                System.out.println("Fingers: "+fingers);
                touchTurn = xd / -100f;
                touchTurnUp = yd / -100f;

                return true;
            case MotionEvent.ACTION_POINTER_DOWN:

                return true;
            case MotionEvent.ACTION_POINTER_UP:

                return true;
        }
//        try {
//            Thread.sleep(15);
//        } catch (Exception e) {
//            // No need for this...
//        }

//        return super.onTouchEvent(me);
        return true;
    }

    protected boolean isFullscreenOpaque() {
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            //scale direction
            boolean scaleUp = true;
            float newSf = detector.getScaleFactor();

            if(mScaleFactor < newSf){
                scaleUp = false;
            }

            mScaleFactor *= newSf;

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.001f, Math.min(mScaleFactor, 5.0f));
//            System.out.println("ScaleFactor " + mScaleFactor);

            //TODO Cube Scale test
            for (BUObject3D obj : objectModels) {
                obj.onScalePosition(scaleUp,4f);
            }

//            invalidate();
            return true;
        }
    }

}
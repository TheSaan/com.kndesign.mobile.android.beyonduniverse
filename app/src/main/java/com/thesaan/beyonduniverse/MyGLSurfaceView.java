package com.thesaan.beyonduniverse;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.opengl.*;
import android.view.SurfaceHolder;

import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.beyonduniverse.gamecontent.world.World;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.TestHandler;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;

/**
 * Created by Michael on 30.12.2015.
 */
class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    Context context;


    World world;

    Map map;

    //Test
    TestHandler test;
    //Test end
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    float screenWidth, screenHeight;


    SurfaceHolder holder;

    Handler surfaceHandler;

    private float mDensity;

    //first touch on screen
    private float mFirstTouchX;
    private float mFirstTouchY;

    //last touch on screen
    private float mLastTouchX;
    private float mLastTouchY;

    private float touchOffset;

    public MyGLSurfaceView(Context context) {
        super(context);
        this.context = context;


        test = new TestHandler();

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2);

        mRenderer = new MyGLRenderer();

        mRenderer.setContext(context);

        holder = getHolder();

        surfaceHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                //TODO MessageSystem einstellen {@link https://developer.android.com/training/multiple-threads/communicate-ui.html#Handler}
            }
        };

        //initialize world
        world = new World(context);
        mRenderer.setGalaxies(world.openGalaxies(200));
//        surfaceHandler.post(new BuildThread(100, BuildThread.ACTION_CREATE_GALAXIES));


        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer);


        // Render the view only when there is a change in the drawing data
        setRenderMode(MyGLSurfaceView.RENDERMODE_WHEN_DIRTY);

        touchOffset = 10f;
    }

    public void setDensity(float density) {
        mDensity = density;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();
        int[] axices = {0, 0, 0};


        screenHeight = mRenderer.screenHeigth;
        screenWidth = mRenderer.screenWidth;

        switch (e.getAction()) {
            case MotionEvent.ACTION_UP:
                mLastTouchX = x;
                mLastTouchY = y;
            case MotionEvent.ACTION_DOWN:
                if (mRenderer != null) {
                    mRenderer.setMovedDistance(x, y);
                    mRenderer.setLastAngle((float) mRenderer.getDegreesFromTouchEvent(x, y));
                }
                //remember the position where the finger went up
                if (mLastTouchX == 0 && mLastTouchX == 0) {
                    mFirstTouchX = mLastTouchX;
                    mFirstTouchY = mLastTouchY;
                } else {
                    mFirstTouchX = x;
                    mFirstTouchY = y;
                }
                axices = getAxices(x, y);

                mLastTouchX = 0;
                mLastTouchY = 0;
            case MotionEvent.ACTION_MOVE:
//                float dx = x - mPreviousX;
//                float dy = y - mPreviousY;
                float dx = x - mFirstTouchX;
                float dy = y - mFirstTouchY;

                MathHandler.Vector origin = new MathHandler.Vector(x, y, 1);


                mRenderer.setMovedDistance(dx, dy);
                float angle = (float) mRenderer.getDegreesFromTouchEvent();


                origin.setScreenSize(mRenderer.screenWidth, mRenderer.screenHeigth);

                if (mRenderer != null && isOutOfOffset(dx, dy, touchOffset)) {
                    //roteate to get the z value
                    origin.rotate3D(angle, axices[0], axices[1], axices[2], null, origin);


                    mRenderer.setAxices(axices[0], axices[1], axices[2]);
//                    mRenderer.setAxices(0,0,1);
                    mRenderer.setOrigin(origin);
//                    mRenderer.mDeltaX += deltaX;
//                    mRenderer.mDeltaY += deltaY;
                    requestRender();
                }
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    /**
     * Makes sure, that not every tiny move call a draw
     *
     * @param x
     * @param y
     * @param offset
     * @return
     */
    public boolean isOutOfOffset(float x, float y, float offset) {
        if (offset < 0)
            offset *= -1;

//        System.out.println("x: "+x+" y: "+y+ "offset: "+offset);
        if (x > offset || x < offset * -1 || y > offset || y < offset * -1) {
//            System.out.println(true);
            return true;
        } else {
//            System.out.println(false);
            return false;
        }
    }

    public int[] getAxices(float x, float y) {
        int[] axices = {0, 0, 0};

        float tx = screenWidth / 3;
        float ty = screenHeight / 3;
        float tx2 = tx * 2;
        float ty2 = ty * 2;

        //defines the partialized areas of the screen
        float[][] areas = {
                //clockwise order
                {//top left
                        0, 0, tx, ty
                },
                {//middle left
                        0, ty, tx, ty2
                },
                {//bottom left
                        0, ty2, tx, screenHeight
                },
                {//top middle
                        tx, 0, tx2, ty
                },
                {//middle middle
                        tx, ty, tx2, ty2
                },
                {//bottom middle
                        tx, ty2, tx2, screenHeight
                },
                {//top right
                        tx2, 0, screenWidth, ty
                },
                {//middle right
                        tx2, ty, screenWidth, ty2
                },
                {//bottom right
                        tx2, ty2, screenWidth, screenHeight
                }
        };

        //check in which area starts the finger to move
        for (int i = 0; i < areas.length; i++) {
            if ((x > areas[i][0] && x < areas[i][2]) && (y > areas[i][1] && y < areas[i][3])) {


                if (i == 4 || i == 6) {
                    //X Rotation
                    axices[0] = 1;

                    printTouchedArea(i);
                    return axices;
                } else if (i == 2 || i == 8) {
                    //Y Rotation
                    axices[1] = 1;

                    printTouchedArea(i);
                    return axices;
                } else if (i == 1 || i == 3 || i == 7 || i == 9) {
                    //Z Rotation
                    axices[2] = 1;

                    printTouchedArea(i);
                    return axices;
                }
//                 else {
//                    //N Rotation
//                    axices[0] = 1;
//                    axices[1] = 1;
//                    axices[2] = 1;
//
//                    printTouchedArea(i);
//                    return axices;
//                }

            }
        }

//        System.out.println("Couldn't detect an area");
        return axices;
    }

    public void printTouchedArea(int i) {
        switch (i) {
            case 0:
                System.out.println("TOP LEFT");
                break;
            case 1:
                System.out.println("MID LEFT");
                break;
            case 2:
                System.out.println("BOTTOM LEFT");
                break;
            case 3:
                System.out.println("TOP CENTER");
                break;
            case 4:
                System.out.println("CENTER");
                break;
            case 5:
                System.out.println("BOTTOM CENTER");
                break;
            case 6:
                System.out.println("TOP RIGHT");
                break;
            case 7:
                System.out.println("MID RIGHT");
                break;
            case 8:
                System.out.println("BOTTOM RIGHT");
                break;
            default:
                System.out.println("NO AREA DETECTED");
                break;

        }
    }

    public class BuildThread extends Thread implements Runnable {


        int action;

        //is used for different kinds of run implementations
        int actionValue;

        public final static int ACTION_CREATE_GALAXIES = 100;

        /**
         * For initializing the Galaxies
         *
         * @param actionValue
         * @param action
         */
        BuildThread(int actionValue, int action) {
            this.action = action;
            this.actionValue = actionValue;
        }

        @Override
        public void run() {
            switch (action) {
                case ACTION_CREATE_GALAXIES:
                    world = new World(context);
                    test.startTimer();
                    mRenderer.setGalaxies(world.openGalaxies(actionValue));
                    mRenderer.objectsLoaded = true;
                    test.stopTimer("Create " + actionValue + " Galaxies in ");
            }
        }
    }
}

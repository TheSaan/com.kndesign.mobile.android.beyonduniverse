package com.thesaan.beyonduniverse;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;
import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.TestHandler;

/**
 * Created by Michael on 30.12.2015.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    Context context;

    //Test
    TestHandler test;

    //Test end
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    float screenWidth, screenHeight;

    private MyGLRenderer renderer;

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

        holder = getHolder();

        surfaceHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                //TODO MessageSystem einstellen {@link https://developer.android.com/training/multiple-threads/communicate-ui.html#Handler}
            }
        };

//        //to provide the display dimension
//        r.setMap(map);


        touchOffset = 10f;
    }

    public void setDensity(float density) {
        mDensity = density;
    }

    public void setScreenDimension(int width, int height) {
        screenHeight = height;
        screenWidth = width;
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

    /**
     * Get the axices depending on which screen area the finger tapped
     *
     * @param x
     * @param y
     * @return
     */
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
            if ((x > areas[i][0] &&
                    x < areas[i][2]) &&
                    y > areas[i][1] &&
                    y < areas[i][3]) {


                if (i == 3 || i == 5) {
                    //X Rotation
                    axices[0] = 1;

//                    printTouchedArea(i);
                    System.out.println("x-axis");
                    return axices;
                } else if (i == 1 || i == 7) {
                    //Y Rotation
                    axices[1] = 1;

//                    printTouchedArea(i);
                    System.out.println("y-axis");
                    return axices;
                } else if (i == 0 || i == 2 || i == 6 || i == 8) {
                    //Z Rotation
                    axices[2] = 1;

//                    printTouchedArea(i);
                    System.out.println("z-axis");
                    return axices;
                } else {
//                    printTouchedArea(i);
                    System.out.println("n-axis");
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

    /**
     * Test Method. Prints on the console on which on the nine
     * screen areas the finger tapped.
     *
     * @param i
     */
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

    public MyGLRenderer getRenderer() {
        return renderer;
    }

    /**
     * Makes the renderer data accessable
     * @param r
     */
    public void setRendererLink(MyGLRenderer r){
        this.renderer = r;
    }
    /**
     * For different background tasks. During the loading
     */
    public class BuildThread extends Thread implements Runnable {


        int action;

        //is used for different kinds of run implementations
        int actionValue;

        /**
         * Load galaxy objects in background
         */
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

            }
        }
    }
}

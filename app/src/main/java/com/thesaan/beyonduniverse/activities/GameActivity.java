package com.thesaan.beyonduniverse.activities;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.thesaan.beyonduniverse.MyGLSurfaceView;
import com.thesaan.beyonduniverse.R;
import com.thesaan.beyonduniverse.gamecontent.world.Game;
import com.thesaan.gameengine.android.handler.TestHandler;
import com.thesaan.gameengine.android.opengl.MyGLRenderer;

/**
 * A simple demo. This shows more how to use jPCT-AE than it shows how to write
 * a proper application for Android. It includes basic activity management to
 * handle pause and resume...
 *
 * @author EgonOlsen
 */
public class GameActivity extends Activity implements View.OnClickListener {

    final int FINGER_SCALE = 2;


    private float touchTurn = 0;
    private float touchTurnUp = 0;


    private ScaleGestureDetector mScaleDetector;

    //the percentual factor of scaling
    private float mScaleFactor = 1f;

    private float xpos = -1;
    private float ypos = -1;

    MyGLSurfaceView mGLView;

    ImageView add1, add2, add3, add4;

    MyGLRenderer renderer = null;

    Button btnStartGame, btnLoadGame, btnOptions, btnDevOptions, btnExit, btnCredits;

    Game game;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());

        setViews();

        //First show all companies
        setContentView(R.layout.activity_game_adds);
    }

    /**
     * Displays the companies who created the game
     * in a two second intervall
     */
    private void runAdds() {

//TODO add Advertising image diashow at the start of the game

//
//        TestHandler test = new TestHandler();
//
//        int length = adds.length;

        //show all images for a 2 seconds
//        test.startTimer();
//        for (int i = 1; i < length; i++) {
//
//            if (test.stopTime() >= 2000) {
//                //now after every 2 seconds change the image
//
//                adds[i-1].setVisibility(View.GONE);
//                adds[i].setVisibility(View.VISIBLE);
//                test.startTimer();
//            } else {
//                i--;
//            }
//        }

        //after displaying the adds, run the menu
//        setContentView(R.layout.activity_game_menu);
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
        runAdds();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
//            runAdds();
        }
    }

    public boolean onTouchEvent(MotionEvent me) {
// Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(me);

        int fingers;

        switch (me.getAction()) {

            case MotionEvent.ACTION_DOWN:

                xpos = me.getX();
                ypos = me.getY();
                if (renderer != null) {
                    renderer.setCamStartMoveTime();
                }

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

                if (renderer != null) {
                    //2 fingers move the cam
                    if (fingers == 2) {
                        renderer.moveCam(xd, yd, 0);
                    } else
                        //3 fingers zoom
                        if (fingers == 3) {
                            renderer.moveCam(0, 0, yd);
                        }
                }

                touchTurn = xd / -100f;
                touchTurnUp = yd / -100f;

                return true;
            case MotionEvent.ACTION_POINTER_DOWN:
                fingers = me.getPointerCount() - 1;
                add1 = (ImageView) findViewById(R.id.ivAdd_Add0);
                add2 = (ImageView) findViewById(R.id.ivAdd_Add1);
                add3 = (ImageView) findViewById(R.id.ivAdd_Add2);
                add4 = (ImageView) findViewById(R.id.ivAdd_Add3);

                switch (fingers) {
                    case 0:
                        add1.setVisibility(ImageView.VISIBLE);
                        add2.setVisibility(ImageView.INVISIBLE);
                        add3.setVisibility(ImageView.INVISIBLE);
                        add4.setVisibility(ImageView.INVISIBLE);
                        break;
                    case 1:
                        add1.setVisibility(ImageView.INVISIBLE);
                        add2.setVisibility(ImageView.VISIBLE);
                        add3.setVisibility(ImageView.INVISIBLE);
                        add4.setVisibility(ImageView.INVISIBLE);
                        break;
                    case 2:
                        add1.setVisibility(ImageView.INVISIBLE);
                        add2.setVisibility(ImageView.INVISIBLE);
                        add3.setVisibility(ImageView.VISIBLE);
                        add4.setVisibility(ImageView.INVISIBLE);
                        break;
                    case 3:
                        add1.setVisibility(ImageView.INVISIBLE);
                        add2.setVisibility(ImageView.INVISIBLE);
                        add3.setVisibility(ImageView.INVISIBLE);
                        add4.setVisibility(ImageView.VISIBLE);
                        break;
                }


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

    private void setViews() {

        btnStartGame = (Button) findViewById(R.id.btnStartGame);
        btnLoadGame = (Button) findViewById(R.id.btnLoadGame);
        btnOptions = (Button) findViewById(R.id.btnOptions);
        btnDevOptions = (Button) findViewById(R.id.btnDevOptions);
        btnCredits = (Button) findViewById(R.id.btnCredits);
        btnExit = (Button) findViewById(R.id.btnExit);

        btnStartGame.setOnClickListener(this);
        btnLoadGame.setOnClickListener(this);
        btnOptions.setOnClickListener(this);
        btnDevOptions.setOnClickListener(this);
        btnCredits.setOnClickListener(this);
        btnExit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStartGame:
                //start new game
                mGLView = game.start(this);
                setContentView(mGLView);
                break;
            case R.id.btnLoadGame:
                //to select a saved game to start it
                setContentView(R.layout.activity_game_load_select);
                break;
            case R.id.btnOptions:
                //open settings
                setContentView(R.layout.activity_game_options);
                break;
            case R.id.btnDevOptions:
                //test settings
                setContentView(R.layout.activity_game_developeroptions);
                break;
            case R.id.btnCredits:
                //dev team infos
                Intent credits = new Intent(getApplicationContext(), GameCreditsActivity.class);
                startActivity(credits);
                break;
            case R.id.btnExit:
                //close game
                game.exit();
                break;
        }
    }


    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {

            //scale direction
            boolean scaleUp = true;
            float newSf = detector.getScaleFactor();

            if (mScaleFactor < newSf) {
                scaleUp = false;
            }

            mScaleFactor *= newSf;

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.001f, Math.min(mScaleFactor, 5.0f));
//            System.out.println("ScaleFactor " + mScaleFactor);

            //TODO Cube Scale test
//            for (BUObject3D obj : objectModels) {
//                obj.onScalePosition(scaleUp,4f);
//            }

//            invalidate();
            return true;
        }
    }

}
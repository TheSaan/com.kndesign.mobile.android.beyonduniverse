package com.thesaan.beyonduniverse.activities;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thesaan.beyonduniverse.MyGLSurfaceView;
import com.thesaan.beyonduniverse.gamecontent.world.Map;
import com.thesaan.gameengine.android.activities.ProActivity;

import com.threed.jpct.*;
import com.threed.jpct.FrameBuffer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class GameActivity extends ProActivity {

    private MyGLSurfaceView mGLView;

    World world;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        Map map = new Map(
                getApplicationContext(),
                displayMetrics.widthPixels,
                displayMetrics.heightPixels
        );

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this, map);

        mGLView.setDensity(displayMetrics.density);
        mGLView.setScreenDimension(
                displayMetrics.widthPixels,
                displayMetrics.heightPixels);

        setContentView(mGLView);

        initViews();
    }

    private void initViews() {
        //main screen container
        LinearLayout llWindowLayout;

        //change the amount of galaxies
        SeekBar sbGalaxyAmount;

        final TextView tvGalaxyAmountInfo;

        tvGalaxyAmountInfo = new TextView(getApplicationContext());

        sbGalaxyAmount = new SeekBar(getApplicationContext());

        sbGalaxyAmount.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mGLView.getRenderer().setGalaxy_amount(progress);
                        tvGalaxyAmountInfo.setText("Calculate " + progress + " Objects to draw");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        mGLView.getRenderer().changeNumberOfObjects();
                    }
                }
        );


    }
}
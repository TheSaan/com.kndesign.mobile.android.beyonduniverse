package com.thesaan.beyonduniverse;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;

public class OpenGLESActivity extends Activity {

    private MyGLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity.
        mGLView = new MyGLSurfaceView(this);

        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mGLView.setDensity(displayMetrics.density);

        setContentView(mGLView);
    }
}
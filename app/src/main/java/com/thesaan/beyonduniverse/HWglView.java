package com.thesaan.beyonduniverse;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.ScaleGestureDetector;

/**
 * Created by Michael on 28.01.2016.
 */
public class HWglView extends GLSurfaceView {


    private ScaleGestureDetector mScaleDetector;

    //the percentual factor of scaling
    private float mScaleFactor = 0f;

    public HWglView(Context context) {
        super(context);

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    public float getScaleFactor(){
        return mScaleFactor;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            System.out.println("ScaleFactor "+mScaleFactor);
//            invalidate();
            return true;
        }
    }


}

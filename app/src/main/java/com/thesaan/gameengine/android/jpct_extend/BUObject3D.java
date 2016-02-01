package com.thesaan.gameengine.android.jpct_extend;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

/**
 * Created by Michael on 28.01.2016.
 */
public class BUObject3D extends Object3D {

    SimpleVector preScalePosition;

    private int fps = 60;

    public BUObject3D(Object3D obj) {
        super(obj);
    }

    public void setPreScalePosition(SimpleVector preScalePosition) {
        this.preScalePosition = preScalePosition;
    }

    public SimpleVector getPreScalePosition() {
        return preScalePosition;
    }

    public void resetScaleTranslation() {
        translate(preScalePosition);
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public void onScalePosition(boolean scaleUp, float frameStep) {
        float x, y, z = frameStep;
        y = x = z;

        if (scaleUp) {
            //if a value is negative make the translation positive
            if (x < 0) x *= -1;
            if (y < 0) y *= -1;
            if (z < 0) z *= -1;
        }else {
            if (x > 0) x *= -1;
            if (y > 0) y *= -1;
            if (z > 0) z *= -1;
        }
        translate(x, y, z);
    }


}

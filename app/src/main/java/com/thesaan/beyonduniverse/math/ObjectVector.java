package com.thesaan.beyonduniverse.math;

import com.thesaan.gameengine.android.handler.MathHandler;
import com.thesaan.gameengine.android.handler.MathHandler.Vector;

/**
 * Defines some special attributes especial for
 * opengl coordinates
 * Created by Michael on 24.01.2016.
 */
public class ObjectVector extends MathHandler.Vector {

    private int arrayListIndex;

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    public ObjectVector(float x, float y, float z){
        super(x,y,z);
        setIsFloat(true);
    }

    /**
     *
     * @param arrayListIndex
     */
    public void setArrayListIndex(int arrayListIndex) {
        this.arrayListIndex = arrayListIndex;
    }

    /**
     *
     * @return
     */
    public int getArrayListIndex() {
        return arrayListIndex;
    }
}

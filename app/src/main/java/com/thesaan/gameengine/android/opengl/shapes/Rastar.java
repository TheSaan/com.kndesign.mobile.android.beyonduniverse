package com.thesaan.gameengine.android.opengl.shapes;

import android.opengl.GLES20;

/**
 * Created by Michael on 25.01.2016.
 */
public class Rastar extends Shape{

    public Rastar(){

    }
    public Rastar(float[] map_coordinates) {
        super(map_coordinates);
    }

    public void draw(float[] mvpMatrix) {

        super.draw(mvpMatrix, GLES20.GL_LINES);

    }
}
